package io.github.icusystem.icu_connect.api_icu;

import java.util.List;

public class StatusResponse {

    public String DeviceState;
    public int LastFaceUpdate;
    public int LastActionUpdate;
    public String SingleState;
    public List<FaceDetect> FaceInFrame;
    public List<Detection> Detections;

}


