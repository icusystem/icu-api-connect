package io.github.icusystem.icu_connect;

import io.github.icusystem.icu_connect.api_icu.DeviceDetail;
import io.github.icusystem.icu_connect.api_icu.EnrollError;
import io.github.icusystem.icu_connect.api_icu.EnrollResponse;
import io.github.icusystem.icu_connect.api_icu.FaceDetect;
import io.github.icusystem.icu_connect.api_icu.FaceSessionData;
import io.github.icusystem.icu_connect.api_icu.ICUDevice;
import io.github.icusystem.icu_connect.api_icu.SessionAgeResult;
import io.github.icusystem.icu_connect.api_icu.SessionScanResult;
import io.github.icusystem.icu_connect.api_icu.StatusResponse;

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
