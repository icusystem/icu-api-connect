package io.github.icusystem.icu_connect.api_icu;


import java.util.ArrayList;
import java.util.List;

public class SettingsRequest {

    public String DeviceName;
    public Boolean ReactiveLED;
    public List<CameraRequest> Cameras = new ArrayList<>();

    public SettingsRequest() {

        DeviceName = "";
        ReactiveLED = true;

        CameraRequest cam = new CameraRequest();
        if (Cameras != null) {
            Cameras.add(cam);
        }
    }

}





