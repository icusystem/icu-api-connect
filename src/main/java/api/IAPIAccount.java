package api;

public interface IAPIAccount {

    void onRequestFail(ICUError icuError, String message);
    void onTokenSuccess(Token token);
    void onDeviceSuccess(DeviceDetail deviceDetail);

    void onDeleteSuccess();

    void onSettingsSuccess(SettingsResponse settingsResponse);
    void onStatusSuccess(StatusResponse statusResponse);


}
