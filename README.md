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



