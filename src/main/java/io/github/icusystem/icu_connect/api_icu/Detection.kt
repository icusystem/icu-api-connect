package io.github.icusystem.icu_connect.api_icu

data class Detection(
    val Age:Int,
    val Camera:String,
    val Gender:String,
    val Mask:Boolean,
    val Uid:String,
    val FeatureUid:String,
    val Feature:String,
    val Image:String )