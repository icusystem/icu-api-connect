package io.github.icusystem.icu_connect;


import io.github.icusystem.icu_connect.api_icu.CameraSettings;
import io.github.icusystem.icu_connect.api_icu.FaceDelete;
import io.github.icusystem.icu_connect.api_icu.ICURunMode;
import io.github.icusystem.icu_connect.api_icu.UpdateFaceData;

public class Connect{

    private APIThread icuThread;
    private CameraSettings cameraSettings;

    private String username;
    private String password;

    public Connect(String apiUsername, String apiPassword){
        cameraSettings = new CameraSettings();
        this.username = apiUsername;
        this.password = apiPassword;
    }

    public Connect(CameraSettings cameraSettings,String apiUsername, String apiPassword){
        this.cameraSettings = cameraSettings;
        this.username = apiUsername;
        this.password = apiPassword;
    }



    public void Start(String tag, LocalAPIListener localAPIListener)
    {
        icuThread = new APIThread(this.cameraSettings,this.username,this.password);
        icuThread.setLocalAPIListener(tag,localAPIListener);
        icuThread.start();

    }

    public void SetListener(String tag, LocalAPIListener listener){ icuThread.setLocalAPIListener(tag,listener);}
    public void RemoveListener(String tag){icuThread.removeLocalApiListener(tag);}

    public void UpdateSettings(CameraSettings settings){icuThread.updateSettings(settings); }

    public void Stop(){
        icuThread.close();
    }

    public void SetMode(ICURunMode mode){
        icuThread.SetMode(mode);
    }

    public void NewEnroll(String image){
        icuThread.NewEnroll(image);
    }

    public void UpdateFaces(UpdateFaceData updateFaceData){ icuThread.UpdateFaces(updateFaceData); }

    public void DeleteFaces(FaceDelete deleteFaceData){
        icuThread.deleteFaces(deleteFaceData);
    }

}
