/*
NGUYEN THANH DAT
202419040
*/
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Kiem thu truc tiep trong main; khong can lop test rieng.
        System.out.println("=== KIEM THU ===");
        int passed = 0;
        {
            check(DeviceFactory.create(" LiGhT ", "Den") instanceof SmartLight, "Sai loai den.");
            check(DeviceFactory.create("FAN", "Quat") instanceof SmartFan, "Sai loai quat.");
            check(DeviceFactory.create("door", "Cua") instanceof SmartDoor, "Sai loai cua.");
            passed++;
            System.out.println("DAT - " + "T1: Factory tao dung ba loai, khong phan biet hoa thuong");

        }

        {
            expectInvalid(() -> DeviceFactory.create("camera", "Camera"));
            passed++;
            System.out.println("DAT - " + "T2: Loai camera chua ho tro gay ngoai le");

        }

        {
            HomeConfiguration c1 = HomeConfiguration.getInstance();
            HomeConfiguration c2 = HomeConfiguration.getInstance();
            check(c1 == c2, "Singleton tao nhieu doi tuong.");
            String previous = c1.getHomeName();
            try {
                c1.setHomeName("Nha kiem thu");
                check("Nha kiem thu".equals(c2.getHomeName()), "Khong chia se cau hinh.");
            } finally {
                c1.setHomeName(previous);
            }
            passed++;
            System.out.println("DAT - " + "T3: Hai tham chieu cung mot cau hinh");

        }

        {
            SmartLight light = (SmartLight) DeviceFactory.create("light", "Den");
            SmartFan fan = (SmartFan) DeviceFactory.create("fan", "Quat");
            SmartDoor door = (SmartDoor) DeviceFactory.create("door", "Cua");
            light.turnOn();
            fan.turnOn();
            door.unlock();
            new SmartHomeFacade(light, fan, door).goToWork();
            check(!light.isOn() && !fan.isOn() && door.isLocked(), "Sai che do di lam.");
            passed++;
            System.out.println("DAT - " + "T4: Di lam tat den, tat quat, khoa cua");

        }

        {
            SmartLight light = (SmartLight) DeviceFactory.create("light", "Den");
            SmartFan fan = (SmartFan) DeviceFactory.create("fan", "Quat");
            SmartDoor door = (SmartDoor) DeviceFactory.create("door", "Cua");
            HomeConfiguration config = HomeConfiguration.getInstance();
            int previous = config.getNightLightLevel();
            PrintStream original = System.out;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fan.turnOn();
            door.unlock();
            try (PrintStream capture = new PrintStream(buffer)) {
                config.setNightLightLevel(45);
                System.setOut(capture);
                new SmartHomeFacade(light, fan, door).goToSleep();
            } finally {
                System.setOut(original);
                config.setNightLightLevel(previous);
            }
            check(light.isOn() && !fan.isOn() && door.isLocked(), "Sai che do di ngu.");
            check(buffer.toString().contains("Muc sang den ngu: 45%"), "Khong doc muc sang cau hinh.");
            passed++;
            System.out.println("DAT - " + "T5: Di ngu bat den, tat quat, khoa cua va hien muc sang");

        }

        {
            expectInvalid(() -> DeviceFactory.create(null, "Den"));
            expectInvalid(() -> DeviceFactory.create(" ", "Den"));
            expectInvalid(() -> DeviceFactory.create("light", " "));
            expectInvalid(() -> DeviceFactory.create("fan", null));
            expectInvalid(() -> DeviceFactory.create("door", ""));
            HomeConfiguration config = HomeConfiguration.getInstance();
            expectInvalid(() -> config.setNightLightLevel(-1));
            expectInvalid(() -> config.setNightLightLevel(101));
            expectInvalid(() -> config.setHomeName(" "));
            SmartDoor door = (SmartDoor) DeviceFactory.create("door", "Cua");
            door.turnOn();
            check(door.isOn(), "Cua chua bat.");
            door.lock();
            door.turnOff();
            check(!door.isOn() && door.isLocked(), "Bat tat khong duoc tu doi khoa.");
            door.unlock();
            check(!door.isLocked(), "Cua chua mo khoa.");
            passed++;
            System.out.println("DAT - " + "T6: Kiem tra du lieu va hanh vi rieng cua cua");

        }

        {
            List<String> calls = new ArrayList<>();
            IDevice light = recordingDevice("den", calls);
            IDevice fan = recordingDevice("quat", calls);
            ILockable door = new ILockable() {
                public void lock() {
                    calls.add("khoa");
                }

                public void unlock() {
                    calls.add("mo khoa");
                }
            };
            SmartHomeFacade facade = new SmartHomeFacade(light, fan, door);
            facade.goToWork();
            check(calls.equals(Arrays.asList("den tat", "quat tat", "khoa")), "Sai thu tu di lam.");
            calls.clear();
            facade.goToSleep();
            check(calls.equals(Arrays.asList("quat tat", "khoa", "den bat")), "Sai thu tu di ngu.");
            passed++;
            System.out.println("DAT - " + "T7: Facade lam viec qua interface va dung thu tu");

        }

        System.out.println("KET QUA: " + passed + "/7 nhom kiem thu DAT.\n");

        HomeConfiguration config = HomeConfiguration.getInstance();
        config.setHomeName("Nha thong minh mini");
        config.setNightLightLevel(30);

        // Moi thiet bi deu duoc tao qua Factory.
        IDevice light = DeviceFactory.create("light", "Den phong khach");
        IDevice fan = DeviceFactory.create("fan", "Quat phong khach");
        IDevice door = DeviceFactory.create("door", "Cua chinh");
        SmartHomeFacade home = new SmartHomeFacade(light, fan, (ILockable) door);

        System.out.println("=== " + config.getHomeName() + " ===");
        light.turnOn();
        fan.turnOn();
        door.turnOn();
        ((ILockable) door).unlock();

        System.out.println("\n--- Che do di lam ---");
        home.goToWork();
        System.out.println("\n--- Che do di ngu ---");
        home.goToSleep();
        System.out.println("\nHai tham chieu cung doi tuong cau hinh: "
                + (config == HomeConfiguration.getInstance()));

        // Them --interactive neu muon tu nhap loai thiet bi.
        if (args.length > 0 && "--interactive".equals(args[0])) {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("\nNhap loai (light/fan/door), hoac exit: ");
                    if (!scanner.hasNextLine())
                        break;
                    String type = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(type.trim()))
                        break;
                    System.out.print("Nhap ten thiet bi: ");
                    if (!scanner.hasNextLine())
                        break;
                    String name = scanner.nextLine();
                    try {
                        IDevice device = DeviceFactory.create(type, name);
                        device.turnOn();
                        device.turnOff();
                        if (device instanceof ILockable) {
                            ((ILockable) device).lock();
                            ((ILockable) device).unlock();
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Loi: " + ex.getMessage());
                    }
                }
            }
        }
    }

    // Cac ham ho tro chi kiem tra dieu kien va tao doi tuong gia.
    private static void check(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
    }

    private static void expectInvalid(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Phai nem IllegalArgumentException.");
    }

    private static IDevice recordingDevice(String name, List<String> calls) {
        return new IDevice() {
            public void turnOn() {
                calls.add(name + " bat");
            }

            public void turnOff() {
                calls.add(name + " tat");
            }
        };
    }
}
