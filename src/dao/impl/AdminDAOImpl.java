package dao.impl;

import dao.IAdminDAO;
import model.Admin;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;
import uttil.CourseMapper;
import uttil.DBConection;
import uttil.StudentMapper;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class AdminDAOImpl implements IAdminDAO {
    @Override
    public void showByStudentID(int id) {
        String sql = "select * from student where id = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(sql);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int student_id = rs.getInt("id");
                String student_name = rs.getString("name");
                Date student_dob = rs.getDate("dob");
                String student_email = rs.getString("email");
                Boolean student_gender = rs.getBoolean("gender");
                String strGender = student_gender? "Nam" : "Nữ";
                String student_phone = rs.getString("phone");
                String student_password = rs.getString("password");
                Date student_created_at = rs.getDate("created_at");

                System.out.println("Thêm học viên thành công!");
                System.out.println(new Student(student_id, student_name, student_dob, student_email, strGender, student_phone, student_password, student_created_at));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //region auth
    @Override
    public Admin checkAdmin(String username, String password) {
        String query = "select * from admin where username=? and password=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setString(1, username);
            prest.setString(2, password);

            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                String reusername = rs.getString("username");
                int reid = rs.getInt("id");
                String repassword = rs.getString("password");

                return new Admin(reid, reusername,  repassword);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    //endregion

    //region : courseDAO
    @Override
    public List<Course> listCourse() {
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
    public boolean addCourse(String name, int duration,  String instructor) {
        String query = "insert into course(name, duration, instructor) values(?,?,?)";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {

            prest.setString(1, name);
            prest.setInt(2, duration);
            prest.setString(3, instructor);

            int change =  prest.executeUpdate();
            return change > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 1. Hàm sửa Tên (Nhận String)
    @Override
    public boolean updateCourseName(int id, String newName) {
        String query = "UPDATE course SET name = ? WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, newName);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0; // Trả về true nếu update thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Hàm sửa Thời gian (Nhận INT -> Chuẩn kiểu dữ liệu)
    @Override
    public boolean updateCourseDuration(int id, int newDuration) {
        String query = "UPDATE course SET duration = ? WHERE id = ?"; // Không cần ::int nữa
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, newDuration); // Dùng setInt chuẩn chỉ
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Hàm sửa Giảng viên (Nhận String)
    @Override
    public boolean updateCourseInstructor(int id, String newInstructor) {
        String query = "UPDATE course SET instructor = ? WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, newInstructor);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Course> findCourse(String key) {
        String query = "select * from course where name ilike ?";
        List<Course> courses = new ArrayList<>();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCourse(int id) {
        String query = "select count(*) from course where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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
    public boolean deleteCourse(int id) {
        String query = "delete from course where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            int change = ps.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCourseHasStudent(int courseId) {
        String query = "SELECT COUNT(*) FROM enrollment WHERE course_id = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //endregion

    //region: studentDAO
    @Override
    public List<Student> listStudent() {
        String query = "select * from student";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                students.add(StudentMapper.toStudent(rs));
            }

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addStudent(String name, Date dob, String email, Boolean gender, String phone, String password) {
        String query = "insert into student(name, dob, email, gender, phone, password) values (?,?,?,?::bit,?,?)";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, name);
            prest.setDate(2, new java.sql.Date(dob.getTime()));
            prest.setString(3, email);
            prest.setInt(4, gender? 1:0);
            prest.setString(5, phone);
            prest.setString(6, password);
            int change = prest.executeUpdate();
            return change > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        String query = "select count(*) as cnt from student where email = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);){
            prest.setString(1, email);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int cnt = rs.getInt("cnt");

                if (cnt > 0) {
                    return true;
                }
                return  false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean editStudent(int id, String fieldName, String newValue) {
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
        String query = "update student set " + column + " = ?" + cast +" where id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, newValue);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
            // Nếu truyền lung tung -> Trả về rỗng hoặc báo lỗi luôn
            System.err.println("❌ Tên cột tìm kiếm không hợp lệ!");
            return students;
        }
        String query ="select * from student where " + column +" ilike ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            String str = "%"+key+"%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                students.add(StudentMapper.toStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteStudent(int id) {
        String query = "delete from student where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            int change =  ps.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkStudentAttend(int id) {
        String query = "SELECT COUNT(*) FROM enrollment WHERE student_id = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkStudentExists(int id) {
        String query = "select count(*) from student where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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


    //endregion

    //region Enrollment
    @Override
    public List<EnrollmentDetailDTO> listStudentByCourse(int id) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
         String query = "SELECT e.id, s.name, c.name as course_name, e.registered_at, e.status " +
                 "FROM enrollment e " +
                 "JOIN student s ON e.student_id = s.id " +
                 "JOIN course c ON e.course_id = c.id " +
                 "WHERE e.course_id = ?";
         try (Connection conn = DBConection.getConnection();
              PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                list.add(new  EnrollmentDetailDTO(
                        rs.getInt("id"),
                        rs.getString("name"), // Tên sinh viên
                        rs.getString("course_name"),
                        rs.getDate("registered_at"),
                        rs.getString("status")
                ));
            }
            return list;
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public boolean clarifyEnrollment(int id, String action) {
        String query = "UPDATE enrollment SET status = ?::course_status WHERE id = ?";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setString(1, action);
            prest.setInt(2, id);
            int change = prest.executeUpdate();
            return change > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EnrollmentDetailDTO> getPendingEnrollments(int id) {
        List<EnrollmentDetailDTO> list = new ArrayList<>();
        String query = "SELECT e.id, s.name, c.name as course_name, e.registered_at, e.status " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.course_id = ? AND e.status = 'WAITING'";
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, id);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteEnrollment(int id) {
        String query = "DELETE FROM enrollment WHERE id = ? and status = 'CONFIRM'";
        try (Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);) {
            prest.setInt(1, id);
            int change = prest.executeUpdate();
            return change > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //endregion

    //region analyze
    @Override
    public Map<String ,Integer> totalCourseAndStudent() {
        String query = "SELECT\n" +
                "(SELECT COUNT(id) FROM course) AS total_course,\n" +
                "    (SELECT COUNT(id) FROM student) AS total_student;";
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = DBConection.getConnection();
             PreparedStatement prest = conn.prepareStatement(query);) {
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                map.put("courses", rs.getInt("total_course"));
                map.put("students", rs.getInt("total_student"));
            }
            return map;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String ,Integer> analyzeTotalStudentByCourse() {
        String query ="select c.name, count(e.student_id)\n" +
                "from course as c\n" +
                "left join enrollment e on c.id = e.course_id and e.status='CONFIRM'\n" +
                "group by c.id";
        Map<String, Integer> map = new HashMap<>();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("name"), rs.getInt("count"));
            }
            return map;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //endregion
}
