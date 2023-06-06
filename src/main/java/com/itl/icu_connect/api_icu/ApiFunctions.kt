package com.itl.icu_connect.api_icu



import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class ApiFunctions {




    companion object {
       // private const val TAG = "ApiFunctions"
        private lateinit var iApiAccountListener: IAPIAccount
        private var apiService: ApiService? = null
        private var retrofit:Retrofit? = null



        fun setOnAPIAccountListener(listener: IAPIAccount) {
            iApiAccountListener = listener
        }


        private fun getBaseURL(): String{
            return "https://192.168.137.8:44345"
        }

        private fun getRetoInstance(){
            // Create Retrofit
             retrofit = Retrofit.Builder()
                .baseUrl(getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(this.getUnsafeOkHttpClient()!!)
                .build()

            apiService = Companion.retrofit?.create(ApiService::class.java)

        }


        /**
         * ICU API request to obtain the authorization token from
         * the ICU device
         */
        fun getToken(username: String, password: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val params = HashMap<String, String>()
            params["grant_type"] = "password"
            params["username"] = username
            params["password"] = password
            val call: Call<Token> = apiService?.getToken(params)!!
            call.enqueue(object : Callback<Token?> {
                override fun onResponse(call: Call<Token?>, response: Response<Token?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener?.onToken(apiResponse)
                        }
                    } else {
                        // Handle error response
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Token?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })
        }


        fun getStatus(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<StatusResponse> = apiService?.getStatus("Bearer $token")!!
            call.enqueue(object : Callback<StatusResponse?> {
                override fun onResponse(call: Call<StatusResponse?>, response: Response<StatusResponse?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onStatus(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<StatusResponse?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })


        }



         fun enrollImage(token: String, imageB64: String) {

             if(retrofit == null){
                getRetoInstance()
             }

            val enroll = EnrollRequest(imageB64)
            val call: Call<EnrollResponse> = apiService?.getImageUID("Bearer $token", enroll)!!
            call.enqueue(object : Callback<EnrollResponse> {
                override fun onResponse(call: Call<EnrollResponse>, response: Response<EnrollResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onFaceIDSuccess(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<EnrollResponse>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })
        }


        fun updateFaceData(token: String, face: UpdateFaceData) {

            if(retrofit == null){
               getRetoInstance()
            }

            val call: Call<Void> = apiService?.setImageUpdate("Bearer $token", face)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onFacesUpdated()
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }


        fun deleteFaces(token: String, faces: FaceDelete) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<Void> = apiService?.setImageDelete("Bearer $token", faces)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onDeleteFaces()

                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }



        fun getDevice(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<DeviceDetail> = apiService?.getDevice("Bearer $token")!!
            call.enqueue(object : Callback<DeviceDetail?> {
                override fun onResponse(call: Call<DeviceDetail?>, response: Response<DeviceDetail?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onDeviceDetail(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<DeviceDetail?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })

        }



        fun getSettings(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<DeviceSettings> = apiService?.getSettings("Bearer $token")!!
            call.enqueue(object : Callback<DeviceSettings?> {
                override fun onResponse(call: Call<DeviceSettings?>, response: Response<DeviceSettings?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onGetSettings(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<DeviceSettings?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })

        }


        fun setSettings(token: String, settings: SettingsRequest) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<Void> = apiService?.setSettings("Bearer $token", settings)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onSetSettings()
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }


        fun setSettings(token: String, settings: ModeSet) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<Void> = apiService?.setSettings("Bearer $token", settings)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onSetSettings()
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }




        fun setSession(token: String, sessionmode: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val session = SetSession(sessionmode)
            val call: Call<Void> = apiService?.setSession("Bearer $token", session)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onSetSession()
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }


        fun getSession(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<SessionResponse> = apiService?.getSession("Bearer $token")!!
            call.enqueue(object : Callback<SessionResponse?> {
                override fun onResponse(call: Call<SessionResponse?>, response: Response<SessionResponse?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onGetSession(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<SessionResponse?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })

        }



        fun getSessionAgeResult(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<SessionAgeResult> = apiService?.getSessionAgeResult("Bearer $token")!!
            call.enqueue(object : Callback<SessionAgeResult?> {
                override fun onResponse(call: Call<SessionAgeResult?>, response: Response<SessionAgeResult?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onGetAgeResult(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<SessionAgeResult?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })

        }

        fun getSessionScanResult(token: String) {

            if(retrofit == null){
                getRetoInstance()
            }

            val call: Call<SessionScanResult> = apiService?.getSessionScanResult("Bearer $token")!!
            call.enqueue(object : Callback<SessionScanResult?> {
                override fun onResponse(call: Call<SessionScanResult?>, response: Response<SessionScanResult?>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            iApiAccountListener!!.onGetScanResult(apiResponse)
                        }
                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<SessionScanResult?>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }
            })

        }

        fun setStreamFaceBoxDisplay(token: String, displayBox:Int) {

            if(retrofit == null){
                getRetoInstance()
            }

            val stream = StreamSetFaceBox(displayBox)
            val call: Call<Void> = apiService?.setStreamSettings("Bearer $token", stream)!!
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        iApiAccountListener!!.onSetStreamSettings()

                    } else {
                        iApiAccountListener!!.onRequestFail(
                            ICUError.HTTP_RESPONSE,
                            "http response fail " + response.code()
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, throwable: Throwable) {
                    iApiAccountListener!!.onRequestFail(ICUError.NO_CONNECTION, throwable.message.toString())
                }

            })
        }




        private fun getUnsafeOkHttpClient(): OkHttpClient? {
            return try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate?>?,
                            authType: String?
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                val trustManagerFactory: TrustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers: Array<TrustManager> =
                    trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + trustManagers.contentToString()
                }

                val trustManager =
                    trustManagers[0] as X509TrustManager


                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustManager)
                builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
                builder.connectTimeout(5000,TimeUnit.MILLISECONDS)
                builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }


    }

}