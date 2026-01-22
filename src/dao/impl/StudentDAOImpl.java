package dao.impl;

import dao.IStudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;
import org.mindrot.jbcrypt.BCrypt;
import util.CourseMapper;
import util.DBConnection;
import util.StudentMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements IStudentDAO {

    //region authentication (đăng nhập)
    //hàm đăng nhập
    @Override
    public Student login(String InputEmail, String InputPassword) {
        // tìm sinh viên theo email
        String query = "SELECT id, password FROM student WHERE email = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setString(1, InputEmail);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                // so sánh mật khẩu hash
                String outpw = rs.getString("password");
                if (BCrypt.checkpw(InputPassword, outpw)) {
                    return new Student(id, outpw);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region course viewing (xem khóa học)
    // lấy toàn bộ khóa học
    @Override
    public List<Course> getAllCourses() {
        String query = "SELECT id, name, duration, instructor, created_at FROM course";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                courses.add(CourseMapper.toCourse(rs));
            }
            return courses;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // tìm kiếm gần đúng theo tên
    @Override
    public List<Course> searchCourses(String key) {
        String query = "SELECT id, name, duration, instructor, created_at FROM course WHERE name ilike ?";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            String str = "%" + key + "%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                courses.add(CourseMapper.toCourse(rs));
            }
            return courses;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // lấy khóa học có nhiều người học nhất để gợi ý
    @Override
    public Course getTopCourse() {
        String query = "SELECT c.*, COUNT(e.student_id) as sl " +
                "FROM course c " +
                "JOIN enrollment e ON c.id = e.course_id " +
                "WHERE e.status = 'CONFIRM' " +
                "GROUP BY c.id " +
                "ORDER BY sl DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {

            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                return CourseMapper.toCourse(rs);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // check khóa học tồn tại
    @Override
    public boolean existsCourseById(int id) {
        String query = "SELECT count(id) FROM course WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //endregion

    //region enrollment (đăng ký học)
    // lấy lịch sử đăng ký học hiển thị ra view
    @Override
    public List<EnrollmentDetailDTO> getEnrollmentHistory(int studentId) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
        String query = "SELECT e.id, s.name, c.name, e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new EnrollmentDetailDTO(
                            rs.getInt("id"),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDate("registered_at"),
                            rs.getString("status")
                    ));
                }
                return list;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // kiểm tra xem đã đăng ký chưa để chặn không cho dăng ký tiếp nếu status là đang đợi hoặc đã xác nhận
    @Override
    public boolean isAlreadyEnrolled(int studentID, int courseID) {
        String query = "SELECT count(id) FROM (SELECT id\n" +
                "    FROM enrollment\n" +
                "    WHERE student_id = ?\n" +
                "    AND course_id = ?\n" +
                "    INTERSECT\n" +
                "    SELECT id\n" +
                "    FROM enrollment\n" +
                "    WHERE status = 'CONFIRM'\n" +
                "    OR status = 'WAITING');";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentID);
            prest.setInt(2, courseID);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // tạo bản ghi đăng ký mới với status waiting và ngày hiện tại
    @Override
    public boolean createEnrollment(int studentID, int courseID) {
        String query = "INSERT INTO enrollment(student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentID);
            prest.setInt(2, courseID);
            int rowInserted = prest.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // xóa đơn đăng ký khỏi hệ thống
    @Override
    public boolean cancelEnrollmentRequest(int enrollmentID) {
        String query = "UPDATE enrollment SET status = 'CANCEL' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query))
        {
            prest.setInt(1, enrollmentID);
            int rowInserted = prest.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //kiểm tra phiếu đăng ký có hủy được không
    @Override
    public boolean isEnrollmentCancellable(int studentID, int enrollmentID) {
        // chỉ cho phép hủy nếu đơn đang ở trạng thái waiting
        String query = "SELECT count(*) FROM enrollment WHERE id = ? AND student_id = ? AND status = 'WAITING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentID);
            prest.setInt(2, enrollmentID);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                return  rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //endregion

    //region account management (quản lý tài khoản)
    // mã hóa mật khẩu mới và cập nhật
    @Override
    public boolean updatePassword(int studentID, String InputPassword) {
        //hash mật khẩu nè
        String hashpw = BCrypt.hashpw(InputPassword, BCrypt.gensalt());
        String query = "UPDATE student SET password = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setString(1, hashpw);
            prest.setInt(2, studentID);
            int changed = prest.executeUpdate();
            return changed > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //xác thực mật khẩu cũ dựa trên thông tin trước khi cập nhật mật khẩu
    @Override
    public boolean verifyCredentials(int studentId, String InputPassword, String InputEmail) {
        // xác thực lại mật khẩu cũ trước khi đổi pass
        String query = "SELECT password FROM student WHERE id = ? AND email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, studentId);
            prest.setString(2, InputEmail);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                String password = rs.getString(1);
                return BCrypt.checkpw(InputPassword, password);
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //endregion
}
