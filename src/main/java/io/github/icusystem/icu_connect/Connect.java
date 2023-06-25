package io.github.icusystem.icu_connect;


import io.github.icusystem.icu_connect.api_icu.CameraSettings;
import io.github.icusystem.icu_connect.api_icu.FaceDelete;
import io.github.icusystem.icu_connect.api_icu.ICURunMode;
import io.github.icusystem.icu_connect.api_icu.UpdateFaceData;


/**
 * Connect
 * The connection class to the ICU API
 * Instantiate this class with connection parameters and
 * Listen for ICU events.
 * Set runtime updates and running modes.
 */
public class Connect{

    private APIThread icuThread;
    private CameraSettings cameraSettings;

    private String username;
    private String password;

    /**
     * Connect - Constructor
     * @param apiUsername - the ICU Device api username
     * @param apiPassword - the ICU Device api password
     */
    public Connect(String apiUsername, String apiPassword){
        cameraSettings = new CameraSettings();
        this.username = apiUsername;
        this.password = apiPassword;
    }

    /**
     * Connect - Overidden constructor
     * @param cameraSettings - a CameraSettings object containing optional start-up settings
     * @param apiUsername - the ICU Device api username
     * @param apiPassword - the ICU Device api password
     */
    public Connect(CameraSettings cameraSettings,String apiUsername, String apiPassword){
        this.cameraSettings = cameraSettings;
        this.username = apiUsername;
        this.password = apiPassword;
    }


    /**
     * Start the ICU API host system
     * @param tag  - a string to identify the instantiating class
     * @param localAPIListener - API event listener interface
     */
    public void Start(String tag, LocalAPIListener localAPIListener)
    {
        icuThread = new APIThread(this.cameraSettings,this.username,this.password);
        icuThread.setLocalAPIListener(tag,localAPIListener);
        icuThread.start();

    }

    /**
     * Start the ICU API host system - overridden
     * @param tag  - a string to identify the instantiating class
     * @param localAPIListener - API event listener interface
     * @param showDebug - print debug log to console    *
     */
    public void Start(String tag, LocalAPIListener localAPIListener, Boolean showDebug)
    {
        icuThread = new APIThread(this.cameraSettings,this.username,this.password,showDebug);
        icuThread.setLocalAPIListener(tag,localAPIListener);
        icuThread.start();

    }


    /**
     * SetListener - add a listener to
     * @param tag - unique string (example class name) of the class that impliments the LocalAPIListener
     * @param listener - API event listener interface
     */
    public void SetListener(String tag, LocalAPIListener listener){ icuThread.setLocalAPIListener(tag,listener);}

    /**
     * RemoveListener - removes the listener, for example when a class the inplements the
     * interface is destroyed
     * @param tag
     */
    public void RemoveListener(String tag){icuThread.removeLocalApiListener(tag);}

    /**
     * UpdateSettings - pass in a CameraSettings object with the required ICU settings to update
     * @param settings - the CameraSettings object
     */
    public void UpdateSettings(CameraSettings settings){icuThread.updateSettings(settings); }

    /**
     * Stop() stops the ICU API host from running
     */
    public void Stop(){
        icuThread.close();
    }

    /**
     * SetMode - change the running mode of the API host
     * @param mode - the mode to change to
     */
    public void SetMode(ICURunMode mode){
        icuThread.SetMode(mode);
    }

    /**
     * NewEnroll - Attempt to create a 'faceid' definition of a JPG image
     * @param image the base64 encoded string of the image
     */
    public void NewEnroll(String image){
        icuThread.NewEnroll(image);
    }

    /**
     * UpdateFaces - add the faceid definitions to the ICU device recognition data
     * @param updateFaceData - the list of faceid definitions to add
     */
    public void UpdateFaces(UpdateFaceData updateFaceData){ icuThread.UpdateFaces(updateFaceData); }

    /**
     * DeleteFaceData - delete the faceid definitions for the ICU device recognition data
     * @param deleteFaceData - the list of face uids to delete
     */
    public void DeleteFaces(FaceDelete deleteFaceData){
        icuThread.deleteFaces(deleteFaceData);
    }

}
