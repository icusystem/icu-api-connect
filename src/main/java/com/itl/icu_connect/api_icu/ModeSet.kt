package com.itl.icu_connect.api_icu

class ModeCamera{
    var Id_Index:Int = 0
    var Face_rec_en:Boolean = false
    var Spoof_level:Int = 0
    var Pose_filter:Boolean = false
    var Camera_distance:Int = 0
    var Rotation:Int = 0
    var View_mode:String? = null
}

class ModeSet {

    var Cameras:MutableList<ModeCamera>? = ArrayList()

    init {
        val cam1 = ModeCamera()
        Cameras?.add(cam1)
    }

}

