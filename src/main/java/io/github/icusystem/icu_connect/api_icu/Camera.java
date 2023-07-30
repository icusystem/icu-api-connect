package io.github.icusystem.icu_connect.api_icu;

/**
 * The Camera class represents a camera device in a system, which can be used for various imaging and facial recognition operations.
 * It contains several attributes related to the camera, such as enabled status, name, index ID, spoof level, rotation, resolution,
 * activity status, camera distance, pose filter, face recognition enablement, view mode, and ID verification capability.
 */
public class Camera {

    /**
     * Indicates whether the camera is enabled or disabled.
     */
    public boolean Enabled = false;

    /**
     * The name of the camera.
     */
    public String Name = null;

    /**
     * The index ID of the camera.
     */
    public int Id_Index = 0;

    /**
     * The spoof level associated with the camera. Spoof level refers to the degree of resistance to spoofing attacks.
     */
    public int Spoof_level = 0;

    /**
     * The rotation angle of the camera view in degrees.
     */
    public int Rotation = 0;

    /**
     * The resolution of the camera's imaging output.
     */
    public String Resolution = null;

    /**
     * Indicates whether the camera is currently active or inactive.
     */
    public boolean Active = false;

    /**
     * The distance of the camera from the subject being imaged, if applicable.
     */
    public int Camera_distance = 0;

    /**
     * Indicates whether the camera applies a pose filter for facial recognition operations.
     */
    public boolean Pose_filter = false;

    /**
     * Indicates whether face recognition is enabled on this camera.
     */
    public boolean Face_rec_en = false;

    /**
     * The view mode of the camera, which can be used for specific imaging or recognition configurations.
     */
    public String View_mode = null;

    /**
     * Indicates whether the camera is capable of performing ID verification.
     */
    public boolean Id_verify_capable = false;

    /**
     * Constructs a new Camera object with default values.
     * The attributes will be initialized with appropriate default values for their respective data types.
     */
    public Camera() {
        // The default values for boolean attributes are already set to 'false'.
        // The default values for numeric attributes are set to '0'.
        // The default value for the 'String' attribute is set to 'null'.
    }

    // Additional constructors, getters, and setters can be added here if needed.
}

