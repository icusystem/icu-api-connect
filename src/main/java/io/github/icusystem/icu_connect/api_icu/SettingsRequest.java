package io.github.icusystem.icu_connect.api_icu;


import java.util.ArrayList;
import java.util.List;

public class SettingsRequest {

    public String DeviceName = "";
    public List<CameraRequest> Cameras = new ArrayList<>();

    public SettingsRequest() {
        CameraRequest cam = new CameraRequest();
        if (Cameras != null) {
            Cameras.add(cam);
        }
    }

}





