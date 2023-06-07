package io.github.icusystem.icu_connect.api_icu;

public interface IAPIAccount {

        fun onToken(token: Token)
        fun onDeviceDetail(device:DeviceDetail)
        fun onStatus(status: StatusResponse)
        fun onGetSettings(settings: DeviceSettings)
        fun onSetSettings()
        fun onSetSession()
        fun onSetStreamSettings()
        fun onGetSession(session:SessionResponse)
        fun onGetAgeResult(session:SessionAgeResult)
        fun onGetScanResult(session:SessionScanResult)
        fun onFaceIDSuccess(enrollResponse: EnrollResponse)
        fun onFaceIDFail(enrollError: EnrollError)
        fun onDeleteFaces()
        fun onFacesUpdated()
        fun onRequestFail(icuError: ICUError, message:String)




}
