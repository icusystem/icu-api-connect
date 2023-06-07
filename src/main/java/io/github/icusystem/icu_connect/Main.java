package io.github.icusystem.icu_connect;

import io.github.icusystem.icu_connect.api_icu.CameraSettings;
import io.github.icusystem.icu_connect.api_icu.ICURunMode;

public class Main {


    private static Connect icuConnect;
    private static IcuListener icuListener;


    public static void main(String[] args)  {



        icuListener = new IcuListener();

        CameraSettings cam = new CameraSettings();
        cam.setCamera_distance(3);
        cam.setSpoof_level(0);
        cam.setRotation(0);
        cam.setPose_filter(false);



        icuConnect = new Connect(cam,"apiuser","apipassword");
        icuConnect.Start("Main",icuListener);

        icuConnect.SetMode(ICURunMode.AGE_ONLY);


        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
