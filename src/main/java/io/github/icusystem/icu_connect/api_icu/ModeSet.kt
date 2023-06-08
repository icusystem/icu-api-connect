package io.github.icusystem.icu_connect.api_icu
class ModeSet {

    var Cameras:MutableList<ModeCamera>? = ArrayList()

    init {
        val cam1 = ModeCamera()
        Cameras?.add(cam1)
    }

}

