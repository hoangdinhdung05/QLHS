# Ứng dụng Quản Lý Dạy Thêm

## 📋 Mục lục
- [Giới thiệu](#giới-thiệu)
- [Tính năng](#tính-năng)
- [Kiến trúc](#kiến-trúc)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt](#cài-đặt)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Database Schema](#database-schema)
- [Workflow phát triển](#workflow-phát-triển)
- [Đóng gói ứng dụng](#đóng-gói-ứng-dụng)
- [Hướng dẫn sử dụng](#hướng-dẫn-sử-dụng)

---

## 🎯 Giới thiệu

Ứng dụng **Quản Lý Dạy Thêm** là một phần mềm desktop được xây dựng bằng Java Swing, giúp giáo viên quản lý lớp dạy thêm tại nhà một cách hiệu quả. Ứng dụng sử dụng SQLite làm database (không cần server), phù hợp cho máy tính cá nhân.

### Đặc điểm nổi bật
- ✅ Giao diện đơn giản, thân thiện với người lớn tuổi
- ✅ Không cần cài đặt database server (SQLite embedded)
- ✅ Tự động tính toán thu nhập theo tháng
- ✅ Xuất báo cáo Excel cho phụ huynh
- ✅ Backup/Restore dữ liệu dễ dàng
- ✅ Chạy offline hoàn toàn

---

## 🚀 Tính năng

### 1. Quản lý học sinh
- CRUD đầy đủ (Thêm, Sửa, Xóa, Tìm kiếm)
- Lưu thông tin: họ tên, ngày sinh, địa chỉ, phụ huynh, SĐT
- Đánh dấu trạng thái: Đang học / Đã nghỉ
- Tìm kiếm nhanh theo tên hoặc mã học sinh

### 2. Quản lý lớp học
- Tạo lớp học với thông tin: môn học, lịch học, học phí/buổi
- Gán học sinh vào lớp (nhiều-nhiều)
- Xem danh sách học sinh trong lớp

### 3. Quản lý buổi học & Điểm danh
- Tạo buổi học theo lịch
- Điểm danh học sinh: Có mặt / Vắng / Muộn
- Ghi chú nội dung bài học

### 4. Tính toán thu nhập
- **Theo học sinh**: Tự động tính tổng buổi học và tiền học phí
- **Theo tháng**: Tổng thu nhập toàn bộ trong tháng
- Công thức: `Thu nhập = Số buổi có mặt × Học phí/buổi`

### 5. Báo cáo & Xuất file
- Xuất danh sách học sinh ra Excel
- Xuất báo cáo thu nhập hàng tháng (Excel/PDF)
- Gửi báo cáo cho phụ huynh qua email (tính năng mở rộng)

### 6. Backup & Restore
- Backup database bằng cách copy file `.db`
- Chọn thư mục lưu backup tùy ý
- Restore dễ dàng khi cần

---

## 🏗️ Kiến trúc

Ứng dụng tuân theo **Clean Architecture** đơn giản cho desktop:

```
┌─────────────────────────────────────┐
│         VIEW (Swing UI)             │  ← Giao diện người dùng
├─────────────────────────────────────┤
│       SERVICE (Business Logic)      │  ← Logic nghiệp vụ, tính toán
├─────────────────────────────────────┤
│       REPOSITORY (Data Access)      │  ← Truy cập database
├─────────────────────────────────────┤
│          ENTITY (Domain)            │  ← Đối tượng nghiệp vụ
├─────────────────────────────────────┤
│       CONFIG (DatabaseHelper)       │  ← Cấu hình DB, kết nối
└─────────────────────────────────────┘
           ↓
     SQLite Database
```

### Lớp (Layer) chi tiết:

#### 1. **Entity Layer** (`org.example.entity`)
Chứa các đối tượng domain:
- `Student.java` - Học sinh
- `ClassEntity.java` - Lớp học
- `LessonSession.java` - Buổi học
- `Attendance.java` - Điểm danh
- `Payment.java` - Thanh toán

#### 2. **Repository Layer** (`org.example.repository`)
Interface và implementation để truy cập database:
- `StudentRepository` / `StudentRepositoryImpl`
- `ClassRepository` / `ClassRepositoryImpl`
- `LessonSessionRepository` / `LessonSessionRepositoryImpl`
- `AttendanceRepository` / `AttendanceRepositoryImpl`

**Pattern sử dụng**: Repository Pattern với JDBC thuần

#### 3. **Service Layer** (`org.example.service`)
Business logic và các tính toán:
- `StudentService` - Quản lý học sinh
- `ClassService` - Quản lý lớp học
- `LessonService` - Quản lý buổi học
- `IncomeService` - Tính toán thu nhập
- `ReportService` - Tạo báo cáo

#### 4. **View Layer** (`org.example.view`)
Giao diện Swing:
- `MainFrame.java` - Màn hình chính
- `StudentManagementFrame.java` - Quản lý học sinh
- `ClassManagementFrame.java` - Quản lý lớp học
- `LessonManagementFrame.java` - Quản lý buổi học
- `AttendanceFrame.java` - Điểm danh
- `IncomeReportFrame.java` - Báo cáo thu nhập

#### 5. **Config Layer** (`org.example.config`)
- `DatabaseConfig.java` - Singleton quản lý kết nối SQLite

#### 6. **Util Layer** (`org.example.util`)
- `ExcelExporter.java` - Export Excel
- `PDFGenerator.java` - Tạo PDF
- `BackupUtil.java` - Backup/Restore database

---

## 💻 Yêu cầu hệ thống

### Phát triển:
- **JDK**: 17 trở lên
- **Maven**: 3.6+
- **IDE**: IntelliJ IDEA / Eclipse / VS Code

### Chạy ứng dụng:
- **JRE**: 17+ (hoặc đóng gói cùng JRE)
- **OS**: Windows 10/11, macOS, Linux
- **RAM**: 512 MB trở lên
- **Disk**: 50 MB trống

---

## 📦 Cài đặt

### Bước 1: Clone hoặc download project

```bash
git clone https://github.com/your-repo/quan-ly-day-them.git
cd quan-ly-day-them
```

### Bước 2: Build project với Maven

```bash
mvn clean install
```

### Bước 3: Chạy ứng dụng

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Hoặc chạy file JAR:

```bash
java -jar target/QuanLyDayThem.jar
```

---

## 📁 Cấu trúc dự án

```
QLSV-HUE/
├── pom.xml                           # Maven configuration
├── database-schema.sql               # Database schema SQL
├── README.md                         # Tài liệu này
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── example/
│   │   │           ├── Main.java                    # Entry point
│   │   │           ├── config/
│   │   │           │   └── DatabaseConfig.java      # SQLite config
│   │   │           ├── entity/
│   │   │           │   ├── Student.java
│   │   │           │   ├── ClassEntity.java
│   │   │           │   ├── LessonSession.java
│   │   │           │   ├── Attendance.java
│   │   │           │   └── Payment.java
│   │   │           ├── repository/
│   │   │           │   ├── StudentRepository.java
│   │   │           │   ├── ClassRepository.java
│   │   │           │   ├── LessonSessionRepository.java
│   │   │           │   ├── AttendanceRepository.java
│   │   │           │   └── impl/
│   │   │           │       ├── StudentRepositoryImpl.java
│   │   │           │       ├── ClassRepositoryImpl.java
│   │   │           │       ├── LessonSessionRepositoryImpl.java
│   │   │           │       └── AttendanceRepositoryImpl.java
│   │   │           ├── service/
│   │   │           │   ├── StudentService.java
│   │   │           │   ├── ClassService.java
│   │   │           │   ├── LessonService.java
│   │   │           │   ├── IncomeService.java
│   │   │           │   └── impl/
│   │   │           │       ├── StudentServiceImpl.java
│   │   │           │       ├── ClassServiceImpl.java
│   │   │           │       ├── LessonServiceImpl.java
│   │   │           │       └── IncomeServiceImpl.java
│   │   │           ├── view/
│   │   │           │   ├── MainFrame.java
│   │   │           │   ├── StudentManagementFrame.java
│   │   │           │   ├── ClassManagementFrame.java
│   │   │           │   ├── LessonManagementFrame.java
│   │   │           │   ├── AttendanceFrame.java
│   │   │           │   └── IncomeReportFrame.java
│   │   │           └── util/
│   │   │               ├── ExcelExporter.java
│   │   │               ├── PDFGenerator.java
│   │   │               └── BackupUtil.java
│   │   └── resources/
│   │       └── (icons, images nếu có)
│   └── test/
│       └── java/
│           └── (test classes)
└── target/
    └── QuanLyDayThem.jar             # Executable JAR
```

---

## 🗄️ Database Schema

### Thiết kế ERD

```
┌────────────────┐       ┌────────────────┐       ┌────────────────┐
│   students     │       │ class_students │       │    classes     │
├────────────────┤       ├────────────────┤       ├────────────────┤
│ id (PK)        │◄─────►│ student_id (FK)│◄─────►│ id (PK)        │
│ student_code   │       │ class_id (FK)  │       │ class_code     │
│ full_name      │       │ joined_date    │       │ class_name     │
│ date_of_birth  │       │ left_date      │       │ subject        │
│ gender         │       │ status         │       │ fee_per_session│
│ phone          │       └────────────────┘       │ schedule       │
│ address        │                                │ status         │
│ parent_name    │                                └────────────────┘
│ parent_phone   │                                        │
│ status         │                                        │
│ notes          │                                        ▼
└────────────────┘                          ┌────────────────────┐
        │                                    │  lesson_sessions   │
        │                                    ├────────────────────┤
        │                                    │ id (PK)            │
        │                                    │ class_id (FK)      │
        │                                    │ lesson_date        │
        │                                    │ lesson_time        │
        │                                    │ lesson_topic       │
        │                                    │ notes              │
        │                                    └────────────────────┘
        │                                            │
        │                                            │
        └────────────────────┬───────────────────────┘
                             ▼
                    ┌────────────────┐
                    │  attendances   │
                    ├────────────────┤
                    │ id (PK)        │
                    │ lesson_id (FK) │
                    │ student_id (FK)│
                    │ status         │  ← PRESENT/ABSENT/LATE
                    │ notes          │
                    └────────────────┘
```

### Các bảng chính:

#### 1. **students** - Học sinh
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(20),
    address TEXT,
    parent_name VARCHAR(100),
    parent_phone VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',  -- ACTIVE, INACTIVE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. **classes** - Lớp học
```sql
CREATE TABLE classes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    fee_per_session DECIMAL(10, 2) NOT NULL,
    schedule VARCHAR(200),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. **lesson_sessions** - Buổi học
```sql
CREATE TABLE lesson_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_id INTEGER NOT NULL,
    lesson_date DATE NOT NULL,
    lesson_time VARCHAR(50),
    lesson_topic VARCHAR(200),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);
```

#### 4. **attendances** - Điểm danh
```sql
CREATE TABLE attendances (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_session_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'PRESENT',  -- PRESENT, ABSENT, LATE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lesson_session_id) REFERENCES lesson_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE(lesson_session_id, student_id)
);
```

### Query tính thu nhập

#### Thu nhập theo học sinh trong tháng:
```sql
SELECT 
    s.id, s.student_code, s.full_name,
    c.class_name, c.fee_per_session,
    COUNT(a.id) as total_sessions,
    (COUNT(a.id) * c.fee_per_session) as total_income
FROM students s
JOIN class_students cs ON s.id = cs.student_id
JOIN classes c ON cs.class_id = c.id
JOIN lesson_sessions ls ON c.id = ls.class_id
JOIN attendances a ON ls.id = a.lesson_session_id AND a.student_id = s.id
WHERE a.status = 'PRESENT'
  AND strftime('%Y', ls.lesson_date) = '2024'
  AND strftime('%m', ls.lesson_date) = '12'
GROUP BY s.id, c.id;
```

#### Tổng thu nhập toàn bộ theo tháng:
```sql
SELECT 
    strftime('%Y-%m', ls.lesson_date) as month,
    COUNT(DISTINCT ls.id) as total_lessons,
    COUNT(a.id) as total_attendances,
    SUM(c.fee_per_session) as total_income
FROM lesson_sessions ls
JOIN classes c ON ls.class_id = c.id
JOIN attendances a ON ls.id = a.lesson_session_id
WHERE a.status = 'PRESENT'
GROUP BY month
ORDER BY month DESC;
```

---

## 🔄 Workflow phát triển

### 1. Thứ tự triển khai tính năng

Nên làm theo thứ tự sau để đảm bảo logic rõ ràng:

```
1. Setup Database
   ├─ Tạo DatabaseConfig (SQLite connection)
   └─ Chạy script tạo tables

2. Entity Layer
   ├─ Tạo các class: Student, ClassEntity, LessonSession, Attendance
   └─ Implement getters/setters

3. Repository Layer
   ├─ Viết interface repositories
   ├─ Implement CRUD cho Student (ưu tiên)
   ├─ Implement CRUD cho Class
   ├─ Implement CRUD cho LessonSession
   └─ Implement CRUD cho Attendance

4. Service Layer
   ├─ StudentService (validate, business logic)
   ├─ ClassService
   ├─ LessonService
   └─ IncomeService (tính toán thu nhập)

5. View Layer
   ├─ MainFrame (menu chính)
   ├─ StudentManagementFrame (ưu tiên)
   ├─ ClassManagementFrame
   ├─ LessonManagementFrame
   ├─ AttendanceFrame
   └─ IncomeReportFrame

6. Utility Features
   ├─ ExcelExporter (xuất Excel)
   ├─ PDFGenerator (xuất PDF)
   └─ BackupUtil (backup/restore)

7. Testing & Polish
   ├─ Test từng chức năng
   ├─ Fix bugs
   └─ Tối ưu UI/UX
```

### 2. Git workflow

```bash
# Feature branch
git checkout -b feature/student-management
git add .
git commit -m "Implement student CRUD"
git push origin feature/student-management

# Merge vào main
git checkout main
git merge feature/student-management
```

### 3. Code convention

- **Package naming**: lowercase, singular (`entity`, không phải `entities`)
- **Class naming**: PascalCase (`StudentService`)
- **Method naming**: camelCase (`getStudentById`)
- **Variable naming**: camelCase (`studentList`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_STUDENTS`)

---

## 📦 Đóng gói ứng dụng

### Option 1: Executable JAR với Maven Shade Plugin

Đã cấu hình sẵn trong `pom.xml`:

```bash
mvn clean package
```

File JAR sẽ nằm ở: `target/QuanLyDayThem.jar`

Chạy:
```bash
java -jar target/QuanLyDayThem.jar
```

### Option 2: jpackage (Java 14+)

Tạo installer native cho Windows:

```bash
jpackage \
  --input target/ \
  --name "Quan Ly Day Them" \
  --main-jar QuanLyDayThem.jar \
  --main-class org.example.Main \
  --type exe \
  --icon icon.ico \
  --win-menu \
  --win-shortcut
```

Tham số:
- `--type exe` - Tạo `.exe` installer cho Windows
- `--type dmg` - Tạo `.dmg` cho macOS
- `--type deb` - Tạo `.deb` cho Linux
- `--icon` - Icon của ứng dụng
- `--win-menu` - Thêm vào Start Menu
- `--win-shortcut` - Tạo shortcut trên Desktop

### Option 3: Launch4j (Windows only)

1. Download Launch4j: https://launch4j.sourceforge.net/
2. Tạo config file `launch4j-config.xml`:

```xml
<launch4jConfig>
  <headerType>gui</headerType>
  <outfile>QuanLyDayThem.exe</outfile>
  <jar>target/QuanLyDayThem.jar</jar>
  <icon>icon.ico</icon>
  <classPath>
    <mainClass>org.example.Main</mainClass>
  </classPath>
  <jre>
    <minVersion>17</minVersion>
  </jre>
</launch4jConfig>
```

3. Build:
```bash
launch4jc launch4j-config.xml
```

### Option 4: GraalVM Native Image (Advanced)

Tạo native executable (không cần JRE):

```bash
native-image \
  --no-fallback \
  -H:+ReportExceptionStackTraces \
  -jar target/QuanLyDayThem.jar \
  QuanLyDayThem
```

**Lưu ý**: GraalVM có hạn chế với Swing, cần test kỹ.

---

## 📖 Hướng dẫn sử dụng

### 1. Khởi động ứng dụng

Chạy file JAR hoặc EXE. Database sẽ được tạo tự động tại:
- Windows: `C:\Users\<YourName>\QuanLyDayThem\daythem.db`
- macOS/Linux: `~/QuanLyDayThem/daythem.db`

### 2. Quản lý học sinh

**Thêm học sinh mới:**
1. Vào menu "Quản lý" → "Học sinh"
2. Click nút "Thêm mới"
3. Điền thông tin: Mã HS, Họ tên, Ngày sinh, Giới tính, SĐT phụ huynh
4. Click "Lưu"

**Sửa học sinh:**
1. Chọn học sinh trong bảng
2. Click "Sửa"
3. Cập nhật thông tin
4. Click "Lưu"

**Xóa học sinh:**
1. Chọn học sinh
2. Click "Xóa"
3. Xác nhận

### 3. Quản lý lớp học

**Tạo lớp mới:**
1. Vào "Quản lý" → "Lớp học"
2. Click "Thêm mới"
3. Nhập: Tên lớp, Môn học, Học phí/buổi, Lịch học
4. Click "Lưu"

**Thêm học sinh vào lớp:**
1. Chọn lớp
2. Click "Quản lý học sinh"
3. Chọn học sinh từ danh sách
4. Click "Thêm vào lớp"

### 4. Điểm danh buổi học

1. Vào "Quản lý" → "Buổi học"
2. Chọn lớp và ngày học
3. Click "Tạo buổi học mới"
4. Điểm danh từng học sinh: ✓ Có mặt / ✗ Vắng / ⏰ Muộn
5. Ghi chú nội dung bài học
6. Click "Lưu"

### 5. Xem báo cáo thu nhập

**Theo học sinh:**
1. Vào "Báo cáo" → "Thu nhập học sinh"
2. Chọn tháng/năm
3. Chọn học sinh
4. Xem chi tiết: Số buổi học, tổng tiền

**Theo tháng:**
1. Vào "Báo cáo" → "Thu nhập tháng"
2. Chọn tháng/năm
3. Xem tổng thu nhập toàn bộ

**Xuất báo cáo Excel:**
1. Trong màn hình báo cáo
2. Click "Xuất Excel"
3. Chọn nơi lưu file
4. File Excel sẽ được tạo

### 6. Backup & Restore

**Backup:**
1. Vào "Công cụ" → "Backup Database"
2. Chọn thư mục lưu backup
3. File backup: `daythem_backup_YYYYMMDD_HHMMSS.db`

**Restore:**
1. Vào "Công cụ" → "Restore Database"
2. Chọn file backup
3. Xác nhận restore
4. Khởi động lại ứng dụng

---

## 🐛 Troubleshooting

### Lỗi: "Không kết nối được database"
**Giải pháp:**
- Kiểm tra quyền ghi vào thư mục `%USERPROFILE%\QuanLyDayThem`
- Chạy ứng dụng với quyền Administrator (Windows)

### Lỗi: "java.lang.ClassNotFoundException: org.sqlite.JDBC"
**Giải pháp:**
- Build lại project: `mvn clean package`
- Đảm bảo SQLite JDBC driver có trong JAR

### Lỗi: "Không xuất được Excel"
**Giải pháp:**
- Đảm bảo thư viện Apache POI đã được thêm vào `pom.xml`
- Kiểm tra quyền ghi file tại thư mục đích

### Database bị lỗi
**Giải pháp:**
- Restore từ file backup
- Hoặc xóa file database, ứng dụng sẽ tạo lại

---

## 📝 TODO / Roadmap

- [ ] Thêm tính năng gửi SMS nhắc phụ huynh
- [ ] Tích hợp email gửi báo cáo tự động
- [ ] Thêm biểu đồ thống kê (Charts)
- [ ] Đồng bộ dữ liệu lên cloud (Google Drive)
- [ ] Multi-language support (English)
- [ ] Dark mode UI

---

## 👥 Đóng góp

Mọi đóng góp đều được hoan nghênh! Vui lòng:
1. Fork project
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

---

## 📄 License

MIT License - Tự do sử dụng cho mục đích cá nhân và thương mại.

---

## 📞 Liên hệ

- Email: your-email@example.com
- GitHub: https://github.com/your-username

---

## 🙏 Cảm ơn

Cảm ơn bạn đã sử dụng ứng dụng **Quản Lý Dạy Thêm**!

Nếu thấy hữu ích, hãy cho một ⭐ trên GitHub nhé! 😊
