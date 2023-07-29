package io.github.icusystem.icu_connect;



import io.github.icusystem.icu_connect.api_icu.*;


import java.util.ArrayList;
import java.util.List;

public interface LocalAPIListener {


    default void ICUConnected(ICUDevice device){}
    default void ICUDeviceUpdate(ICUDevice device){}
    default void ICUDisconnected(String message){}
    default void ICUInitialising(){}
    default void ICUReady(){}
    default void ICULastFaceUpdate(Integer lastFaceIndex){}
    default void ICUAge(FaceSessionData data){}
    default void ICUUid(FaceSessionData data){}

    default void ICUSessionAgeResult(SessionAgeResult sessionAgeResult){}
    default void ICUSessionScanResult(SessionScanResult sessionScanResult){}

    default void ICULiveFrame(List<FaceDetect> frameCounts){}

    default void ICUFaceIDSuccess(ArrayList<EnrollItem> enrollResponse){}
    default void ICUFaceIDError(EnrollError enrollError){}

    default void ICUFacesDeleted(){}

}
