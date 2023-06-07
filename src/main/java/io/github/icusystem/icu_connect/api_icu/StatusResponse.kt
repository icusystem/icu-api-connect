package io.github.icusystem.icu_connect.api_icu

data class FaceDetect(var CameraIndex:Int,var Face:Boolean, var Spoof: Boolean, var FaceCount:Int, var PersonCount:Int)

data class Detection(val Age:Int, val Camera:String, val Gender:String,val Mask:Boolean, val Uid:String, val FeatureUid:String, val Feature:String, val Image:String )
data class StatusResponse(
    val DeviceState:String,
    val LastFaceUpdate:Int,
    var LastActionUpdate:Int,
    var SingleState:String,
    var FaceInFrame:List<FaceDetect>,
    val Detections:List<Detection>
    )


