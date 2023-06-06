package com.itl.icu_connect;

import com.itl.icu_connect.api_icu.ICUDevice;

class IcuListener implements LocalAPIListener {


    @Override
    public void ICUConnected(ICUDevice device){
        System.out.println(device.getDeviceDetail().getDeviceName());
    }

    @Override
    public void ICUReady(){

        System.out.println("Ready");

    }




}