package dao.impl;

import dao.IAdminDAO;
import model.Admin;
import model.Course;
import model.Enrollment;
import model.Student;
import uttil.DBConection;

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
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int duration = rs.getInt("duration");
                String instructor = rs.getString("instructor");
                Date  created_at = rs.getDate("created_at");

                courses.add(new Course(id, name, duration, instructor, created_at));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCourse(String name, int duration,  String instructor) {
        String query = "insert into course(name, duration, instructor) values(?,?,?)";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            prest.setString(1, name);
            prest.setInt(2, duration);
            prest.setString(3, instructor);

            prest.executeUpdate();
            ResultSet rs = prest.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("================================");
                System.out.println("Thêm khóa học thành công");
                System.out.println("Thông tin khóa học :");
                PreparedStatement returnCourse =  conn.prepareStatement("select * from course where id=?");
                returnCourse.setInt(1, id);
                ResultSet rs1 = returnCourse.executeQuery();
                if (rs1.next()) {
                    int course_id = rs1.getInt("id");
                    String course_name = rs1.getString("name");
                    int course_duration = rs1.getInt("duration");
                    String course_instructor = rs1.getString("instructor");
                    Date course_created_at = rs1.getDate("created_at");

                    System.out.println(new Course(course_id, course_name, course_duration, course_instructor, course_created_at));
                    System.out.println("================================");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editCourse(int id, String editPart,String editContent) {
        String query = new String();
        if(editPart.equals("name")){
            query = "update course set name=? where id=?";
        }
        if(editPart.equals("duration")){
            query = "update course set duration=?::int where id=?";
        }
        if(editPart.equals("instructor")){
            query = "update course set instructor=? where id=?";
        }

        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setString(1, editContent);
            prest.setInt(2, id);
            prest.executeUpdate();
            System.out.println("Update thành công!");
            PreparedStatement prest2 = conn.prepareStatement("select * from course where id=?");
            prest2.setInt(1, id);
            ResultSet rs = prest2.executeQuery();
            if (rs.next()) {
                int course_id = rs.getInt("id");
                String course_name = rs.getString("name");
                int course_duration = rs.getInt("duration");
                String course_instructor = rs.getString("instructor");
                Date course_created_at = rs.getDate("created_at");
                System.out.println(new Course(course_id,course_name, course_duration, course_instructor, course_created_at));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public List<Course> sortListCourse(List<Course> courses , String sortBy) {
        if(sortBy.equals("name")){
            courses.sort((c1,c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        }

        if(sortBy.equals("duration")){
            courses.sort((c1,c2) -> c1.getDuration() - c2.getDuration());
        }

        return courses;
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
    public void deleteCourse(int id) {
        String query = "delete from course where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Thành công!");
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
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date date_of_birth = rs.getDate("dob");
                String email = rs.getString("email");
                Boolean gender = rs.getBoolean("gender");
                String strGender = gender? "Nam" : "Nữ";
                String phoneNumber = rs.getString("phone");
                String password = rs.getString("password");
                Date created_at = rs.getDate("created_at");

                students.add(new Student(id, name, date_of_birth, email, strGender, phoneNumber, password, created_at));
            }

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStudent(String name, Date dob, String email, Boolean gender, String phone, String password) {
        String query = "insert into student(name, dob, email, gender, phone, password) values (?,?,?,?::bit,?,?)";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            prest.setString(1, name);
            prest.setDate(2, new java.sql.Date(dob.getTime()));
            prest.setString(3, email);
            prest.setInt(4, gender? 1:0);
            prest.setString(5, phone);
            prest.setString(6, password);
            prest.executeUpdate();

            ResultSet rs = prest.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                showByStudentID(id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        String query = "select count(*) as cnt from student where email = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
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
    public void editStudent(int id, String editPart, String editContent) {
        String query = new String();
        if (editPart.equals("name")) {
            query = "update student set name=? where id=?";
        }
        if (editPart.equals("dob")) {
            query = "update student set dob=?::date where id=?";
        }
        if (editPart.equals("email")) {
            query = "update student set email=? where id=?";
        }
        if (editPart.equals("gender")) {
            query = "update student set gender=?::bit where id=?";
        }
        if (editPart.equals("phone")) {
            query = "update student set phone=? where id=?";
        }
        if(editPart.equals("password")) {
            query = "update student set password=? where id=?";
        }

        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, editContent);
            prest.setInt(2, id);
            prest.executeUpdate();

            showByStudentID(id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findStudent(String key, String searchBy) {
        String query = new  String();
        List<Student> students = new ArrayList<>();
        if (searchBy.equals("name")) {
            query = "select * from student where name ilike ?";
        }
        if (searchBy.equals("email")) {
            query = "select * from student where email ilike ?";
        }

        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            String str = "%"+key+"%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date date_of_birth = rs.getDate("dob");
                String email = rs.getString("email");
                Boolean gender = rs.getBoolean("gender");
                String strGender = gender? "Nam" : "Nữ";
                String phoneNumber = rs.getString("phone");
                String password = rs.getString("password");
                Date created_at = rs.getDate("created_at");

                students.add(new Student(id, name, date_of_birth, email, strGender, phoneNumber, password, created_at));
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> sortListStudent(List<Student> students, String sortBy) {
        List<Student> studentList = listStudent();
        if(sortBy.equals("name")) {
            studentList.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
        }
        if(sortBy.equals("email")) {
            studentList.sort((s1, s2) -> s1.getEmail().compareTo(s2.getEmail()));
        }
        return studentList;
    }

    @Override
    public void deleteStudent(int id) {
        String query = "delete from student where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Thành công!");
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
    public void listStudentByCourse(int id) {
         String query = "select distinct student_id, status from enrollment where course_id=?";
         try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int student_id = rs.getInt("student_id");
                String status = rs.getString("status");

                Student stu = findStudent(student_id);
                System.out.println(stu.getId() + "|" + stu.getName() + "|" + stu.getDateOfBirth() + "|" + stu.getEmail() + "|" + stu.getGender() + "|" + stu.getPhoneNumber() + "|" + status);
            }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public Student findStudent(int id) {
        String query = "select * from student where id=?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int student_id = rs.getInt("id");
                String name = rs.getString("name");
                Date date_of_birth = rs.getDate("dob");
                String email = rs.getString("email");
                Boolean gender = rs.getBoolean("gender");
                String strGender = gender? "Nam" : "Nữ";
                String phoneNumber = rs.getString("phone");

                return new Student(student_id, name, email, date_of_birth, strGender, phoneNumber);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }

    @Override
    public void clarifyEnrollment(int id, String action) {
        String query = new  String();
        if(action.equals("Confirm")) {
            query = "update enrollment set status='CONFIRM' where id=?";
        }
        if(action.equals("Denied")) {
            query = "update enrollment set status='DENIED' where id=?";
        }
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            prest.executeUpdate();
            System.out.println("Thành công!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> unclarifyEnrollment(int id) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "select * from enrollment where course_id=? and status='WAITING'";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int rsid = rs.getInt("id");
                int student_id = rs.getInt("student_id");
                int course_id = rs.getInt("course_id");
                java.sql.Date registered_at = rs.getDate("registered_at");
                String status = rs.getString("status");

                System.out.println(rsid);
                enrollments.add(new Enrollment(rsid, student_id, course_id, registered_at, status));
            }
            return enrollments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkEnrollment(int id, int course_id) {
        String query = "select count(*) from enrollment where id=? and course_id=? and  status='WAITING'";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            prest.setInt(2, course_id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return (count > 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    //endregion

    //region analyze
    @Override
    public void totalCourseAndStudent() {
        String query = "SELECT\n" +
                "(SELECT COUNT(id) FROM course) AS total_course,\n" +
                "    (SELECT COUNT(id) FROM student) AS total_student;";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int total_course = rs.getInt("total_course");
                int total_student = rs.getInt("total_student");
                System.out.println("Tổng khóa học : " + total_course);
                System.out.println("Tổng học viên : "  + total_student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void analyzeTotalStudentByCourse() {
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
                String name = rs.getString("name");
                int count = rs.getInt("count");
                map.put(name, count);
            }

            map.forEach((k,v)->{
                System.out.println("Tên khóa : " + k + " Số học viên : " + v);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Top5CourseWithMostStudents() {
        String query = "select c.name, count(e.student_id)\n" +
                "from course as c\n" +
                "left join enrollment e on c.id = e.course_id and e.status='CONFIRM'\n" +
                "group by c.id\n" +
                "order by count(e.student_id) desc, c.name asc\n" +
                "limit 5";

        Map<String, Integer> map = new HashMap<>();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                int count = rs.getInt("count");
                map.put(name, count);
            }

            map.forEach((k,v)->{
                System.out.println("Tên khóa : " + k + " Số học viên : " + v);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void CourseWithMoreThan10Students() {
        String query = "select c.name, count(e.student_id)\n" +
                "from course as c\n" +
                "left join enrollment e on c.id = e.course_id and e.status='CONFIRM'\n" +
                "group by c.id\n" +
                "having count(e.student_id) >= 10\n" +
                "order by count(e.student_id) desc, c.name asc";

        Map<String, Integer> map = new HashMap<>();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                int count = rs.getInt("count");
                map.put(name, count);
            }

            map.forEach((k,v)->{
                System.out.println("Tên khóa : " + k + " Số học viên : " + v);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //endregion
}
