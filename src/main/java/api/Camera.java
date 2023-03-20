package api;



public final class Camera {
    private boolean Enabled;
     
    private String Name;
    private int Id_Index;
    private int Spoof_Level;
    private int Camera_distance;
    private boolean Pose_filter;
    private int Rotation;
     
    private String Resolution;
    private boolean Active;
    private boolean Face_rec_en;
     
    private String View_mode;

    public final boolean getEnabled() {
        return this.Enabled;
    }

    public final void setEnabled(boolean var1) {
        this.Enabled = var1;
    }

     
    public final String getName() {
        return this.Name;
    }

    public final void setName(  String var1) {

        this.Name = var1;
    }

    public final int getId_Index() {
        return this.Id_Index;
    }

    public final void setId_Index(int var1) {
        this.Id_Index = var1;
    }

    public final int getSpoof_Level() {
        return this.Spoof_Level;
    }

    public final void setSpoof_Level(int var1) {
        this.Spoof_Level = var1;
    }

    public final int getCamera_distance() {
        return this.Camera_distance;
    }

    public final void setCamera_distance(int var1) {
        this.Camera_distance = var1;
    }

    public final boolean getPose_filter() {
        return this.Pose_filter;
    }

    public final void setPose_filter(boolean var1) {
        this.Pose_filter = var1;
    }

    public final int getRotation() {
        return this.Rotation;
    }

    public final void setRotation(int var1) {
        this.Rotation = var1;
    }

     
    public final String getResolution() {
        return this.Resolution;
    }

    public final void setResolution(  String var1) {
        this.Resolution = var1;
    }

    public final boolean getActive() {
        return this.Active;
    }

    public final void setActive(boolean var1) {
        this.Active = var1;
    }

    public final boolean getFace_rec_en() {
        return this.Face_rec_en;
    }

    public final void setFace_rec_en(boolean var1) {
        this.Face_rec_en = var1;
    }

     
    public final String getView_mode() {
        return this.View_mode;
    }

    public final void setView_mode(  String var1) {

        this.View_mode = var1;
    }

    public Camera(boolean Enabled,   String Name, int Id_Index, int Spoof_Level, int Camera_distance, boolean Pose_filter, int Rotation,   String Resolution, boolean Active, boolean Face_rec_en,   String View_mode) {
        super();

        this.Enabled = Enabled;
        this.Name = Name;
        this.Id_Index = Id_Index;
        this.Spoof_Level = Spoof_Level;
        this.Camera_distance = Camera_distance;
        this.Pose_filter = Pose_filter;
        this.Rotation = Rotation;
        this.Resolution = Resolution;
        this.Active = Active;
        this.Face_rec_en = Face_rec_en;
        this.View_mode = View_mode;
    }

    public final boolean component1() {
        return this.Enabled;
    }

     
    public final String component2() {
        return this.Name;
    }

    public final int component3() {
        return this.Id_Index;
    }

    public final int component4() {
        return this.Spoof_Level;
    }

    public final int component5() {
        return this.Camera_distance;
    }

    public final boolean component6() {
        return this.Pose_filter;
    }

    public final int component7() {
        return this.Rotation;
    }

     
    public final String component8() {
        return this.Resolution;
    }

    public final boolean component9() {
        return this.Active;
    }

    public final boolean component10() {
        return this.Face_rec_en;
    }

     
    public final String component11() {
        return this.View_mode;
    }

     
    public final Camera copy(boolean Enabled,   String Name, int Id_Index, int Spoof_Level, int Camera_distance, boolean Pose_filter, int Rotation,   String Resolution, boolean Active, boolean Face_rec_en,   String View_mode) {

        return new Camera(Enabled, Name, Id_Index, Spoof_Level, Camera_distance, Pose_filter, Rotation, Resolution, Active, Face_rec_en, View_mode);
    }

    // $FF: synthetic method
    public static Camera copy$default(Camera var0, boolean var1, String var2, int var3, int var4, int var5, boolean var6, int var7, String var8, boolean var9, boolean var10, String var11, int var12, Object var13) {
        if ((var12 & 1) != 0) {
            var1 = var0.Enabled;
        }

        if ((var12 & 2) != 0) {
            var2 = var0.Name;
        }

        if ((var12 & 4) != 0) {
            var3 = var0.Id_Index;
        }

        if ((var12 & 8) != 0) {
            var4 = var0.Spoof_Level;
        }

        if ((var12 & 16) != 0) {
            var5 = var0.Camera_distance;
        }

        if ((var12 & 32) != 0) {
            var6 = var0.Pose_filter;
        }

        if ((var12 & 64) != 0) {
            var7 = var0.Rotation;
        }

        if ((var12 & 128) != 0) {
            var8 = var0.Resolution;
        }

        if ((var12 & 256) != 0) {
            var9 = var0.Active;
        }

        if ((var12 & 512) != 0) {
            var10 = var0.Face_rec_en;
        }

        if ((var12 & 1024) != 0) {
            var11 = var0.View_mode;
        }

        return var0.copy(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
    }

     
    public String toString() {
        return "Camera(Enabled=" + this.Enabled + ", Name=" + this.Name + ", Id_Index=" + this.Id_Index + ", Spoof_Level=" + this.Spoof_Level + ", Camera_distance=" + this.Camera_distance + ", Pose_filter=" + this.Pose_filter + ", Rotation=" + this.Rotation + ", Resolution=" + this.Resolution + ", Active=" + this.Active + ", Face_rec_en=" + this.Face_rec_en + ", View_mode=" + this.View_mode + ")";
    }



}