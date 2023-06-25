package io.github.icusystem.icu_connect.api_icu;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;



public class ApiFunctions {
    private static IAPIAccount iApiAccountListener;
    private static ApiService apiService;
    private static Retrofit retrofit;
    private static Boolean showDebug = false;
    private static final String TAG = "ApiFunctions";
    public static void setOnAPIAccountListener(IAPIAccount listener) {
        iApiAccountListener = listener;
    }

    private static String getBaseURL() {
        return "https://192.168.137.8:44345";
    }

    private static void getRetrofitInstance() {
        // Create Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static void setShowDebug(Boolean debug){
        showDebug = debug;
    }

    public static void getToken(String username, String password) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);

        debugShowRequest("getToken", params);

        Call<Token> call = apiService.getToken(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Token> call, @NotNull Response<Token> response) {
                if (response.isSuccessful()) {
                    Token apiResponse = response.body();
                    debugShowResponse("getToken", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onToken(apiResponse);
                    }
                } else {
                    // Handle error response
                    debugShowResponse("getToken", new ResponseError(response.code(), "HTTP response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }


            @Override
            public void onFailure(@NotNull Call<Token> call, @NotNull Throwable throwable) {
                debugShowResponse("getToken", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }


    public static void getStatus(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("getStatus", null);

        Call<StatusResponse> call = apiService.getStatus("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<StatusResponse> call, @NotNull Response<StatusResponse> response) {
                if (response.isSuccessful()) {
                    StatusResponse apiResponse = response.body();
                    debugShowResponse("getStatus", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onStatus(apiResponse);
                    }
                } else {
                    debugShowResponse("getStatus", new ResponseError(response.code(), "HTTP response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<StatusResponse> call, @NotNull Throwable throwable) {
                debugShowResponse("getStatus", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setEnrollImage(String token, String imageB64) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("setEnrollImage", null);

        EnrollRequest enroll = new EnrollRequest(imageB64);
        Call<EnrollResponse> call = apiService.getImageUID("Bearer " + token, enroll);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<EnrollResponse> call, @NotNull Response<EnrollResponse> response) {
                if (response.isSuccessful()) {
                    EnrollResponse apiResponse = response.body();
                    debugShowResponse("setEnrollImage", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onFaceIDSuccess(apiResponse);
                    }
                } else {
                    debugShowResponse("setEnrollImage", new ResponseError(response.code(), "HTTP response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<EnrollResponse> call, @NotNull Throwable throwable) {
                debugShowResponse("setEnrollImage", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setFaceData(String token, UpdateFaceData face) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("setFaceData", face);
        Call<Void> call = apiService.setImageUpdate("Bearer " + token, face);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setFaceData", null);
                    iApiAccountListener.onFacesUpdated();
                } else {
                    debugShowResponse("setFaceData", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setFaceData", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setDeleteFaces(String token, FaceDelete faces) {
        if (retrofit == null) {
            getRetrofitInstance();
        }
        debugShowRequest("setDeleteFaces", faces);
        Call<Void> call = apiService.setImageDelete("Bearer " + token, faces);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setDeleteFaces", null);
                    iApiAccountListener.onDeleteFaces();
                } else {
                    debugShowResponse("setDeleteFaces", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setDeleteFaces", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }


    public static void getDevice(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("getDevice",null);
        Call<DeviceDetail> call = apiService.getDevice("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<DeviceDetail> call, @NotNull Response<DeviceDetail> response) {
                if (response.isSuccessful()) {
                    DeviceDetail apiResponse = response.body();
                    debugShowResponse("getDevice", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onDeviceDetail(apiResponse);
                    }
                } else {
                    debugShowResponse("getDevice", new ResponseError(response.code(), "HTTP response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<DeviceDetail> call, @NotNull Throwable throwable) {
                debugShowResponse("getDevice", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }


    public static void getSettings(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }
        debugShowRequest("getSettings",null);
        Call<DeviceSettings> call = apiService.getSettings("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<DeviceSettings> call, @NotNull Response<DeviceSettings> response) {
                if (response.isSuccessful()) {
                    DeviceSettings apiResponse = response.body();
                    debugShowResponse("getSettings", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onGetSettings(apiResponse);
                    }
                } else {
                    debugShowResponse("getSettings", new ResponseError(response.code(), "HTTP Response Fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<DeviceSettings> call, @NotNull Throwable throwable) {
                debugShowResponse("getSettings", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public void setSettings(String token, SettingsRequest settings) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("setSettings",settings);
        Call<Void> call = apiService.setSettings("Bearer " + token, settings);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setSettings", null);
                    iApiAccountListener.onSetSettings();
                } else {
                    debugShowResponse("setSettings", new ResponseError(response.code(), "HTTP Response"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setSettings", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setSettings(String token, ModeSet settings) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("setSettings",settings);
        Call<Void> call = apiService.setSettings("Bearer " + token, settings);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setSettings", null);
                    iApiAccountListener.onSetSettings();
                } else {
                    debugShowResponse("setSettings", new ResponseError(response.code(), "HTTP Response"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setSettings", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setSession(String token, String sessionmode) {
        if (retrofit == null) {
            getRetrofitInstance();
        }


        SetSession session = new SetSession(sessionmode);
        debugShowRequest("setSession",session);
        Call<Void> call = apiService.setSession("Bearer " + token, session);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setSession", null);
                    iApiAccountListener.onSetSession();
                } else {
                    debugShowResponse("setSession", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setSession", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void getSession(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("getSession",null);
        Call<SessionResponse> call = apiService.getSession("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SessionResponse> call, @NotNull Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    SessionResponse apiResponse = response.body();
                    debugShowResponse("setSession", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onGetSession(apiResponse);
                    }
                } else {
                    debugShowResponse("setSession", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<SessionResponse> call, @NotNull Throwable throwable) {
                debugShowResponse("getSession", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void getSessionAgeResult(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("getSessionAgeResult",null);
        Call<SessionAgeResult> call = apiService.getSessionAgeResult("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SessionAgeResult> call, @NotNull Response<SessionAgeResult> response) {
                if (response.isSuccessful()) {
                    SessionAgeResult apiResponse = response.body();
                    debugShowResponse("getSessionAgeResult", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onGetAgeResult(apiResponse);
                    }
                } else {
                    debugShowResponse("getSessionAgeResult", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<SessionAgeResult> call, @NotNull Throwable throwable) {
                debugShowResponse("getSessionAgeResult", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void getSessionScanResult(String token) {
        if (retrofit == null) {
            getRetrofitInstance();
        }

        debugShowRequest("getSessionScanResult",null);
        Call<SessionScanResult> call = apiService.getSessionScanResult("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SessionScanResult> call, @NotNull Response<SessionScanResult> response) {
                if (response.isSuccessful()) {
                    SessionScanResult apiResponse = response.body();
                    debugShowResponse("getSessionScanResult", apiResponse);
                    if (apiResponse != null) {
                        iApiAccountListener.onGetScanResult(apiResponse);
                    }
                } else {
                    debugShowResponse("getSessionScanResult", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<SessionScanResult> call, @NotNull Throwable throwable) {
                debugShowResponse("getSessionScanResult", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }

    public static void setStreamFaceBoxDisplay(String token, int displayBox) {
        if (retrofit == null) {
            getRetrofitInstance();
        }


        StreamSetFaceBox stream = new StreamSetFaceBox(displayBox);
        debugShowRequest("setStreamFaceBoxDisplay",stream);
        Call<Void> call = apiService.setStreamSettings("Bearer " + token, stream);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    debugShowResponse("setStreamFaceBoxDisplay", null);
                    iApiAccountListener.onSetStreamSettings();
                } else {
                    debugShowResponse("setStreamFaceBoxDisplay", new ResponseError(response.code(), "HTTP Response fail"));
                    iApiAccountListener.onRequestFail(ICUError.HTTP_RESPONSE, "http response fail " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable throwable) {
                debugShowResponse("setStreamFaceBoxDisplay", new ResponseError(0, throwable.getMessage()));
                iApiAccountListener.onRequestFail(ICUError.NO_CONNECTION, throwable.getMessage());
            }
        });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an okhttp3.OkHttpClient with the sslSocketFactory
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void debugShowRequest(String methodName, Object request) {

        if(showDebug == false){
            return;
        }

        String data = "";
        if(request != null) {
            Gson gson = new Gson();
            data = gson.toJson(request);
        }

        LocalDateTime currentTime = LocalDateTime.now();

        // Format the current time using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("API request: " + currentTime.format(formatter));
        System.out.println(methodName);
        System.out.println(data);


    }

    private static void debugShowResponse(String methodName, Object response) {

        if(showDebug == false){
            return;
        }

        String data = "";
        if(response != null) {
            Gson gson = new Gson();
            data = gson.toJson(response);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        // Format the current time using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("API response: " + currentTime.format(formatter));
        System.out.println(methodName);
        System.out.println(data);

        System.out.println("-------------------------------------------------------------------------------------");


    }


}
