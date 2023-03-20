package api;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public final class SettingsResponse {
    @NotNull
    private final String DeviceName;
    @NotNull
    private final List<Camera> Cameras;

    @NotNull
    public final String getDeviceName() {
        return this.DeviceName;
    }

    @NotNull
    public final List<Camera> getCameras() {
        return this.Cameras;
    }

    public SettingsResponse(@NotNull String DeviceName, @NotNull List<Camera> Cameras) {
        super();
        Intrinsics.checkNotNullParameter(DeviceName, "DeviceName");
        Intrinsics.checkNotNullParameter(Cameras, "Cameras");
        this.DeviceName = DeviceName;
        this.Cameras = Cameras;
    }

    @NotNull
    public final String component1() {
        return this.DeviceName;
    }

    @NotNull
    public final List component2() {
        return this.Cameras;
    }

    @NotNull
    public final SettingsResponse copy(@NotNull String DeviceName, @NotNull List<Camera> Cameras) {
        Intrinsics.checkNotNullParameter(DeviceName, "DeviceName");
        Intrinsics.checkNotNullParameter(Cameras, "Cameras");
        return new SettingsResponse(DeviceName, Cameras);
    }

    // $FF: synthetic method
    public static SettingsResponse copy$default(SettingsResponse var0, String var1, List var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.DeviceName;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.Cameras;
        }

        return var0.copy(var1, var2);
    }

    @NotNull
    public String toString() {
        return "SettingsResponse(DeviceName=" + this.DeviceName + ", Cameras=" + this.Cameras + ")";
    }

    public int hashCode() {
        String var10000 = this.DeviceName;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        List var10001 = this.Cameras;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(@Nullable Object var1) {
        if (this != var1) {
            if (var1 instanceof SettingsResponse) {
                SettingsResponse var2 = (SettingsResponse)var1;
                if (Intrinsics.areEqual(this.DeviceName, var2.DeviceName) && Intrinsics.areEqual(this.Cameras, var2.Cameras)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }
}
