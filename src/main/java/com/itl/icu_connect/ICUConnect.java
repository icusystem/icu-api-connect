package com.itl.icu_connect;

import api.ApiThread;
import api.IDeviceEvent;

import java.net.URL;

/**
 * ICUConnect class - instantiate from code
 * Use this object from your code to connect and run the
 *
 */
public class ICUConnect {

    private ApiThread apiThread;




    /**
     * @param deviceEventListener the event listener interface
     */
    public void Start( String api_username, String api_password, IDeviceEvent deviceEventListener){

        apiThread = new ApiThread();
        apiThread.setCredentials(api_username,api_password);
        apiThread.setDeviceEventListener(deviceEventListener);

        apiThread.start();

    }

    public void Start( String baseUrl, String api_username, String api_password, IDeviceEvent deviceEventListener){

        apiThread = new ApiThread(baseUrl);
        apiThread.setCredentials(api_username,api_password);
        apiThread.setDeviceEventListener(deviceEventListener);

        apiThread.start();

    }



    /**
     * Call from your instantiated object to stop the Connect worker thread
     */
    public void Stop(){
        apiThread.Stop();
    }

}
