# Smart Home Mini - Bai thuc hanh 10

## Phan A: Phan tich thiet ke

Abstraction chung la IDevice; kha nang khoa tach rieng thanh ILockable.
DeviceFactory quyet dinh lop can khoi tao. HomeConfiguration chi giu ten nha va
muc sang ban dem, khong giu trang thai tung thiet bi. Facade giu hai IDevice
(den, quat) va mot ILockable (cua). Client tao qua Factory, truyen vao Facade
va goi goToWork()/goToSleep(). Xem so do trong UML.md.

## Vi tri va loi ich cua ba pattern

| Pattern | Vi tri | Loi ich |
| --- | --- | --- |
| Factory (Simple Factory) | DeviceFactory.create() | Gom logic chon lop va tao thiet bi; tra ve IDevice |
| Singleton | HomeConfiguration.getInstance() | Chia se mot cau hinh; Holder khoi tao tre va an toan luong |
| Facade | SmartHomeFacade | Gom nhieu thao tac thanh hai che do de goi |

goToWork(): tat den -> tat quat -> khoa cua.
goToSleep(): tat quat -> khoa cua -> bat den -> doc va in muc sang ban dem.
Muc sang duoc mo phong bang thong bao theo ma mau; chua dieu khien phan cung
hay them ham dieu chinh do sang vao IDevice.

## Phan C va D: Cau hoi phan tich

1. Factory che giau viec chon lop cu the, goi constructor va chuan hoa loai.
2. Them SmartCamera: tao lop implements IDevice, them nhanh camera vao Factory,
   sua kiem thu T2 va them kiem thu moi. Chi sua Facade neu che do can dung camera.
3. Constructor Singleton phai la private de ngan new tu ben ngoai.
4. final trong Java ngan ke thua, tuong ung muc dich sealed trong C#.
5. Singleton tao trang thai dung chung, de lam test phu thuoc nhau va kho thay
   bang doi tuong gia. Test thay cau hinh phai khoi phuc gia tri trong finally.
6. Neu dung DI container voi lifetime singleton (vi du AddSingleton trong .NET),
   thuong khong can tu cai Singleton; container quan ly mot doi tuong trong pham vi cua no.

## Phan F: Kiem thu

Tat ca tinh huong T1-T7 duoc viet truc tiep trong Main.main(String[] args),
khong co lop SmartHomeTest rieng. Cac ham ho tro check/expectInvalid chi kiem tra
dieu kien va ngoai le; recordingDevice tao doi tuong gia cho T7.
Dung AssertionError truc tiep, khong can JUnit hay tuy chon -ea.
Neu kiem thu sai, chuong trinh dung voi AssertionError va ma thoat khac 0.
Chi in DAT sau khi cac dieu kien cua nhom da duoc kiem tra thanh cong.

| Ma | Tinh huong | Ket qua mong doi |
| --- | --- | --- |
| T1 | Tao light/fan/door, chu hoa thuong va khoang trang | Dung lop |
| T2 | Tao camera chua ho tro | IllegalArgumentException |
| T3 | Lay cau hinh hai lan | Cung tham chieu, cung du lieu |
| T4 | goToWork() tu trang thai dang bat/mo khoa | Den tat, quat tat, cua khoa |
| T5 | goToSleep() voi muc sang 45% | Den bat, quat tat, cua khoa, in 45% |
| T6 | Du lieu sai; bat/tat, khoa/mo khoa cua | Bao loi dung; trang thai doc lap |
| T7 | Facade nhan doi tuong gia qua interface | Dung thu tu thao tac |
