package com.itl.icu_connect.api_icu

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    /**
     * @param params hashmap of API authentication parameters
     * @return Token object
     */
    @FormUrlEncoded
    @POST("/Token")
    fun getToken(@FieldMap params: HashMap<String, String>): Call<Token>

    /**
     * @param token the authorization token value
     * @return ICU device StatusResponse object
     */
    @GET("/api/v1_0/status")
    fun getStatus(@Header("Authorization") token: String): Call<StatusResponse>

    /**
     * @param token the authorization token value
     * @param faceDelete the Array of strings object with ID deletion values
     * @return void
     */
    @POST("/api/v1_0/imagedelete")
    fun getImageUID(@Header("Authorization") token: String?, @Body image: EnrollRequest): Call<EnrollResponse>


    @POST("/api/v1_0/imageupdate")
    fun setImageUpdate(@Header("Authorization") token: String, @Body image: UpdateFaceData): Call<Void>


    @POST("/api/v1_0/imagedelete")
    fun setImageDelete(@Header("Authorization") token: String, @Body image: FaceDelete): Call<Void>


    @GET("/api/v1_0/settings")
    fun getSettings(@Header("Authorization") token: String): Call<DeviceSettings>

    @GET("/api/v1_0/device")
    fun getDevice(@Header("Authorization") token: String): Call<DeviceDetail>

    @POST("/api/v1_0/settings")
    fun setSettings(@Header("Authorization") token: String, @Body settings: SettingsRequest): Call<Void>

    @POST("/api/v1_0/settings")
    fun setSettings(@Header("Authorization") token: String, @Body settings: ModeSet): Call<Void>

    @POST("/api/v1_0/session")
    fun setSession(@Header("Authorization") token: String, @Body session: SetSession): Call<Void>

    @GET("/api/v1_0/session")
    fun getSession(@Header("Authorization") token: String): Call<SessionResponse>

    @GET("/api/v1_0/session_age_result")
    fun getSessionAgeResult(@Header("Authorization") token: String): Call<SessionAgeResult>

    @GET("/api/v1_0/session_scan_result")
    fun getSessionScanResult(@Header("Authorization") token: String): Call<SessionScanResult>

    @POST("/api/v1_0/streamsettings")
    fun setStreamSettings(@Header("Authorization") token: String, @Body stream: StreamSetFaceBox): Call<Void>


}