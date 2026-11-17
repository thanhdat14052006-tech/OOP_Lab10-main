
/*
NGUYEN THANH DAT
202419040
*/
import java.util.Locale;

public final class DeviceFactory {
    private DeviceFactory() {
    }

    // Client chi can biet loai va ten, khong can goi constructor cu the.
    public static IDevice create(String type, String name) {
        if (type == null) {
            throw new IllegalArgumentException("Loai thiet bi khong duoc null.");
        }
        switch (type.trim().toLowerCase(Locale.ROOT)) {
            case "light":
                return new SmartLight(name);
            case "fan":
                return new SmartFan(name);
            case "door":
                return new SmartDoor(name);
            default:
                throw new IllegalArgumentException("Loai thiet bi khong duoc ho tro: " + type);
        }
    }
}
