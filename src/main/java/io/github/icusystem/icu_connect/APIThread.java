package io.github.icusystem.icu_connect;


import io.github.icusystem.icu_connect.api_icu.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class APIThread extends Thread implements IAPIAccount {




    private enum ICU_SM{
        SM_IDLE,
        SM_CONNECT,
        SM_REFRESH_TOKEN,
        SM_RESPONSE,
        SM_GET_DEVICE,
        SM_GET_SETTINGS,
        SM_SET_SETTINGS,
        SM_STATUS,
        SM_SET_AGE_ONLY,
        SM_SET_AGE_ID_VERIFY,
        SM_SET_SESSION_IDLE,
        SM_GET_SESSION_STATUS,
        SM_SET_SESSION_FACE_CAPTURE,
        SM_GET_SESSION_AGE_RESULT,
        SM_GET_SESSION_SCAN_RESULT,
        SM_SET_SESSION_CARD_SCAN,
        SM_SET_FACE_REC,
        SM_SET_STREAM_SETTINGS,
        SM_SET_ENROLL_IMAGE,
        SM_PURGE_FACES,
        SM_FACES_TO_UPDATE,
        SM_FACES_TO_DELETE
    }


    private boolean _isrun = false;
    private int tokenTickCount = 0;
    private boolean validSettings = false;

    private ICU_SM icuState = ICU_SM.SM_CONNECT;
    private ICU_SM icuLastState = ICU_SM.SM_CONNECT;
    private ICU_SM icuSavedState = ICU_SM.SM_IDLE;
    private HashMap<String,LocalAPIListener> icuThreadListeners;
    private final String TAG = "APIThread";
    private Token apiToken;
    private ICUDevice icuDevice;
    private Camera requestCamera;

    private Integer lastFaceTimestamp = 0;

    private EnrollResponse enrollResponse = null;
    private UpdateFaceData updateFaceData = null;
    public boolean newFaceRecMode = false;
    public boolean newAgeOnlyMode;
    public boolean newAgeVerifyMode;
    public boolean newSettingsUpdate = false;
    public boolean newSetSessionIdle;
    public boolean newSetCardScan = false;
    public boolean newStreamFaceBox = false;
    public boolean newUpdateFaceData = false;
    public boolean newEnrollImage = false;

    public boolean newDeleteFace = false;
    public String enrollImage = "";

    private boolean readySent = false;
    private ICU_SM logICUState = ICU_SM.SM_SET_SESSION_IDLE;

    private CameraSettings cameraSettings;
    private FaceDelete deleteFaceData;

    private Integer faceBoxDisplayValue = 0;

    private final int HOURS_MS = 60*60*1000;
    private final int POLL_DELAY = 500; //ms
    private final int TOKEN_REFRESH_TIME = 2 * (HOURS_MS);
    private final int TOKEN_REFRESH_TICK = TOKEN_REFRESH_TIME/POLL_DELAY;

    private ICURunMode currentMode = ICURunMode.NONE;
    private String apiUserName;
    private String apiPassword;

    public APIThread(CameraSettings cameraSettings, String username, String password) {

        this.icuThreadListeners = new HashMap<>();
        this.icuDevice = new ICUDevice();
        this.cameraSettings = cameraSettings;
        this.apiUserName = username;
        this.apiPassword = password;
    }

    public void setLocalAPIListener(String tag, LocalAPIListener listener){
        this.icuThreadListeners.put(tag,listener);
    }
    public void removeLocalApiListener(String tag){
        for(String key : icuThreadListeners.keySet()){
            if(tag.equals(key)){
                icuThreadListeners.remove(key);
                break;
            }
        }
    }


    public void close()
    {
        _isrun = false;
    }

    public void updateSettings(CameraSettings cameraSettings){
        this.cameraSettings = cameraSettings;
        newSettingsUpdate = true;
    }

    public void deleteFaces(FaceDelete deleteFaceData){
        this.deleteFaceData = deleteFaceData;
        newDeleteFace = true;
    }


    @Override
    public void run() {
        super.run();

        ApiFunctions.Companion.setOnAPIAccountListener(this);

        _isrun = true;

        for (LocalAPIListener l : icuThreadListeners.values()) {
            l.ICUDisconnected("");
        }



        while (_isrun) {

            tokenTickCount++;
            // check for command updates
            if(readySent && (
                            icuState == ICU_SM.SM_STATUS ||
                            icuState == ICU_SM.SM_GET_SESSION_STATUS ||
                            icuState == ICU_SM.SM_IDLE
            )){

                // check for token update request
                if(tokenTickCount >= TOKEN_REFRESH_TICK){
                    icuSavedState = icuState;
                    icuState = ICU_SM.SM_REFRESH_TOKEN;
                    tokenTickCount = 0;
                }else {

                    if (newAgeOnlyMode) {
                        icuState = ICU_SM.SM_SET_AGE_ONLY;
                        newAgeOnlyMode = false;
                    } else if (newAgeVerifyMode) {
                        icuState = ICU_SM.SM_SET_AGE_ID_VERIFY;
                        newAgeVerifyMode = false;
                    } else if (newSetSessionIdle) {
                        icuState = ICU_SM.SM_SET_SESSION_IDLE;
                        newSetSessionIdle = false;
                    } else if (newSetCardScan) {
                        icuState = ICU_SM.SM_SET_SESSION_CARD_SCAN;
                        newSetCardScan = false;
                    }else if (newSettingsUpdate) {
                        icuState = ICU_SM.SM_SET_SETTINGS;
                        newSettingsUpdate = false;
                    }else if (newFaceRecMode) {
                        icuState = ICU_SM.SM_SET_FACE_REC;
                        newFaceRecMode = false;
                    } else if (newStreamFaceBox) {
                        icuState = ICU_SM.SM_SET_STREAM_SETTINGS;
                        newStreamFaceBox = false;
                    }else if (newEnrollImage) {
                        icuState = ICU_SM.SM_SET_ENROLL_IMAGE;
                        newEnrollImage = false;
                    }else if(newUpdateFaceData){
                        icuState = ICU_SM.SM_FACES_TO_UPDATE;
                        newUpdateFaceData = false;
                    }else if(newDeleteFace){
                        icuState = ICU_SM.SM_FACES_TO_DELETE;
                        newDeleteFace = false;
                    }
                }

            }

            // log for state change
            if(icuState != logICUState){
                logICUState = icuState;
            }

            switch (icuState){
                // attempt to connect to the ICU device API
                case SM_CONNECT:
                    readySent = false;
                    validSettings = false;
                    ApiFunctions.Companion.getToken(this.apiUserName,this.apiPassword);
                    icuSavedState =  ICU_SM.SM_GET_DEVICE;
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_REFRESH_TOKEN:
                    ApiFunctions.Companion.getToken(this.apiUserName,this.apiPassword);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_DEVICE:
                    icuLastState = icuState;
                    ApiFunctions.Companion.getDevice(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SETTINGS:
                    ApiFunctions.Companion.getSettings(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SETTINGS:
                    // update with preference settings
                    icuLastState = icuState;
                    ModeSet mode2 = new ModeSet();
                    mode2.getCameras().get(0).setId_Index(0);
                    mode2.getCameras().get(0).setFace_rec_en(this.requestCamera.getFace_rec_en());
                    mode2.getCameras().get(0).setCamera_distance(this.cameraSettings.getCamera_distance());
                    mode2.getCameras().get(0).setSpoof_level(this.cameraSettings.getSpoof_level());
                    mode2.getCameras().get(0).setPose_filter(this.cameraSettings.getPose_filter());
                    mode2.getCameras().get(0).setView_mode(this.requestCamera.getView_mode());
                    mode2.getCameras().get(0).setRotation(this.cameraSettings.getRotation());
                    ApiFunctions.Companion.setSettings(apiToken.access_token,mode2);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_STATUS:
                    ApiFunctions.Companion.getStatus(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_AGE_ONLY:
                    icuLastState = icuState;
                    ModeSet mode = new ModeSet();
                    mode.getCameras().get(0).setId_Index(0);
                    mode.getCameras().get(0).setFace_rec_en(false);
                    mode.getCameras().get(0).setCamera_distance(this.cameraSettings.getCamera_distance());
                    mode.getCameras().get(0).setSpoof_level(this.cameraSettings.getSpoof_level());
                    mode.getCameras().get(0).setPose_filter(this.cameraSettings.getPose_filter());
                    mode.getCameras().get(0).setRotation(this.cameraSettings.getRotation());
                    mode.getCameras().get(0).setView_mode("Biggest");
                    ApiFunctions.Companion.setSettings(apiToken.access_token,mode);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_FACE_REC:
                    icuLastState = icuState;
                    ModeSet mode3 = new ModeSet();
                    mode3.getCameras().get(0).setId_Index(0);
                    mode3.getCameras().get(0).setFace_rec_en(true);
                    mode3.getCameras().get(0).setCamera_distance(this.cameraSettings.getCamera_distance());
                    mode3.getCameras().get(0).setSpoof_level(this.cameraSettings.getSpoof_level());
                    mode3.getCameras().get(0).setPose_filter(this.cameraSettings.getPose_filter());
                    mode3.getCameras().get(0).setRotation(this.cameraSettings.getRotation());
                    mode3.getCameras().get(0).setView_mode("Biggest");
                    ApiFunctions.Companion.setSettings(apiToken.access_token,mode3);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_AGE_ID_VERIFY:
                    icuLastState = icuState;
                    ModeSet mode1 = new ModeSet();
                    mode1.getCameras().get(0).setId_Index(0);
                    mode1.getCameras().get(0).setFace_rec_en(true);
                    mode1.getCameras().get(0).setCamera_distance(this.cameraSettings.getCamera_distance());
                    mode1.getCameras().get(0).setSpoof_level(this.cameraSettings.getSpoof_level());
                    mode1.getCameras().get(0).setPose_filter(this.cameraSettings.getPose_filter());
                    mode1.getCameras().get(0).setRotation(this.cameraSettings.getRotation());
                    mode1.getCameras().get(0).setView_mode("OCR");
                    ApiFunctions.Companion.setSettings(apiToken.access_token,mode1);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_IDLE:
                    ApiFunctions.Companion.setSession(apiToken.access_token, "idle");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_STATUS:
                    ApiFunctions.Companion.getSession(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_FACE_CAPTURE:
                    ApiFunctions.Companion.setSession(apiToken.access_token, "face_capture");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_AGE_RESULT:
                    ApiFunctions.Companion.getSessionAgeResult(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_CARD_SCAN:
                    ApiFunctions.Companion.setSession(apiToken.access_token, "id_scan");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_SCAN_RESULT:
                    ApiFunctions.Companion.getSessionScanResult(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_STREAM_SETTINGS:
                    ApiFunctions.Companion.setStreamFaceBoxDisplay(apiToken.access_token,faceBoxDisplayValue);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_ENROLL_IMAGE:
                    ApiFunctions.Companion.enrollImage(apiToken.access_token,enrollImage);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_PURGE_FACES:
                    icuLastState = icuState;
                    String [] del = new String[1];
                    del[0] = "all";
                    FaceDelete faceDelete = new FaceDelete(del);
                    ApiFunctions.Companion.deleteFaces(apiToken.access_token,faceDelete);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_FACES_TO_UPDATE:
                    icuLastState = icuState;
                    ApiFunctions.Companion.updateFaceData(apiToken.access_token,this.updateFaceData);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_FACES_TO_DELETE:
                    icuLastState = icuState;
                    ApiFunctions.Companion.deleteFaces(apiToken.access_token,this.deleteFaceData);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
            }


            // loop delay
            try {
                Thread.sleep(POLL_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void NewEnroll(String image){
        enrollImage = image;
        newEnrollImage = true;
    }

    public void SetMode(ICURunMode mode){

        currentMode = mode;
        if(mode == ICURunMode.AGE_ONLY){
            newAgeOnlyMode = true;
        }else if(mode == ICURunMode.AGE_ID_VERIFY){
            newAgeVerifyMode = true;
        }else if(mode == ICURunMode.AGE_ID_IDLE){
            newSetSessionIdle = true;
        }else if(mode == ICURunMode.AGE_ID_SCAN){
            newSetCardScan = true;
        }else if(mode == ICURunMode.FACE_REC){
            newFaceRecMode = true;
        }else if(mode == ICURunMode.STREAM_FACE_BOX_ON){
            faceBoxDisplayValue = 1;
            newStreamFaceBox = true;
        }else if(mode == ICURunMode.STREAM_FACE_BOX_OFF){
            faceBoxDisplayValue = 0;
            newStreamFaceBox = true;
        }

    }

    public void UpdateFaces(UpdateFaceData updateFaceData){
        this.updateFaceData = updateFaceData;
        newUpdateFaceData = true;
    }



    @Override
    public void onToken(Token token) {

        apiToken = token;
        icuState = icuSavedState;
    }

    @Override
    public void onDeviceDetail(  DeviceDetail device) {
        this.icuDevice.setDeviceDetail(device);
        icuState = ICU_SM.SM_GET_SETTINGS;
    }

    @Override
    public void onStatus(  StatusResponse status) {

        if(!readySent && status.getDeviceState().equalsIgnoreCase("ready")){
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICUReady();
            }
            readySent = true;
        }

        if(validSettings){
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICULastFaceUpdate(status.getLastFaceUpdate());
            }
        }

        for(int i = 0; i < status.getDetections().size(); i++) {
            Detection d = status.getDetections().get(i);
            FaceSessionData faceSessionData = new FaceSessionData();
            faceSessionData.age = d.getAge();
            faceSessionData.image = d.getImage();
            faceSessionData.uid = d.getUid();
            faceSessionData.gender = d.getGender();

            if (!d.getUid().equals("none")) {
                faceSessionData.record_image = "db image found";
                for(LocalAPIListener l: icuThreadListeners.values()){
                    l.ICUUid(faceSessionData);
                }
            } else {
                faceSessionData.feature = d.getFeature();
                for(LocalAPIListener l: icuThreadListeners.values()){
                    l.ICUAge(faceSessionData);
                }
            }
        }
        if(readySent) {
            for (LocalAPIListener l : icuThreadListeners.values()) {
                l.ICULiveFrame(status.getFaceInFrame());
            }
        }


        icuState = ICU_SM.SM_STATUS;


    }

    @Override
    public void onGetSettings(  DeviceSettings settings) {
        this.icuDevice.setDeviceSettings(settings);

        // update global settings object with response
        if(settings.getCameras() != null) {
            this.requestCamera = settings.getCameras().get(0);
        }else{
            this.requestCamera = null;
        }

        if(!readySent){
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICUConnected(this.icuDevice);
            }
        }else{
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICUDeviceUpdate(this.icuDevice);
            }
        }

        if(!this.validSettings){
            icuState = ICU_SM.SM_PURGE_FACES;
            this.validSettings = true;
        }else {
            if (icuLastState == ICU_SM.SM_SET_SETTINGS ||  icuLastState == ICU_SM.SM_GET_DEVICE ||
                    icuLastState == ICU_SM.SM_SET_AGE_ONLY || icuLastState == ICU_SM.SM_SET_FACE_REC) {
                icuState = ICU_SM.SM_STATUS;
            } else {
                icuState = ICU_SM.SM_SET_SESSION_IDLE;
            }
        }
    }

    @Override
    public void onSetSettings() {

        if(icuLastState == ICU_SM.SM_SET_SETTINGS ||  icuLastState == ICU_SM.SM_SET_AGE_ONLY
                || icuLastState == ICU_SM.SM_SET_AGE_ID_VERIFY
                || icuLastState == ICU_SM.SM_SET_FACE_REC || icuLastState == ICU_SM.SM_PURGE_FACES){
            icuState = ICU_SM.SM_GET_SETTINGS;
        }

    }

    @Override
    public void onSetSession() {


        icuState = ICU_SM.SM_GET_SESSION_STATUS;

    }

    @Override
    public void onGetSession(  SessionResponse session) {

        if(session.getDevice_status().equals("ready")){
            if(session.getSession_mode().equals("idle") && session.getFaceInFrame() && currentMode != ICURunMode.AGE_ID_IDLE){
                icuState = ICU_SM.SM_SET_SESSION_FACE_CAPTURE;
            }else if(session.getSession_mode().equals("face_capture") && session.getSession_age_result()){
                icuState = ICU_SM.SM_GET_SESSION_AGE_RESULT;
            }else if(session.getSession_mode().equals("id_scan") && session.getSession_scan_result()){
                icuState = ICU_SM.SM_GET_SESSION_SCAN_RESULT;
            }else{
                 icuState = ICU_SM.SM_GET_SESSION_STATUS;
            }
        }else{
            icuState = ICU_SM.SM_GET_SESSION_STATUS;
        }

    }




    @Override
    public void onGetAgeResult(  SessionAgeResult session) {

        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUSessionAgeResult(session);
        }
        icuState = ICU_SM.SM_IDLE;

    }

    @Override
    public void onGetScanResult(  SessionScanResult session) {

        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUSessionScanResult(session);
        }
        icuState = ICU_SM.SM_IDLE;
    }

    @Override
    public void onSetStreamSettings() {
        icuState = ICU_SM.SM_STATUS;
    }


    @Override
    public void onFaceIDSuccess(  EnrollResponse enrollResponse) {

        icuState = ICU_SM.SM_STATUS;
        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUFaceIDSuccess(enrollResponse);
        }

    }

    @Override
    public void onFaceIDFail(  EnrollError enrollError) {
        icuState = ICU_SM.SM_STATUS;
        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUFaceIDError(enrollError);
        }
    }


    @Override
    public void onRequestFail(@NotNull ICUError icuError, @NotNull String message) {

        icuState = ICU_SM.SM_CONNECT;
        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUDisconnected(message);
        }
    }


    @Override
    public void onDeleteFaces() {


        if(icuLastState == ICU_SM.SM_PURGE_FACES) {
            icuState = ICU_SM.SM_SET_SETTINGS;
        }else{
            icuState = ICU_SM.SM_STATUS;
        }
        for(LocalAPIListener l: icuThreadListeners.values()){
            l.ICUFacesDeleted();
        }

    }


    @Override
    public void onFacesUpdated() {
        icuState = ICU_SM.SM_STATUS;
    }


}
