package io.github.icusystem.icu_connect.api_icu

data class SessionResponse(
    val Session_mode:String,
    val FaceInFrame:Boolean,
    val Session_age_result:Boolean,
    val Session_scan_result:Boolean,
    val Device_status:String
)
