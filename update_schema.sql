-- Script để update database cho phiên bản mới
-- Chạy script này để thêm bảng teachers và cập nhật bảng classes

-- Tạo bảng giáo viên
CREATE TABLE IF NOT EXISTS teachers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    teacher_code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    specialization VARCHAR(200), -- Chuyên môn (Toán, Lý, Hóa, Anh...)
    qualification VARCHAR(100), -- Trình độ (Cử nhân, Thạc sĩ, Tiến sĩ...)
    years_of_experience INTEGER, -- Số năm kinh nghiệm
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm các cột mới vào bảng classes nếu chưa có
-- SQLite không hỗ trợ ALTER TABLE ADD COLUMN IF NOT EXISTS, 
-- nên cần kiểm tra thủ công hoặc tạo lại bảng

-- Backup bảng classes hiện tại
CREATE TABLE IF NOT EXISTS classes_backup AS SELECT * FROM classes;

-- Xóa bảng classes cũ
DROP TABLE IF EXISTS classes;

-- Tạo lại bảng classes với cấu trúc mới
CREATE TABLE classes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    teacher_name VARCHAR(100), -- Tên giáo viên
    location VARCHAR(200), -- Địa điểm dạy
    time_slot VARCHAR(50), -- Khung giờ (VD: "18:00-20:00")
    day_of_week VARCHAR(50), -- Thứ trong tuần (VD: "2,4,6")
    max_students INTEGER DEFAULT 10, -- Số lượng học sinh tối đa
    current_students INTEGER DEFAULT 0, -- Số học sinh hiện tại
    fee_per_session DECIMAL(10, 2) NOT NULL, -- Học phí mỗi buổi
    schedule VARCHAR(200), -- Lịch học tổng quan
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, FULL
    class_type VARCHAR(20) DEFAULT 'GROUP', -- INDIVIDUAL, SMALL_GROUP, GROUP
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Khôi phục dữ liệu từ backup
INSERT INTO classes (id, class_code, class_name, subject, fee_per_session, schedule, status, notes, created_at, updated_at)
SELECT id, class_code, class_name, subject, fee_per_session, schedule, status, notes, created_at, updated_at
FROM classes_backup;

-- Xóa bảng backup
DROP TABLE classes_backup;

-- Tạo indexes
CREATE INDEX IF NOT EXISTS idx_teachers_code ON teachers(teacher_code);
CREATE INDEX IF NOT EXISTS idx_teachers_status ON teachers(status);
CREATE INDEX IF NOT EXISTS idx_classes_type ON classes(class_type);

-- Insert dữ liệu mẫu cho giáo viên
INSERT INTO teachers (teacher_code, full_name, gender, phone, email, specialization, qualification, years_of_experience, status)
VALUES 
('GV001', 'Nguyen Van A', 'Nam', '0901234567', 'gv001@email.com', 'Toan, Ly', 'Thac si', 5, 'ACTIVE'),
('GV002', 'Tran Thi B', 'Nu', '0907654321', 'gv002@email.com', 'Hoa, Sinh', 'Cu nhan', 3, 'ACTIVE'),
('GV003', 'Le Van C', 'Nam', '0912345678', 'gv003@email.com', 'Tieng Anh', 'Thac si', 7, 'ACTIVE');

COMMIT;
