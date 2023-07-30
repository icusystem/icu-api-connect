package io.github.icusystem.icu_connect.api_icu;

/**
 * The EnrollItem class represents an enrolled item in a system, typically associated with facial recognition data.
 * It contains various attributes related to the enrolled item, such as status, image, face ID, user ID, and version.
 */
public class EnrollItem {

    /**
     * The status of the enrolled item.
     */
    public String status;

    /**
     * The image associated with the enrolled item.
     */
    public String image;

    /**
     * The unique face ID of the enrolled item.
     */
    public String faceid;

    /**
     * The unique user ID associated with the enrolled item.
     */
    public String uid;

    /**
     * The version information of the enrolled item.
     */
    public String version;

    /**
     * Constructs a new EnrollItem object with default values.
     * The attributes will be initialized to empty strings ("").
     */
    public EnrollItem() {
        status = "";
        image = "";
        faceid = "";
        uid = "";
        version = "";
    }

    // Additional constructors, getters, and setters can be added here if needed.
}

