/*
NGUYEN CHINH THAI
202419094
*/
public final class SmartLight implements IDevice {
    private final String name;
    private boolean on;

    public SmartLight(String name) {
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
}
