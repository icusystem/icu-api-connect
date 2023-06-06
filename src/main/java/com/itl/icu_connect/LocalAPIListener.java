package com.itl.icu_connect;

import com.itl.icu_connect.api_icu.DeviceDetail;
import com.itl.icu_connect.api_icu.EnrollError;
import com.itl.icu_connect.api_icu.EnrollResponse;
import com.itl.icu_connect.api_icu.FaceDetect;
import com.itl.icu_connect.api_icu.FaceSessionData;
import com.itl.icu_connect.api_icu.ICUDevice;
import com.itl.icu_connect.api_icu.SessionAgeResult;
import com.itl.icu_connect.api_icu.SessionScanResult;
import com.itl.icu_connect.api_icu.StatusResponse;

import java.util.List;

public interface LocalAPIListener {


    default void ICUConnected(ICUDevice device){}
    default void ICUDeviceUpdate(ICUDevice device){}
    default void ICUDisconnected(String message){}
    default void ICUReady(){}
    default void ICULastFaceUpdate(Integer lastFaceIndex){}
    default void ICUAge(FaceSessionData data){}
    default void ICUUid(FaceSessionData data){}

    default void ICUSessionAgeResult(SessionAgeResult sessionAgeResult){}
    default void ICUSessionScanResult(SessionScanResult sessionScanResult){}

    default void ICULiveFrame(List<FaceDetect> frameCounts){}

    default void ICUFaceIDSuccess(EnrollResponse enrollResponse){}
    default void ICUFaceIDError(EnrollError enrollError){}

    default void ICUFacesDeleted(){}

}
