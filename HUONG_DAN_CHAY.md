# HƯỚNG DẪN CHẠY DỰ ÁN

## ✅ Đã Fix Xong!

Dự án đã được sửa lỗi và có thể chạy được. Các lỗi đã fix:

1. ✅ Sửa lỗi duplicate method `sortByScoreDesc()` trong `StudentServiceImpl`
2. ✅ Thêm import thiếu `IOException` và `Files` trong `DatabaseConfig`
3. ✅ Xóa import không dùng `java.sql.*` trong `AccountServiceImpl`
4. ✅ Build thành công với Maven
5. ✅ Tạo file JAR executable
6. ✅ Tạo script chạy tiện lợi

## 🚀 Cách Chạy Dự Án

### Cách 1: Chạy với Script (Khuyến nghị)

**Windows PowerShell:**
```powershell
.\run.ps1
```

**Windows CMD:**
```cmd
run.bat
```

### Cách 2: Chạy với Maven
```bash
mvn clean package
java -jar target/QuanLyDayThem.jar
```

### Cách 3: Chạy từ source
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## 📂 Vị Trí Database

Database sẽ được tự động tạo tại:
```
C:\Users\<TenDangNhap>\QuanLyDayThem\daythem.db
```

## 🔧 Các File Quan Trọng

- `target/QuanLyDayThem.jar` - File JAR executable chứa toàn bộ ứng dụng
- `run.ps1` - Script PowerShell để chạy ứng dụng
- `run.bat` - Script CMD để chạy ứng dụng
- `pom.xml` - Maven configuration

## ⚠️ Lưu Ý

1. **Encoding Console**: Nếu thấy ký tự tiếng Việt bị lỗi trong console, đây là vấn đề của Windows Terminal. Ứng dụng vẫn hoạt động bình thường, dữ liệu trong database được lưu đúng.

2. **Java Version**: Dự án yêu cầu Java 17 trở lên. Kiểm tra với:
   ```bash
   java -version
   ```

3. **Dependencies**: Tất cả dependencies đã được đóng gói trong file JAR, không cần cài đặt thêm gì.

## 📊 Tính Năng Hiện Tại

- ✅ Kết nối SQLite database
- ✅ Khởi tạo schema tự động
- ✅ Thêm dữ liệu học sinh mẫu
- ✅ CRUD operations cho Student
- ✅ Repository pattern
- ✅ Service layer

## 🎯 Kết Quả Test

Ứng dụng đã test thành công:
- Database được tạo tự động
- Học sinh mẫu được thêm vào
- Không có lỗi compile
- Application khởi động bình thường

## 📝 Log Mẫu Khi Chạy

```
=== Khởi động ứng dụng Quản Lý Dạy Thêm ===
✓ Khởi tạo tables thành công
✓ Kết nối SQLite thành công: C:\Users\hoang\QuanLyDayThem\daythem.db

Đã có 1 học sinh trong database
  - HS001: Nguyễn Văn An

=== Ứng dụng đã sẵn sàng ===
Database location: C:\Users\hoang\QuanLyDayThem\daythem.db
```

---
**Tạo bởi**: GitHub Copilot
**Ngày**: 06/12/2025
