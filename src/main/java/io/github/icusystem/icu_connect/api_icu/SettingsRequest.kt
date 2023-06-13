package io.github.icusystem.icu_connect.api_icu




class SettingsRequest {
     var DeviceName:String = ""
     var Cameras: MutableList<CameraRequest>? = ArrayList<CameraRequest>()

    constructor(){
        val cam = CameraRequest()
        Cameras?.add(cam)
    }




}




