package io.github.icusystem.icu_connect.api_icu;
public class ModeCamera {

    public int Id_Index;
    public boolean Face_rec_en;
    public int Spoof_level;
    public  Boolean Spoof_terminate;
    public boolean Pose_filter;
    public int Camera_distance;
    public int Rotation;
    public String View_mode;

    public ModeCamera(){
        View_mode = "Biggest";
    }

}
