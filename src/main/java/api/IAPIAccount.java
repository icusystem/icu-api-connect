package api;

public interface IAPIAccount {

    void onRequestFail();
    void onTokenSuccess(Token token);
    void onDeviceSuccess(DeviceDetail deviceDetail);

    void onDeleteSuccess();

    void onSettingsSuccess(SettingsResponse settingsResponse);
    void onStatusSuccess(StatusResponse statusResponse);


}
