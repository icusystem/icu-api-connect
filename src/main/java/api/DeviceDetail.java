package api;


import java.util.List;

public final class DeviceDetail {

    private String DeviceId;

    private String DeviceName;

    private String DeviceType;

    private String HwLevel;

    private String SWBuildVersion;

    private List Cameras;


    public final String getDeviceId() {
        return this.DeviceId;
    }

    public final void setDeviceId(String var1) {

        this.DeviceId = var1;
    }


    public final String getDeviceName() {
        return this.DeviceName;
    }

    public final void setDeviceName(String var1) {

        this.DeviceName = var1;
    }


    public final String getDeviceType() {
        return this.DeviceType;
    }

    public final void setDeviceType(String var1) {

        this.DeviceType = var1;
    }


    public final String getHwLevel() {
        return this.HwLevel;
    }

    public final void setHwLevel( String var1) {

        this.HwLevel = var1;
    }


    public final String getSWBuildVersion() {
        return this.SWBuildVersion;
    }

    public final void setSWBuildVersion(String var1) {

        this.SWBuildVersion = var1;
    }

     
    public final List getCameras() {
        return this.Cameras;
    }

    public final void setCameras(  List var1) {

        this.Cameras = var1;
    }

    public DeviceDetail(  String DeviceId,   String DeviceName,   String DeviceType,   String HwLevel,   String SWBuildVersion,   List Cameras) {
        super();
        this.DeviceId = DeviceId;
        this.DeviceName = DeviceName;
        this.DeviceType = DeviceType;
        this.HwLevel = HwLevel;
        this.SWBuildVersion = SWBuildVersion;
        this.Cameras = Cameras;
    }

     
    public final String component1() {
        return this.DeviceId;
    }

     
    public final String component2() {
        return this.DeviceName;
    }

     
    public final String component3() {
        return this.DeviceType;
    }

     
    public final String component4() {
        return this.HwLevel;
    }

     
    public final String component5() {
        return this.SWBuildVersion;
    }

     
    public final List component6() {
        return this.Cameras;
    }

     
    public final DeviceDetail copy(  String DeviceId,   String DeviceName,   String DeviceType,   String HwLevel,   String SWBuildVersion,   List Cameras) {
        return new DeviceDetail(DeviceId, DeviceName, DeviceType, HwLevel, SWBuildVersion, Cameras);
    }

    // $FF: synthetic method
    public static DeviceDetail copy$default(DeviceDetail var0, String var1, String var2, String var3, String var4, String var5, List var6, int var7, Object var8) {
        if ((var7 & 1) != 0) {
            var1 = var0.DeviceId;
        }

        if ((var7 & 2) != 0) {
            var2 = var0.DeviceName;
        }

        if ((var7 & 4) != 0) {
            var3 = var0.DeviceType;
        }

        if ((var7 & 8) != 0) {
            var4 = var0.HwLevel;
        }

        if ((var7 & 16) != 0) {
            var5 = var0.SWBuildVersion;
        }

        if ((var7 & 32) != 0) {
            var6 = var0.Cameras;
        }

        return var0.copy(var1, var2, var3, var4, var5, var6);
    }

     
    public String toString() {
        return "DeviceDetail(DeviceId=" + this.DeviceId + ", DeviceName=" + this.DeviceName + ", DeviceType=" + this.DeviceType + ", HwLevel=" + this.HwLevel + ", SWBuildVersion=" + this.SWBuildVersion + ", Cameras=" + this.Cameras + ")";
    }



}