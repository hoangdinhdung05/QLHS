package org.example.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseHelper - Quản lý kết nối SQLite và khởi tạo database
 * Singleton pattern để đảm bảo chỉ có 1 instance
 */
public class DatabaseConfig {

    private static final String DB_DIR = System.getProperty("user.home") + File.separator + "QuanLyDayThem";
    private static final String DB_NAME = "daythem.db";
    private static final String DB_PATH = DB_DIR + File.separator + DB_NAME;
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    
    private static DatabaseConfig instance;
    private Connection connection;

    private DatabaseConfig() {
        try {
            // Tạo thư mục chứa database nếu chưa có
            File dbDirectory = new File(DB_DIR);
            if (!dbDirectory.exists()) {
                dbDirectory.mkdirs();
            }

            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Khởi tạo connection
            connection = DriverManager.getConnection(DB_URL);
            
            // Enable foreign keys
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            
            // Khởi tạo tables
            initializeTables();
            
            System.out.println("✓ Kết nối SQLite thành công: " + DB_PATH);
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy SQLite JDBC driver", e);
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kết nối đến database", e);
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(DB_URL);
                try (Statement stmt = this.connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Không thể lấy connection", e);
        }
        return this.connection;
    }

    /**
     * Khởi tạo tất cả các bảng trong database
     */
    private void initializeTables() {
        String[] createTableQueries = {
            // Bảng accounts
            """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                role VARCHAR(20) NOT NULL,
                is_active BOOLEAN DEFAULT 1,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Bảng students
            """
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
                status VARCHAR(20) DEFAULT 'ACTIVE',
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            // Bảng classes
            """
            CREATE TABLE IF NOT EXISTS classes (
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
            )
            """,
            
            // Bảng class_students
            """
            CREATE TABLE IF NOT EXISTS class_students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                class_id INTEGER NOT NULL,
                student_id INTEGER NOT NULL,
                joined_date DATE NOT NULL,
                left_date DATE,
                status VARCHAR(20) DEFAULT 'ACTIVE',
                FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
                FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                UNIQUE(class_id, student_id)
            )
            """,
            
            // Bảng lesson_sessions
            """
            CREATE TABLE IF NOT EXISTS lesson_sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                class_id INTEGER NOT NULL,
                lesson_date DATE NOT NULL,
                lesson_time VARCHAR(50),
                lesson_topic VARCHAR(200),
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
            )
            """,
            
            // Bảng attendances
            """
            CREATE TABLE IF NOT EXISTS attendances (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                lesson_session_id INTEGER NOT NULL,
                student_id INTEGER NOT NULL,
                status VARCHAR(20) DEFAULT 'PRESENT',
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (lesson_session_id) REFERENCES lesson_sessions(id) ON DELETE CASCADE,
                FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                UNIQUE(lesson_session_id, student_id)
            )
            """,
            
            // Bảng payments
            """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER NOT NULL,
                class_id INTEGER NOT NULL,
                month INTEGER NOT NULL,
                year INTEGER NOT NULL,
                total_sessions INTEGER DEFAULT 0,
                total_amount DECIMAL(10, 2) DEFAULT 0,
                paid_amount DECIMAL(10, 2) DEFAULT 0,
                payment_status VARCHAR(20) DEFAULT 'UNPAID',
                payment_date DATE,
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
                UNIQUE(student_id, class_id, month, year)
            )
            """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
            
            // Tạo indexes
            createIndexes(stmt);
            
            System.out.println("✓ Khởi tạo tables thành công");
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khởi tạo tables", e);
        }
    }

    private void createIndexes(Statement stmt) throws SQLException {
        String[] indexes = {
            "CREATE INDEX IF NOT EXISTS idx_students_code ON students(student_code)",
            "CREATE INDEX IF NOT EXISTS idx_students_status ON students(status)",
            "CREATE INDEX IF NOT EXISTS idx_classes_code ON classes(class_code)",
            "CREATE INDEX IF NOT EXISTS idx_classes_status ON classes(status)",
            "CREATE INDEX IF NOT EXISTS idx_lesson_sessions_date ON lesson_sessions(lesson_date)",
            "CREATE INDEX IF NOT EXISTS idx_lesson_sessions_class ON lesson_sessions(class_id)",
            "CREATE INDEX IF NOT EXISTS idx_attendances_lesson ON attendances(lesson_session_id)",
            "CREATE INDEX IF NOT EXISTS idx_attendances_student ON attendances(student_id)",
            "CREATE INDEX IF NOT EXISTS idx_payments_student ON payments(student_id)",
            "CREATE INDEX IF NOT EXISTS idx_payments_month_year ON payments(month, year)"
        };

        for (String index : indexes) {
            stmt.execute(index);
        }
    }

    /**
     * Đóng connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy đường dẫn đến file database
     */
    public static String getDatabasePath() {
        return DB_PATH;
    }

    /**
     * Backup database
     */
    public static boolean backupDatabase(String targetPath) {
        try {
            File source = new File(DB_PATH);
            File target = new File(targetPath);
            
            if (!source.exists()) {
                return false;
            }
            
            Files.copy(source.toPath(), target.toPath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
