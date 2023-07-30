# icu-api-connect
A connection and event library for the Innovative Technology Ltd range of ICU:tm: of age estimation and face recognition products.

## ICU Events

| Name                                          | Description                                |
|-----------------------------------------------|--------------------------------------------|
| [ICUDisconnected](#icudisconnected)           | The ICU device was disconnected            |
| [ICUConnected](#icuconnected)                 | An ICU device connection has been detected |
| [ICUDeviceUpdate](#icudeviceupdate)           | Connected ICU device information           |
| [ICUInitialising](#icuinitialising)           | The ICU device is starting up              |
| [ICUReady](#icuready)                         | The ICU Device is in-service               |
| [ICUAge](#icuage)                             | Face age information has been detected     |
| [ICUUid](#icuuid)                             | Face recognition has been detected         |
| [ICULiveFrame](#iculiveframe)                 | Live frame information                     |
| [ICULastFaceUpdate](#liculastfaceupdate)      | The last stored face update index          |
| [ICUFaceIDSuccess](#icufaceidsuccess)         | Face image data is ready                   |
| [ICUFaceIDError](#icufaceiderror)             | Face image data is not valid               |
| [ICUFacesDeleted](#icufacesdeleted)           | Requested face data was deleted            |
| [ICUSessionAgeResult](#icusessionageresult)   | ID session Age result is ready             |
| [ICUSessionScanResult](#icusessionscanresult) | ID Session card scan result is ready       |

# Event detail
## *ICUDisconnected*
### Fires 
On ICU device connection error
### Parameters
None

---
## *ICUConnected*
### Fires
An ICU connection is detected and communication is initiated
### Parameters
None

---
## *ICUDeviceUpdate*
### Fires
Connected ICU device information is ready
### Parameters
An [ICUDevice](#icudevice) object

---
## *ICUInitialising*
### Fires
The ICU device is starting up and loading data.
### Parameters
None
---
## *ICUReady*
### Fires
The ICU device has finished loading and is ready for operation
### Parameters
None

---
## *ICUAge*
### Fires
A detected face age only estimate is ready
### Parameters
A FaceSessionData object

---
## *ICUUid*
### Fires
A detected face has been matched and information is ready
### Parameters
A FaceSessionData object

---
## *ICULiveFrame*
### Fires
Live frame information is available
### Parameters
A List of FaceDetect objects

---
## *ICULastFaceUpdate*
### Fires
The last stored index number of database stored face data.
This can be an Indexed Primary Key ID or a serialised TimeStamp.
### Parameters
Integer number

---
## *ICUFaceIDSuccess*
### Fires
A face image sent to the ICU Device has been processed and added to the internal data
for recognition (Enrolled). The face data is available here for external database storage.
### Parameters
An ArrayList of EnrollItem objects giving face enroll data for the image.

---
## *ICUFaceIDError*
### Fires
A face image sent to the ICU Device has been processed but conferencing for face recognition 
was not successful. No face data was stored internally.
### Parameters
An EnrollError object giving information about the failure.

---
## *ICUFacesDeleted*
### Fires
A request to delete faces from the ICU device internal storage was successful.
### Parameters
None

---
## *ICUSessionAgeResult*
### Fires
A request to run an ID Card Age detection session has given an Age result.
### Parameters
A SessionAgeResult object

---
## *ICUSessionScanResult*
### Fires
A request to run an ID Card scan verification session has given a scan result
### Parameters
A SessionScanResult object.

---
# Run Mode requests
 The user can request operational modes using the *SetRunMode* command with a request type parameter
### ICU Request Mode Parameter

| Name                    | Description                                                                                               |
|-------------------------|-----------------------------------------------------------------------------------------------------------|
| **NONE**                | No action                                                                                                 |
| **AGE_ONLY**            | The device will perform age only (AAE enabled) estimations                                                |
| **FACE_REC**            | The device will perform face recognition functions (AAE disabled)                                         |
| **AGE_ID_VERITY**       | The device will start an ID Card match session to estimate the age of subject                             |
| **AGE_ID_SCAN**         | The device will start an ID Card scan using the age data obtained above to match against a photo ID image |
| **AGE_ID_IDLE**         | The device will enter an idle mode between ID Scan session. (No age or card scans carried out)            |
| **STREAM_FACE_BOX_OFF** | The user can disable detected 'face boxes' drawn on the http video stream                                 |
| **STREAM_FACE_BOX_ON**  | The user can enable detected 'face boxes' drawn on the http video stream                                  |

# Simple Example Implementation
A basic how-to example
## Java
Main.java
```
import io.github.icusystem.icu_connect.Connect;
import io.github.icusystem.icu_connect.api_icu.CameraSettings;
import io.github.icusystem.icu_connect.api_icu.ICURunMode;

public class Main {

    public static void main(String[] args) {

        /* The start-up camera settings */
        CameraSettings cameraSettings = new CameraSettings();
        cameraSettings.Spoof_level = 0;
        cameraSettings.Camera_distance = 3;
        cameraSettings.Rotation = 0;
        /* Create a Connect object, passing settings and ICU Device API username and password */
        Connect connect = new Connect(cameraSettings,"apiuser","apipassword");
        /* Create an event listener object */
        ApiEvents apiEvents = new ApiEvents();
        /* Start the Connect, passing the event listener object */
        connect.Start("Main",apiEvents);
        /* Set the operation mode - AGE_ONLY in this example */
        connect.SetMode(ICURunMode.AGE_ONLY);

        
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    
}
```
ApiEvents.java
```
import io.github.icusystem.icu_connect.LocalAPIListener;
import io.github.icusystem.icu_connect.api_icu.FaceSessionData;
import io.github.icusystem.icu_connect.api_icu.ICUDevice;

public class ApiEvents implements LocalAPIListener {


    @Override
    public void ICUConnected(ICUDevice icuDevice){

        System.out.println("connected " + icuDevice.deviceDetail.DeviceName);


    }

    @Override
    public void ICUDisconnected(String message){

        System.out.println("dis-connected " + message);


    }


    @Override
    public void ICUInitialising(){

        System.out.println("initialising...");


    }


    @Override
    public void ICUReady(){

        System.out.println("Ready");

    }


    @Override
    public void ICUAge(FaceSessionData data){

        System.out.println("Age  " + data.age);

    }


}
```



