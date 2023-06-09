package io.github.icusystem.icu_connect.api_icu;

import java.util.ArrayList;

public interface IAPIAccount {

    void onToken(Token token);
    void onDeviceDetail(DeviceDetail device);
    void onStatus(StatusResponse status);
    void onGetSettings(DeviceSettings settings);
    void onSetSettings();
    void onSetSession();
    void onSetStreamSettings();
    void onGetSession(SessionResponse session);
    void onGetAgeResult(SessionAgeResult session);
    void onGetScanResult(SessionScanResult session);
    void onFaceIDSuccess(ArrayList<EnrollItem> enrollResponse);
    void onFaceIDFail(EnrollError enrollError);
    void onDeleteFaces();
    void onFacesUpdated();
    void onRequestFail(ICUError icuError,String  message);

    void onStatusFail(ICUError icuError,String  message);


}
