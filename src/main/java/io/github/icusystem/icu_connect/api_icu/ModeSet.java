package io.github.icusystem.icu_connect.api_icu;
import java.util.ArrayList;
import java.util.List;

public class ModeSet {

    public List<ModeCamera> Cameras = new ArrayList<>();

    public ModeSet() {
        ModeCamera cam1 = new ModeCamera();
        if (Cameras != null) {
            Cameras.add(cam1);
        }
    }


}


