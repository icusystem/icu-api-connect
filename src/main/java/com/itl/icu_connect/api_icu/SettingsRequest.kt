package com.itl.icu_connect.api_icu


class CameraRequest{
     var Enabled:Boolean = false
     var Name:String = ""
     var Id_Index:Int = -1
     var Spoof_Level: Int = 0
     var Camera_distance:Int = 0
     var Pose_filter:Boolean = false
     var Rotation:Int = 0
     var Face_rec_enable:Boolean = false
     var View_mode:String = ""

}


class SettingsRequest {
     var DeviceName:String = ""
     var Cameras: MutableList<CameraRequest>? = ArrayList<CameraRequest>()

    constructor(){
        val cam = CameraRequest()
        Cameras?.add(cam)
    }




}




