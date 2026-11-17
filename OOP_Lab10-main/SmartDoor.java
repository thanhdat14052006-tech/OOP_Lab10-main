/*
NGUYEN THANH DAT
202419040
*/
public final class SmartDoor implements IDevice, ILockable {
    private final String name;
    private boolean on;
    private boolean locked;

    public SmartDoor(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten thiet bi khong duoc rong.");
        }
        this.name = name.trim();
    }

    @Override
    public void turnOn() {
        on = true;
        System.out.println(name + ": ON");
    }

    @Override
    public void turnOff() {
        on = false;
        System.out.println(name + ": OFF");
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return on;
    }

    @Override
    public void lock() {
        locked = true;
        System.out.println(name + ": LOCKED");
    }

    @Override
    public void unlock() {
        locked = false;
        System.out.println(name + ": UNLOCKED");
    }

    public boolean isLocked() {
        return locked;
    }
}
