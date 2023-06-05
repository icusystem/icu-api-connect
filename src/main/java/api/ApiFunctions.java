package api;

import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Static call for ICU API functions
 */
public final class ApiFunctions {

    private static String BASE_URL = "";
    private static IAPIAccount iApiAccountListener;
    /**
     * Only one instance for this Retrofit
     */
    private static Retrofit retrofit = null;

    private static ApiService apiService = null;


    /**
     * @param listener the IAPIAccount listener interface
     */
    public static void setApiAccountListener(IAPIAccount listener){
        iApiAccountListener = listener;
    }

    public static void setBaseUrl(String baseUrl){
        BASE_URL = baseUrl;
    }



    /**
     * ICU API request to obtain the authorization token from
     * the ICU device
     */
    public static void getToken(String username, String password){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .client(getUnsafeOkHttpClient())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

            apiService = retrofit.create(ApiService.class);
        }



        HashMap<String,String> params = new HashMap<>();
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);

        Call<Token> call = apiService.getToken(params);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token apiResponse = response.body();
                    iApiAccountListener.onTokenSuccess(apiResponse);

                } else {
                    // Handle error response
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE,"http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });

    }


    /**
     * Get ICU device details from the connected ICU Device
     * @param token the api authorisation token value
     */
    public static void getDevice(String token){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }


        Call<DeviceDetail> call = apiService.getDevice("Bearer " + token);
        call.enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Call<DeviceDetail> call, Response<DeviceDetail> response) {
                if (response.isSuccessful()) {
                DeviceDetail apiResponse = response.body();
                iApiAccountListener.onDeviceSuccess(apiResponse);
                } else {
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE,"http response fail " + response.code());
                }
            }
            @Override
            public void onFailure(Call<DeviceDetail> call, Throwable throwable) {
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });



    }

    /**
     * Request to ICU Device API to delete listed or all stored face definitions
     * @param token the api authorization token value
     * @param faceDelete object of Array of strings listing all the face definition IDs
     *                   to be deleted
     */
    public static void setDeleteFaces(String token, FaceDelete faceDelete){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }


        Call<Void> call = apiService.setDeleteFaces("Bearer " + token,faceDelete);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    iApiAccountListener.onDeleteSuccess();
                } else {
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE,"http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION,throwable.getMessage());
            }
        });


    }


    /**
     * API request to get all the ICU device settings
     * @param token the api authorization token value
     */
    public static void getSettings(String token){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }


        Call<SettingsResponse> call = apiService.getSettings("Bearer " + token);
        call.enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                if (response.isSuccessful()) {
                    SettingsResponse apiResponse = response.body();
                    iApiAccountListener.onSettingsSuccess(apiResponse);
                } else {
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE,"http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable throwable) {
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION,throwable.getMessage());
            }
        });

    }

    /**
     * API request to get the current operation status of
     * the connected ICU device
     * @param token the api authorization token value
     */
    public static void getStatus(String token){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }


        Call<StatusResponse> call = apiService.getStatus("Bearer " + token);
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.isSuccessful()) {
                    StatusResponse apiResponse = response.body();
                    iApiAccountListener.onStatusSuccess(apiResponse);
                } else {
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE,"http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable throwable) {
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION,throwable.getMessage());
            }
        });

    }


    /**
     * Get the OkHttpClient with Self-signed SSL by-pass
     * @return OkHttpClient object
     */
    @NotNull
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + trustManagers);
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
