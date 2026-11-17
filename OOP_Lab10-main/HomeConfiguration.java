/*
NGUYEN THANH DAT
202419040
*/
public final class HomeConfiguration {
    private volatile String homeName = "My Home";
    private volatile int nightLightLevel = 30;

    private HomeConfiguration() {
    }

    // JVM chi khoi tao lop Holder khi can va bao dam an toan luong.
    private static class Holder {
        private static final HomeConfiguration INSTANCE = new HomeConfiguration();
    }

    public static HomeConfiguration getInstance() {
        return Holder.INSTANCE;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        if (homeName == null || homeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten nha khong duoc rong.");
        }
        this.homeName = homeName.trim();
    }

    public int getNightLightLevel() {
        return nightLightLevel;
    }

    public void setNightLightLevel(int nightLightLevel) {
        if (nightLightLevel < 0 || nightLightLevel > 100) {
            throw new IllegalArgumentException("Muc sang phai tu 0 den 100.");
        }
        this.nightLightLevel = nightLightLevel;
    }
}
