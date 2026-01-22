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

    @Override
    public Student checkStudent(String InputEmail, String InputPassword) {
        String query = "SELECT * FROM student WHERE email = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setString(1, InputEmail);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                String outpw = rs.getString("password");
                if (BCrypt.checkpw(InputPassword, outpw)) {
                    return StudentMapper.toStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> listCourses() {
        String query = "SELECT * FROM course";
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

    @Override
    public List<Course> findCourse(String key) {
        String query = "SELECT * FROM course WHERE name ilike ?";
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

    @Override
    public List<EnrollmentDetailDTO> getHistory(int studentId) {
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

    @Override
    public boolean checkCourseExist(int id) {
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

    @Override
    public boolean checkEnrollmentExist(int studentID, int courseID) {
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


    @Override
    public boolean registerCourse(int studentID, int courseID) {
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

    @Override
    public List<Enrollment> listEnrollment(int studentID) {
        String query = "SELECT * FROM enrollment WHERE student_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentID);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int student_id = rs.getInt("student_id");
                int course_id = rs.getInt("course_id");
                Date registered_at = rs.getDate("registered_at");
                String status = rs.getString("status");
                enrollments.add(new Enrollment(id, student_id, course_id, registered_at, status));
            }
            return enrollments;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean cancelEnrollment(int enrollmentID) {
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

    @Override
    public boolean checkCheckCancelable(int studentID, int enrollmentID) {
        String query = "SELECT * FROM enrollment WHERE student_id = ? AND id = ?";
        Enrollment foundEnrollment = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentID);
            prest.setInt(2, enrollmentID);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int  student_id = rs.getInt("student_id");
                int course_id = rs.getInt("course_id");
                Date registered_at = rs.getDate("registered_at");
                String status = rs.getString("status");
                foundEnrollment = new Enrollment(id, student_id, course_id, registered_at, status);
            }
            if(foundEnrollment == null) return false;
            if(foundEnrollment.getStatus().equals("WAITING")) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean changePassword(int studentID, String InputPassword) {
        String query = "UPDATE student SET password = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setString(1, InputPassword);
            prest.setInt(2, studentID);
            int changed = prest.executeUpdate();
            return changed > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean verification(int studentId, String InputPassword, String InputEmail) {
        String query = "SELECT * FROM student WHERE id = ?";
        Student foundStudent = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, studentId);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                foundStudent = StudentMapper.toStudent(rs);
            }
            if(foundStudent.getEmail().equals(InputEmail) && foundStudent.getPassword().equals(InputPassword)) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
