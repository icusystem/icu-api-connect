package io.github.icusystem.icu_connect.api_icu;

/**
 * The Detection class represents a detected entity or object in a system, typically associated with facial detection or recognition results.
 * It contains several attributes related to the detected entity, such as age, camera name, gender, tracking number, mask presence,
 * unique user ID (UID), unique feature UID, facial feature description, and associated image.
 */
public class Detection {

    /**
     * The estimated age of the detected entity.
     */
    public int Age;

    /**
     * The name or identifier of the camera that captured the detection.
     */
    public String Camera;

    /**
     * The estimated gender of the detected entity (e.g., "Male," "Female," or "Unknown").
     */
    public String Gender;

    /**
     * The tracking number associated with the detection to uniquely identify the entity across frames or detections.
     */
    public int TrackingNumber;

    /**
     * Indicates whether the detected entity is wearing a mask or not.
     */
    public boolean Mask;

    /**
     * The unique user ID (UID) associated with the detected entity, if applicable.
     */
    public String Uid;

    /**
     * The unique feature UID associated with the detected entity's facial features, if applicable.
     */
    public String FeatureUid;

    /**
     * The description or information about the detected facial features of the entity.
     */
    public String Feature;

    /**
     * The image associated with the detected entity, typically the facial image.
     */
    public String Image;

    /**
     * Constructs a new Detection object with default values.
     * The attributes will be initialized with appropriate default values for their respective data types.
     */
    public Detection() {
        // The default value for numeric attributes (e.g., 'int') is set to '0'.
        // The default value for the 'boolean' attribute is set to 'false'.
        // The default value for the 'String' attribute is set to 'null'.
    }

    // Additional constructors, getters, and setters can be added here if needed.
}
