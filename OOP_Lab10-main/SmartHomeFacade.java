/*
NGUYEN THANH DAT
202419040
*/
import java.util.Objects;

public final class SmartHomeFacade {
    private final IDevice light;
    private final IDevice fan;
    private final ILockable door;

    // Nhan phu thuoc qua interface de khong gan chat voi lop cu the.
    public SmartHomeFacade(IDevice light, IDevice fan, ILockable door) {
        this.light = Objects.requireNonNull(light, "Den khong duoc null.");
        this.fan = Objects.requireNonNull(fan, "Quat khong duoc null.");
        this.door = Objects.requireNonNull(door, "Cua khong duoc null.");
    }

    public void goToWork() {
        light.turnOff();
        fan.turnOff();
        door.lock();
    }

    public void goToSleep() {
        fan.turnOff();
        door.lock();
        light.turnOn();
        int level = HomeConfiguration.getInstance().getNightLightLevel();
        // Mo phong muc sang bang thong bao, dung nhu ma mau trong de.
        System.out.println("Muc sang den ngu: " + level + "%");
    }
}
