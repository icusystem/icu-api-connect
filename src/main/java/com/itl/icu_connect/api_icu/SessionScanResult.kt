package com.itl.icu_connect.api_icu

data class SessionScanResult(
    val IDAge:Int,
    val IDFaceMatch:Boolean,
    val IDFaceImage:String,
    val IDScanStatus:String,
    var IDCompareImage:String
)
