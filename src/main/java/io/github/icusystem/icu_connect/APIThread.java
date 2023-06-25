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

    public APIThread(CameraSettings cameraSettings, String username, String password, Boolean showDebug) {

        this.icuThreadListeners = new HashMap<>();
        this.icuDevice = new ICUDevice();
        this.cameraSettings = cameraSettings;
        this.apiUserName = username;
        this.apiPassword = password;
        ApiFunctions.setShowDebug(showDebug);
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

        ApiFunctions.setOnAPIAccountListener(this);

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
                    ApiFunctions.getToken(this.apiUserName,this.apiPassword);
                    icuSavedState =  ICU_SM.SM_GET_DEVICE;
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_REFRESH_TOKEN:
                    ApiFunctions.getToken(this.apiUserName,this.apiPassword);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_DEVICE:
                    icuLastState = icuState;
                    ApiFunctions.getDevice(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SETTINGS:
                    ApiFunctions.getSettings(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SETTINGS:
                    // update with preference settings
                    icuLastState = icuState;
                    ModeSet mode2 = new ModeSet();
                    mode2.Cameras.get(0).Id_Index = 0;
                    mode2.Cameras.get(0).Face_rec_en = this.requestCamera.Face_rec_en;
                    mode2.Cameras.get(0).Camera_distance = this.cameraSettings.Camera_distance;
                    mode2.Cameras.get(0).Spoof_level = this.cameraSettings.Spoof_level;
                    mode2.Cameras.get(0).Pose_filter = this.cameraSettings.Pose_filter;
                    mode2.Cameras.get(0).View_mode  = this.requestCamera.View_mode;
                    mode2.Cameras.get(0).Rotation =  this.cameraSettings.Rotation;
                    ApiFunctions.setSettings(apiToken.access_token,mode2);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_STATUS:
                    ApiFunctions.getStatus(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_AGE_ONLY:
                    icuLastState = icuState;
                    ModeSet mode = new ModeSet();
                    mode.Cameras.get(0).Id_Index = 0;
                    mode.Cameras.get(0).Face_rec_en = false;
                    mode.Cameras.get(0).Camera_distance = this.cameraSettings.Camera_distance;
                    mode.Cameras.get(0).Spoof_level = this.cameraSettings.Spoof_level;
                    mode.Cameras.get(0).Pose_filter = this.cameraSettings.Pose_filter;
                    mode.Cameras.get(0).Rotation =  this.cameraSettings.Rotation;
                    mode.Cameras.get(0).View_mode = "Biggest";
                    ApiFunctions.setSettings(apiToken.access_token,mode);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_FACE_REC:
                    icuLastState = icuState;
                    ModeSet mode3 = new ModeSet();
                    mode3.Cameras.get(0).Id_Index = 0;
                    mode3.Cameras.get(0).Face_rec_en = true;
                    mode3.Cameras.get(0).Camera_distance = this.cameraSettings.Camera_distance;
                    mode3.Cameras.get(0).Spoof_level = this.cameraSettings.Spoof_level;
                    mode3.Cameras.get(0).Pose_filter = this.cameraSettings.Pose_filter;
                    mode3.Cameras.get(0).Rotation =  this.cameraSettings.Rotation;
                    mode3.Cameras.get(0).View_mode = "Biggest";
                    ApiFunctions.setSettings(apiToken.access_token,mode3);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_AGE_ID_VERIFY:
                    icuLastState = icuState;
                    ModeSet mode1 = new ModeSet();
                    mode1.Cameras.get(0).Id_Index = 0;
                    mode1.Cameras.get(0).Face_rec_en = true;
                    mode1.Cameras.get(0).Camera_distance = this.cameraSettings.Camera_distance;
                    mode1.Cameras.get(0).Spoof_level = this.cameraSettings.Spoof_level;
                    mode1.Cameras.get(0).Pose_filter = this.cameraSettings.Pose_filter;
                    mode1.Cameras.get(0).Rotation =  this.cameraSettings.Rotation;
                    mode1.Cameras.get(0).View_mode = "OCR";
                    ApiFunctions.setSettings(apiToken.access_token,mode1);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_IDLE:
                    ApiFunctions.setSession(apiToken.access_token, "idle");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_STATUS:
                    ApiFunctions.getSession(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_FACE_CAPTURE:
                    ApiFunctions.setSession(apiToken.access_token, "face_capture");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_AGE_RESULT:
                    ApiFunctions.getSessionAgeResult(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_SESSION_CARD_SCAN:
                    ApiFunctions.setSession(apiToken.access_token, "id_scan");
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_GET_SESSION_SCAN_RESULT:
                    ApiFunctions.getSessionScanResult(apiToken.access_token);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_STREAM_SETTINGS:
                    ApiFunctions.setStreamFaceBoxDisplay(apiToken.access_token,faceBoxDisplayValue);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_SET_ENROLL_IMAGE:
                    ApiFunctions.setEnrollImage(apiToken.access_token,enrollImage);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_PURGE_FACES:
                    icuLastState = icuState;
                    String [] del = new String[1];
                    del[0] = "all";
                    FaceDelete faceDelete = new FaceDelete(del);
                    ApiFunctions.setDeleteFaces(apiToken.access_token,faceDelete);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_FACES_TO_UPDATE:
                    icuLastState = icuState;
                    ApiFunctions.setFaceData(apiToken.access_token,this.updateFaceData);
                    icuState = ICU_SM.SM_RESPONSE;
                    break;
                case SM_FACES_TO_DELETE:
                    icuLastState = icuState;
                    ApiFunctions.setDeleteFaces(apiToken.access_token,this.deleteFaceData);
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
        this.icuDevice.deviceDetail = device;
        icuState = ICU_SM.SM_GET_SETTINGS;
    }

    @Override
    public void onStatus(  StatusResponse status) {

        if(!readySent && status.DeviceState.equalsIgnoreCase("ready")){
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICUReady();
            }
            readySent = true;
        }

        if(validSettings){
            for(LocalAPIListener l: icuThreadListeners.values()){
                l.ICULastFaceUpdate(status.LastFaceUpdate);
            }
        }

        for(int i = 0; i < status.Detections.size(); i++) {
            Detection d = status.Detections.get(i);
            FaceSessionData faceSessionData = new FaceSessionData();
            faceSessionData.age = d.Age;
            faceSessionData.image = d.Image;
            faceSessionData.uid = d.Uid;
            faceSessionData.gender = d.Gender;

            if (!d.Uid.equals("none")) {
                faceSessionData.record_image = "db image found";
                for(LocalAPIListener l: icuThreadListeners.values()){
                    l.ICUUid(faceSessionData);
                }
            } else {
                faceSessionData.feature = d.Feature;
                for(LocalAPIListener l: icuThreadListeners.values()){
                    l.ICUAge(faceSessionData);
                }
            }
        }
        if(readySent) {
            for (LocalAPIListener l : icuThreadListeners.values()) {
                l.ICULiveFrame(status.FaceInFrame);
            }
        }


        icuState = ICU_SM.SM_STATUS;


    }

    @Override
    public void onGetSettings(  DeviceSettings settings) {
        this.icuDevice.deviceSettings = settings;

        // update global settings object with response
        if(settings.Cameras != null) {
            this.requestCamera = settings.Cameras.get(0);
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

        if(session.Device_status.equals("ready")){
            if(session.Session_mode.equals("idle") && session.FaceInFrame && currentMode != ICURunMode.AGE_ID_IDLE){
                icuState = ICU_SM.SM_SET_SESSION_FACE_CAPTURE;
            }else if(session.Session_mode.equals("face_capture") && session.Session_age_result){
                icuState = ICU_SM.SM_GET_SESSION_AGE_RESULT;
            }else if(session.Session_mode.equals("id_scan") && session.Session_scan_result){
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
