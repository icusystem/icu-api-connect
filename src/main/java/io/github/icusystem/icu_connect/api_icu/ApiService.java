package io.github.icusystem.icu_connect.api_icu;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.HashMap;

public interface ApiService {

    /**
     * @param params hashmap of API authentication parameters
     * @return Token object
     */
    @FormUrlEncoded
    @POST("/Token")
    Call<Token> getToken(@FieldMap HashMap<String, String> params);

    /**
     * @param token the authorization token value
     * @return ICU device StatusResponse object
     */
    @GET("/api/v1_0/status")
    Call<StatusResponse> getStatus(@Header("Authorization") String token);

    /**
     * @param token the authorization token value
     * @param image the EnrollRequest object with image data
     * @return EnrollResponse object
     */
    @POST("/api/v1_0/image")
    Call<ArrayList<EnrollItem>> getImageUID(@Header("Authorization") String token, @Body EnrollRequest image);

    @POST("/api/v1_0/imageupdate")
    Call<Void> setImageUpdate(@Header("Authorization") String token, @Body UpdateFaceData image);

    @POST("/api/v1_0/imagedelete")
    Call<Void> setImageDelete(@Header("Authorization") String token, @Body FaceDelete image);

    @GET("/api/v1_0/settings")
    Call<DeviceSettings> getSettings(@Header("Authorization") String token);

    @GET("/api/v1_0/device")
    Call<DeviceDetail> getDevice(@Header("Authorization") String token);

    @POST("/api/v1_0/settings")
    Call<Void> setSettings(@Header("Authorization") String token, @Body SettingsRequest settings);

    @POST("/api/v1_0/settings")
    Call<Void> setSettings(@Header("Authorization") String token, @Body ModeSet settings);

    @POST("/api/v1_0/session")
    Call<Void> setSession(@Header("Authorization") String token, @Body SetSession session);

    @GET("/api/v1_0/session")
    Call<SessionResponse> getSession(@Header("Authorization") String token);

    @GET("/api/v1_0/session_age_result")
    Call<SessionAgeResult> getSessionAgeResult(@Header("Authorization") String token);

    @GET("/api/v1_0/session_scan_result")
    Call<SessionScanResult> getSessionScanResult(@Header("Authorization") String token);

    @POST("/api/v1_0/streamsettings")
    Call<Void> setStreamSettings(@Header("Authorization") String token, @Body StreamSetFaceBox stream);
}

