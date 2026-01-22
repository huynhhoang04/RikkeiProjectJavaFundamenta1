package dao.impl;

import dao.IAdminDAO;
import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;
import util.CourseMapper;
import util.DBConection;
import util.StudentMapper;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class AdminDAOImpl implements IAdminDAO {


    @Override
    public Admin checkAdmin(String InputUsername, String InputPassword) {
        String query = "SELECT id, username, password FROM admin WHERE username=? AND password=?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setString(1, InputUsername);
            prest.setString(2, InputPassword);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String OutputUsername = rs.getString("username");
                String OutputPassword = rs.getString("password");
                return new Admin(id, OutputUsername, OutputPassword);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> listCourse() {
        String query = "SELECT * FROM course";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConection.getConnection();
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
    public boolean addCourse(String InputName, int InputDuration,  String InputInstructor) {
        String query = "INSERT INTO course(name, duration, instructor) VALUES (?,?,?)";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setString(1, InputName);
            prest.setInt(2, InputDuration);
            prest.setString(3, InputInstructor);
            int change =  prest.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCourseName(int courseId, String newName) {
        String query = "UPDATE course SET name = ? WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newName);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCourseDuration(int courseid, int newDuration) {
        String query = "UPDATE course SET duration = ? WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, newDuration);
            ps.setInt(2, courseid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCourseInstructor(int courseid, String newInstructor) {
        String query = "UPDATE course SET instructor = ? WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newInstructor);
            ps.setInt(2, courseid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Course> findCourse(String key) {
        String query = "SELECT * FROM course WHERE name ilike ?";
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            String searchformat = "%" + key + "%";
            prest.setString(1, searchformat);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int duration = rs.getInt("duration");
                String instructor = rs.getString("instructor");
                Date created_at = rs.getDate("created_at");
                courses.add(new Course(id, name, duration, instructor, created_at));
            }
            return courses;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkCourse(int courseId) {
        String query = "SELECT count(*) FROM course WHERE id=?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, courseId);
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
    public boolean deleteCourse(int courseId) {
        String query = "DELETE FROM course WHERE id=?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, courseId);
            int change = ps.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkCourseHasStudent(int courseId) {
        String query = "SELECT count(*) FROM enrollment WHERE course_id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Student> listStudent() {
        String query = "SELECT * FROM student";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                students.add(StudentMapper.toStudent(rs));
            }

            return students;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addStudent(String InputName, Date InputDOB, String InputEmail, Boolean InputGender, String InputPhoneNumber, String InputPassword) {
        String query = "INSERT INTO student(name, dob, email, gender, phone, password) VALUES (?,?,?,?::bit,?,?)";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setString(1, InputName);
            prest.setDate(2, new java.sql.Date(InputDOB.getTime()));
            prest.setString(3, InputEmail);
            prest.setInt(4, InputGender? 1:0);
            prest.setString(5, InputPhoneNumber);
            prest.setString(6, InputPassword);
            int change = prest.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String InputEmail) {
        String query = "SELECT count(*) AS cnt FROM student WHERE email = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)){
            prest.setString(1, InputEmail);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int cnt = rs.getInt("cnt");
                if (cnt > 0) {
                    return true;
                }
                return  false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean editStudent(int studentId, String fieldName, String newValue) {
        String column = "";
        String cast = "";
        switch (fieldName) {
            case "name": column = "name"; break;
            case "dob":
                column = "dob";
                cast = "::date";
                break;
            case "email": column = "email"; break;
            case "gender":
                column = "gender";
                cast = "::bit";
                break;
            case "phone": column = "phone"; break;
            case "password": column = "password"; break;
            default: return false;
        }
        String query = "UPDATE student SET " + column + " = ?" + cast +" WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newValue);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Student> findStudent(String key, String searchBy) {
        List<Student> students = new ArrayList<>();
        String column = "";
        if (searchBy.equalsIgnoreCase("name")) {
            column = "name";
        } else if (searchBy.equalsIgnoreCase("email")) {
            column = "email";
        } else {
            return students;
        }
        String query ="SELECT * FROM student WHERE " + column +" ilike ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            String str = "%"+key+"%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                students.add(StudentMapper.toStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM student WHERE id=?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            int change =  ps.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkStudentAttend(int studentId) {
        String query = "SELECT COUNT(*) FROM enrollment WHERE student_id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkStudentExists(int studentId) {
        String query = "SELECT count(*) FROM student WHERE id=?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, studentId);
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
    public List<EnrollmentDetailDTO> listStudentByCourse(int courseId) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
         String query = "SELECT e.id, s.name, c.name as course_name, e.registered_at, e.status " +
                 "FROM enrollment e " +
                 "JOIN student s ON e.student_id = s.id " +
                 "JOIN course c ON e.course_id = c.id " +
                 "WHERE e.course_id = ? AND e.status = 'CONFIRM'";
         try (Connection conn = DBConection.getConnection();
              PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, courseId);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                list.add(new  EnrollmentDetailDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getDate("registered_at"),
                        rs.getString("status")
                ));
            }
            return list;
         } catch (SQLException e) {
             System.err.println(e.getMessage());
             e.printStackTrace();
         }
         return null;
    }

    @Override
    public boolean clarifyEnrollment(int enrollmentId, String action) {
        String query = "UPDATE enrollment SET status = ?::course_status WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setString(1, action);
            prest.setInt(2, enrollmentId);
            int change = prest.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<EnrollmentDetailDTO> getPendingEnrollments(int enrollmentId) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
        String query = "SELECT e.id, s.name, c.name as course_name, e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.course_id = ? AND e.status = 'WAITING'";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, enrollmentId);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                list.add(new EnrollmentDetailDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getDate("registered_at"),
                        rs.getString("status")
                ));
            }
            return list;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        String query = "DELETE FROM enrollment WHERE id = ? and status = 'CONFIRM'";
        try (Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query)) {
            prest.setInt(1, enrollmentId);
            int change = prest.executeUpdate();
            return change > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String ,Integer> totalCourseAndStudent() {
        String query = "SELECT\n" +
                "(SELECT COUNT(id) FROM course) AS total_course,\n" +
                "    (SELECT COUNT(id) FROM student) AS total_student;";
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                map.put("courses", rs.getInt("total_course"));
                map.put("students", rs.getInt("total_student"));
            }
            return map;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String ,Integer> analyzeTotalStudentByCourse() {
        String query ="select c.name, count(e.student_id)\n" +
                "from course as c\n" +
                "left join enrollment e on c.id = e.course_id and e.status='CONFIRM'\n" +
                "group by c.id";
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query)) {
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("count"));
            }
            return map;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
