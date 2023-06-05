package api;

public interface IDeviceEvent {

    void onNewICUEvent(ICUEvent icuEvent, Object o);
    void onICUError(ICUError icuError, String message);

}
