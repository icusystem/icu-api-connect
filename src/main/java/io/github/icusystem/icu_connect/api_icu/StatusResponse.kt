package io.github.icusystem.icu_connect.api_icu
data class StatusResponse(
    val DeviceState:String,
    val LastFaceUpdate:Int,
    var LastActionUpdate:Int,
    var SingleState:String,
    var FaceInFrame:List<FaceDetect>,
    val Detections:List<Detection>
    )


