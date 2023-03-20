package api;



public final class DeviceCamera {
     
    private String Name;
     
    private String Type;
     
    private String Data;
    private boolean Enabled;

     
    public final String getName() {
        return this.Name;
    }

    public final void setName(  String var1) {
        this.Name = var1;
    }

     
    public final String getType() {
        return this.Type;
    }

    public final void setType(  String var1) {
        this.Type = var1;
    }

     
    public final String getData() {
        return this.Data;
    }

    public final void setData(  String var1) {
        this.Data = var1;
    }

    public final boolean getEnabled() {
        return this.Enabled;
    }

    public final void setEnabled(boolean var1) {
        this.Enabled = var1;
    }

    public DeviceCamera(  String Name,   String Type,   String Data, boolean Enabled) {
        super();
        this.Name = Name;
        this.Type = Type;
        this.Data = Data;
        this.Enabled = Enabled;
    }

     
    public final String component1() {
        return this.Name;
    }

     
    public final String component2() {
        return this.Type;
    }

     
    public final String component3() {
        return this.Data;
    }

    public final boolean component4() {
        return this.Enabled;
    }

     
    public final DeviceCamera copy(  String Name,   String Type,   String Data, boolean Enabled) {
        return new DeviceCamera(Name, Type, Data, Enabled);
    }

    // $FF: synthetic method
    public static DeviceCamera copy$default(DeviceCamera var0, String var1, String var2, String var3, boolean var4, int var5, Object var6) {
        if ((var5 & 1) != 0) {
            var1 = var0.Name;
        }

        if ((var5 & 2) != 0) {
            var2 = var0.Type;
        }

        if ((var5 & 4) != 0) {
            var3 = var0.Data;
        }

        if ((var5 & 8) != 0) {
            var4 = var0.Enabled;
        }

        return var0.copy(var1, var2, var3, var4);
    }

     
    public String toString() {
        return "DeviceCamera(Name=" + this.Name + ", Type=" + this.Type + ", Data=" + this.Data + ", Enabled=" + this.Enabled + ")";
    }


}
