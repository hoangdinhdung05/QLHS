-- ===============================================
-- DATABASE SCHEMA FOR QUẢN LÝ DẠY THÊM
-- SQLite Database
-- ===============================================

-- Bảng học sinh
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(20),
    address TEXT,
    parent_name VARCHAR(100),
    parent_phone VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng lớp học
CREATE TABLE IF NOT EXISTS classes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    fee_per_session DECIMAL(10, 2) NOT NULL, -- Học phí mỗi buổi
    schedule VARCHAR(200), -- Lịch học (VD: "Thứ 2, 4, 6 - 18:00-19:30")
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng học sinh trong lớp (nhiều-nhiều)
CREATE TABLE IF NOT EXISTS class_students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    joined_date DATE NOT NULL,
    left_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, LEFT
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE(class_id, student_id)
);

-- Bảng buổi học
CREATE TABLE IF NOT EXISTS lesson_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    class_id INTEGER NOT NULL,
    lesson_date DATE NOT NULL,
    lesson_time VARCHAR(50), -- VD: "18:00-19:30"
    lesson_topic VARCHAR(200),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);

-- Bảng điểm danh
CREATE TABLE IF NOT EXISTS attendances (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_session_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'PRESENT', -- PRESENT, ABSENT, LATE
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lesson_session_id) REFERENCES lesson_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE(lesson_session_id, student_id)
);

-- Bảng thanh toán (để tracking chi tiết hơn)
CREATE TABLE IF NOT EXISTS payments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    class_id INTEGER NOT NULL,
    month INTEGER NOT NULL, -- Tháng (1-12)
    year INTEGER NOT NULL, -- Năm
    total_sessions INTEGER DEFAULT 0, -- Tổng số buổi có mặt
    total_amount DECIMAL(10, 2) DEFAULT 0, -- Tổng tiền
    paid_amount DECIMAL(10, 2) DEFAULT 0, -- Đã thanh toán
    payment_status VARCHAR(20) DEFAULT 'UNPAID', -- UNPAID, PARTIAL, PAID
    payment_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    UNIQUE(student_id, class_id, month, year)
);

-- ===============================================
-- INDEXES for performance
-- ===============================================
CREATE INDEX IF NOT EXISTS idx_students_code ON students(student_code);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);
CREATE INDEX IF NOT EXISTS idx_classes_code ON classes(class_code);
CREATE INDEX IF NOT EXISTS idx_classes_status ON classes(status);
CREATE INDEX IF NOT EXISTS idx_lesson_sessions_date ON lesson_sessions(lesson_date);
CREATE INDEX IF NOT EXISTS idx_lesson_sessions_class ON lesson_sessions(class_id);
CREATE INDEX IF NOT EXISTS idx_attendances_lesson ON attendances(lesson_session_id);
CREATE INDEX IF NOT EXISTS idx_attendances_student ON attendances(student_id);
CREATE INDEX IF NOT EXISTS idx_payments_student ON payments(student_id);
CREATE INDEX IF NOT EXISTS idx_payments_month_year ON payments(month, year);

-- ===============================================
-- VIEWS for reporting
-- ===============================================

-- View: Tổng thu nhập theo học sinh trong tháng
CREATE VIEW IF NOT EXISTS v_student_monthly_income AS
SELECT 
    s.id as student_id,
    s.student_code,
    s.full_name,
    s.parent_phone,
    c.id as class_id,
    c.class_name,
    c.subject,
    c.fee_per_session,
    strftime('%Y', ls.lesson_date) as year,
    strftime('%m', ls.lesson_date) as month,
    COUNT(a.id) as total_sessions,
    (COUNT(a.id) * c.fee_per_session) as total_income
FROM students s
JOIN class_students cs ON s.id = cs.student_id
JOIN classes c ON cs.class_id = c.id
JOIN lesson_sessions ls ON c.id = ls.class_id
JOIN attendances a ON ls.id = a.lesson_session_id AND a.student_id = s.id
WHERE a.status = 'PRESENT'
GROUP BY s.id, c.id, year, month;

-- View: Tổng thu nhập toàn bộ theo tháng
CREATE VIEW IF NOT EXISTS v_total_monthly_income AS
SELECT 
    strftime('%Y', ls.lesson_date) as year,
    strftime('%m', ls.lesson_date) as month,
    COUNT(DISTINCT ls.id) as total_lessons,
    COUNT(a.id) as total_attendances,
    SUM(c.fee_per_session) as total_income
FROM lesson_sessions ls
JOIN classes c ON ls.class_id = c.id
JOIN attendances a ON ls.id = a.lesson_session_id
WHERE a.status = 'PRESENT'
GROUP BY year, month;

-- ===============================================
-- SAMPLE DATA (for testing)
-- ===============================================

-- Sample students
INSERT OR IGNORE INTO students (student_code, full_name, date_of_birth, gender, phone, parent_name, parent_phone, status)
VALUES 
('HS001', 'Nguyễn Văn An', '2010-05-15', 'Nam', '0123456789', 'Nguyễn Văn A', '0987654321', 'ACTIVE'),
('HS002', 'Trần Thị Bình', '2011-08-20', 'Nữ', '0123456788', 'Trần Văn B', '0987654322', 'ACTIVE'),
('HS003', 'Lê Hoàng Cường', '2010-12-10', 'Nam', '0123456787', 'Lê Văn C', '0987654323', 'ACTIVE');

-- Sample classes
INSERT OR IGNORE INTO classes (class_code, class_name, subject, fee_per_session, schedule, status)
VALUES 
('LH001', 'Toán 8A', 'Toán', 100000, 'Thứ 2, 4, 6 - 18:00-19:30', 'ACTIVE'),
('LH002', 'Lý 9B', 'Vật Lý', 120000, 'Thứ 3, 5, 7 - 19:00-20:30', 'ACTIVE');

-- Sample class_students
INSERT OR IGNORE INTO class_students (class_id, student_id, joined_date, status)
VALUES 
(1, 1, '2024-01-15', 'ACTIVE'),
(1, 2, '2024-01-15', 'ACTIVE'),
(2, 1, '2024-01-20', 'ACTIVE'),
(2, 3, '2024-01-20', 'ACTIVE');
