package io.github.icusystem.icu_connect.api_icu;

public class FaceDetect {

    public int CameraIndex;
    public boolean Face;
    public boolean Spoof;
    public boolean SpoofTerminated;
    public int FaceCount;
    public int PersonCount;

    public String FaceReject;

    public FaceDetect(){
        FaceReject = "";
    }

}
