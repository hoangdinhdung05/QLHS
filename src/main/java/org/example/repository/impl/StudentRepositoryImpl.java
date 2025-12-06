package org.example.repository.impl;

import org.example.config.DatabaseConfig;
import org.example.entity.Student;
import org.example.repository.StudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của StudentRepository sử dụng JDBC
 */
public class StudentRepositoryImpl implements StudentRepository {

    private Connection connection;

    public StudentRepositoryImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    private Connection getConnection() {
        return DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public Student save(Student student) {
        String sql = """
            INSERT INTO students (student_code, full_name, date_of_birth, gender, phone, 
                                 address, parent_name, parent_phone, status, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setDate(3, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getAddress());
            stmt.setString(7, student.getParentName());
            stmt.setString(8, student.getParentPhone());
            stmt.setString(9, student.getStatus() != null ? student.getStatus() : "ACTIVE");
            stmt.setString(10, student.getNotes());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // SQLite không hỗ trợ getGeneratedKeys(), dùng last_insert_rowid()
                try (PreparedStatement idStmt = getConnection().prepareStatement("SELECT last_insert_rowid()");
                     ResultSet rs = idStmt.executeQuery()) {
                    if (rs.next()) {
                        student.setId(rs.getLong(1));
                    }
                }
            }

            return student;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm học sinh: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(Student student) {
        String sql = """
            UPDATE students 
            SET student_code = ?, full_name = ?, date_of_birth = ?, gender = ?, 
                phone = ?, address = ?, parent_name = ?, parent_phone = ?, 
                status = ?, notes = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getFullName());
            stmt.setDate(3, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getAddress());
            stmt.setString(7, student.getParentName());
            stmt.setString(8, student.getParentPhone());
            stmt.setString(9, student.getStatus());
            stmt.setString(10, student.getNotes());
            stmt.setLong(11, student.getId());

            stmt.executeUpdate();
            return student;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật học sinh: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa học sinh: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm học sinh: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Student> findByStudentCode(String studentCode) {
        String sql = "SELECT * FROM students WHERE student_code = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm học sinh theo mã: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY created_at DESC";
        List<Student> students = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách học sinh: " + e.getMessage(), e);
        }

        return students;
    }

    @Override
    public List<Student> findByStatus(String status) {
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY full_name";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm học sinh theo trạng thái: " + e.getMessage(), e);
        }

        return students;
    }

    @Override
    public List<Student> searchByName(String keyword) {
        String sql = "SELECT * FROM students WHERE full_name LIKE ? OR student_code LIKE ? ORDER BY full_name";
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm học sinh: " + e.getMessage(), e);
        }

        return students;
    }

    @Override
    public List<Student> findByClassId(Long classId) {
        String sql = """
            SELECT s.* FROM students s
            JOIN class_students cs ON s.id = cs.student_id
            WHERE cs.class_id = ? AND cs.status = 'ACTIVE'
            ORDER BY s.full_name
            """;
        List<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, classId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm học sinh theo lớp: " + e.getMessage(), e);
        }

        return students;
    }

    /**
     * Map ResultSet sang Student object
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setFullName(rs.getString("full_name"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            student.setDateOfBirth(dob.toLocalDate());
        }
        
        student.setGender(rs.getString("gender"));
        student.setPhone(rs.getString("phone"));
        student.setAddress(rs.getString("address"));
        student.setParentName(rs.getString("parent_name"));
        student.setParentPhone(rs.getString("parent_phone"));
        student.setStatus(rs.getString("status"));
        student.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            student.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return student;
    }
}

