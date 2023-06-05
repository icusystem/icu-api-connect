package api;

/**
 * Thread class to run the ICU Device API state machine
 */
public final class ApiThread extends Thread implements IAPIAccount {


    public ApiThread(String baseUrl){this.baseUrl = baseUrl;}
    public ApiThread(){}


    /**
     * The State Machine states
     */
    private enum IcuState{
        SM_CONNECT, SM_WAIT,SM_STATUS,SM_GET_DEVICE,SM_PURGE_FACES,SM_GET_SETTINGS,SM_REFRESH_TOKEN
    }

    private String api_username = "";
    private String api_password = "";
    private String baseUrl = "https://192.168.137.8:44345";
    private boolean _run = false;
    private boolean readySent = false;
    private boolean setupComplete = false;
    private int faceCount = 0;
    private int noFaceCount = 0;
    private boolean sessionStart = false;
    private IDeviceEvent deviceEventListener;
    private IcuState icuState = IcuState.SM_CONNECT;
    private Token token;
    private int tokenTick = 0;
    private static final int POLL_DELAY = 100;
    private static final int CONNECT_DELAY_MS = 1000;
    private static final int TOKEN_REFRESH_TICK = (60*2*6*100);


    /**
     * @param deviceEventListener the listener interface
     */
    public void setDeviceEventListener(IDeviceEvent deviceEventListener){
        this.deviceEventListener = deviceEventListener;
    }

    /**
     * Set the ICU Device api credentials
     * @param username the ICU Device api username
     * @param password the ICU Device api password
     */
    public void setCredentials(String username, String password){
        this.api_username = username;
        this.api_password = password;
    }

    /**
     * The Thread run loop
     */
    @Override
    public void run(){
        super.run();

        ApiFunctions.setBaseUrl(baseUrl);
        ApiFunctions.setApiAccountListener(this);

        _run = true;
        while(_run){

            tokenTick += POLL_DELAY;
            // refresh the token at intervals
            if(icuState == IcuState.SM_STATUS) {
                if (tokenTick >= TOKEN_REFRESH_TICK) {
                    icuState = IcuState.SM_REFRESH_TOKEN;
                    tokenTick = 0;
                }
            }

            switch(icuState){
                case SM_CONNECT:
                    try {
                        Thread.sleep(CONNECT_DELAY_MS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    setupComplete = false;
                    readySent = false;
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.getToken(this.api_username,this.api_password);
                    break;
                case SM_WAIT:
                    // intentional empty - state is changed by callbacks
                    break;
                case SM_STATUS:
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.getStatus(token.access_token);
                    break;
                case SM_GET_DEVICE:
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.getDevice(token.access_token);
                    break;
                case SM_PURGE_FACES:
                    FaceDelete faceDelete = new FaceDelete();
                    faceDelete.uid = new String[1];
                    faceDelete.uid[0] = "all";
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.setDeleteFaces(token.access_token,faceDelete);
                    break;

                case SM_GET_SETTINGS:
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.getSettings(token.access_token);
                    break;
                case SM_REFRESH_TOKEN:
                    icuState = IcuState.SM_WAIT;
                    ApiFunctions.getToken(this.api_username,this.api_password);
                    break;
                default:
                    System.out.println("illegal state: " + icuState);

            }

            try {
                Thread.sleep(POLL_DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }

    /**
     * External control to stop the thread loop
     */
    public void Stop(){
        _run = false;
    }

    /**
     * The IAPIAccount listener events
     */
    @Override
    public void onRequestFail(ICUError icuError, String message) {
        deviceEventListener.onNewICUEvent(ICUEvent.DISCONNECTED,null);
        icuState = IcuState.SM_CONNECT;
        deviceEventListener.onICUError(icuError, message);
    }

    @Override
    public void onTokenSuccess(Token token) {
        this.token = token;

        if(!setupComplete) {
            icuState = IcuState.SM_GET_DEVICE;
            deviceEventListener.onNewICUEvent(ICUEvent.CONNECTED,null);
        }else{
            icuState =  IcuState.SM_STATUS;
        }

    }


    @Override
    public void onDeviceSuccess(DeviceDetail deviceDetail) {

        if(!setupComplete){
            deviceEventListener.onNewICUEvent(ICUEvent.INITIALISING,null);
            setupComplete = true;
        }
        deviceEventListener.onNewICUEvent(ICUEvent.DEVICE_PARAMETERS,deviceDetail);
        icuState = IcuState.SM_PURGE_FACES;

    }


    @Override
    public void onDeleteSuccess() {
        icuState = IcuState.SM_GET_SETTINGS;
    }



    @Override
    public void onSettingsSuccess(SettingsResponse settingsResponse) {
        icuState = IcuState.SM_STATUS;
        deviceEventListener.onNewICUEvent(ICUEvent.SETTINGS_PARAMETERS,settingsResponse);
    }

    @Override
    public void onStatusSuccess(StatusResponse statusResponse) {

        if(setupComplete && !readySent && statusResponse.DeviceState.equalsIgnoreCase("ready")){
            deviceEventListener.onNewICUEvent(ICUEvent.READY,null);
            readySent = true;
        }

        if(readySent) {

            if (statusResponse.FaceInFrame.get(0).Face) {
                noFaceCount = 0;
                faceCount++;
                if (!sessionStart) {
                    deviceEventListener.onNewICUEvent(ICUEvent.SESSION_START,null);
                    sessionStart = true;
                }
                if (faceCount > 0 && statusResponse.Detections.size() > 0) {
                    faceCount = 0;
                    for (int i = 0; i < statusResponse.Detections.size(); i++) {
                         Detection detection = statusResponse.Detections.get(i);
                         FaceSessionData faceSessionData = new FaceSessionData();
                        faceSessionData.age = detection.Age;
                        faceSessionData.image = detection.Image;
                        faceSessionData.uid = detection.Uid;
                        faceSessionData.gender = detection.Gender;
                        if (!detection.Uid.equalsIgnoreCase("none") ){
                            faceSessionData.feature = detection.Feature;
                            faceSessionData.featureUid  = detection.FeatureUid;
                            deviceEventListener.onNewICUEvent(ICUEvent.ID_DETECTION,faceSessionData);
                        } else {
                            deviceEventListener.onNewICUEvent(ICUEvent.AGE_DETECTION,faceSessionData);
                        }
                    }
                }
            } else {
                noFaceCount++;
                if (noFaceCount >= 3) {
                    noFaceCount = 0;
                    faceCount = 0;
                    if (sessionStart) {
                        deviceEventListener.onNewICUEvent(ICUEvent.SESSION_END,null);
                        sessionStart = false;
                    }
                }
            }

        }

        icuState = IcuState.SM_STATUS;

    }


}
