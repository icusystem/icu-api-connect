package com.itl.icu_connect.api_icu

data class UpdateData(var Uid:String, var Version:String, var Data:String, var Group:String, var GroupId:Int)

data class UpdateFaceData(var last_update_time:Long, var updates:List<UpdateData>)
