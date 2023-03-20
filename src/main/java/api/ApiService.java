package api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;

/**
 * An interface for the ApiService definition
 */
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
     * @return DeviceDetail object
     */
    @GET("/api/v1_0/device")
    Call<DeviceDetail> getDevice(@Header("Authorization") String token);

    /**
     * @param token the authorization token value
     * @param faceDelete the Array of strings object with ID deletion values
     * @return void
     */
    @POST("/api/v1_0/imagedelete")
    Call<Void> setDeleteFaces(@Header("Authorization") String token, @Body FaceDelete faceDelete);

    /**
     * @param token the authorization token value
     * @return Settings response object
     */
    @GET("/api/v1_0/settings")
    Call<SettingsResponse> getSettings(@Header("Authorization") String token);

    /**
     * @param token the authorization token value
     * @return ICU device StatusResponse object
     */
    @GET("/api/v1_0/status")
    Call<StatusResponse> getStatus(@Header("Authorization") String token);


}
