package dao.impl;

import dao.IStudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;
import uttil.CourseMapper;
import uttil.DBConection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDAOImpl implements IStudentDAO {
    @Override
    public Student checkStudent(String inemail, String inpassword) {
        String query = "select * from student where email = ? and password = ?";
        try(Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);) {

            prest.setString(1, inemail);
            prest.setString(2, inpassword);

            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                return util.StudentMapper.toStudent(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Course> listCourses() {
        String query = "select * from course";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            ResultSet rs = prest.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(CourseMapper.toCourse(rs));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findCourse(String key) {
        String query = "select * from course where name ilike ?";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            String str = "%" + key + "%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                courses.add(CourseMapper.toCourse(rs));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EnrollmentDetailDTO> getHistory(int studentId) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
        String query = "SELECT e.id, s.name, c.name, e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ?";

        try (Connection conn = DBConection.getConnection();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean checkCourseExist(int id) {
        String query = "select count(id) from course where id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean registerCourse(int studentID, int courseID) {
        String query = "insert into enrollment(student_id, course_id) values (?, ?)";
        try ( Connection conn = DBConection.getConnection();
              PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, studentID);
            prest.setInt(2, courseID);
            int rowInserted = prest.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> listEnrollment(int studentID) {
        String query = "select * from enrollment where student_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cancerEnrollment(int enrollmentID) {
        String query = "update enrollment set status = 'CANCER' where id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);)
        {
            prest.setInt(1, enrollmentID);
            int rowInserted = prest.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCheckCancerlation(int studentID, int enrollmentID) {
        String query = "select * from enrollment where student_id = ? and id = ?";
        Enrollment foundEnrollment = null;
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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

            if(foundEnrollment.getStatus().equals("WAITING")) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(int studentID, String password) {
        String query = "update student set password = ? where id = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, password);
            prest.setInt(2, studentID);
            int changed = prest.executeUpdate();
            if(changed == 1) {
                System.out.println("Thành công!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verification(int id, String password, String email) {
        String query = "select * from student where id = ?";
        Student foundStudent = null;
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                foundStudent = util.StudentMapper.toStudent(rs);
            }
            if(foundStudent.getEmail().equals(email) && foundStudent.getPassword().equals(password)) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
