package dao.impl;

import dao.IStudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import uttil.DBConection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDAOImpl implements IStudentDAO {
    @Override
    public Student checkStudent(String inemail, String inpassword) {
        String query = "select * from student where email = ? and password = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setString(1, inemail);
            prest.setString(2, inpassword);

            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date dateOfBirth = rs.getDate("dob");
                String email = rs.getString("email");
                Boolean gender = rs.getBoolean("gender");
                String genderStr = gender? "Nam" : "Nữ";
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                Date created_at = rs.getDate("created_at");

                return new Student(id, name, dateOfBirth,email, genderStr, phone, password, created_at);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Course> listCourses() {
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
                java.util.Date created_at = rs.getDate("created_at");

                courses.add(new Course(id, name, duration, instructor, created_at));
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
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            String str = "%" + key + "%";
            prest.setString(1, str);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int duration = rs.getInt("duration");
                String instructor = rs.getString("instructor");
                java.util.Date created_at = rs.getDate("created_at");

                courses.add(new Course(id, name, duration, instructor, created_at));
            }
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showEnrolment(int id) {
        String query = "select * from enrollment where id = ?";
        String query2 = "select name from student where id = ?";
        String query3 = "select name from course where id = ?";
        String student_name = new String();
        String course_name = new String();
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                int rsid = rs.getInt("id");
                int student_id = rs.getInt("student_id");
                int course_id = rs.getInt("course_id");
                Date registered_at = rs.getDate("registered_at");
                String status = rs.getString("status");
                PreparedStatement prest2 = conn.prepareStatement(query2);
                prest2.setInt(1, student_id);
                PreparedStatement prest3 = conn.prepareStatement(query3);
                prest3.setInt(1, course_id);
                ResultSet rs2 = prest2.executeQuery();
                if (rs2.next()) {
                    student_name = rs2.getString("name");
                }
                ResultSet rs3 = prest3.executeQuery();
                if (rs3.next()) {
                    course_name = rs3.getString("name");
                }
                System.out.println("Thành công!");
                System.out.println("|" + rsid + "|" + student_name + "|" + course_name + "|" + registered_at + "|" + status + "|");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCourseExist(int id) {
        String query = "select count(id) from course where id = ?";
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
    public void registerCourse(int studentID, int courseID) {
        String query = "insert into enrollment(student_id, course_id) values (?, ?)";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);
            prest.setInt(1, studentID);
            prest.setInt(2, courseID);
            prest.execute();
            ResultSet rs = prest.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                showEnrolment(id);
            }
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
    public List<Enrollment> sortEnrollment(int studentID, String sortBy, String sortOrder) {
        List<Enrollment> sortEnrollments = listEnrollment(studentID);
        List<Course> allCourses = listCourses();

        Map<Integer, String> allCoursesMap = allCourses.stream().collect(
                Collectors.toMap(Course::getId, Course::getName)
        );

        if(sortBy.equals("name") && sortOrder.equals("td")) {
            sortEnrollments.sort((e1, e2) -> {
                String name1 = allCoursesMap.getOrDefault(e1.getCourseid(),"");
                String name2 = allCoursesMap.getOrDefault(e2.getCourseid(), "");

                System.out.println("So sánh: [" + name1 + "] với [" + name2 + "]");
                return name1.compareToIgnoreCase(name2);
            });
        }
        if(sortBy.equals("name") && sortOrder.equals("gd")) {
            sortEnrollments.sort((e1, e2) -> {
                String name1 = allCoursesMap.getOrDefault(e1.getCourseid(),"");
                String name2 = allCoursesMap.getOrDefault(e2.getCourseid(), "");
                return name2.compareToIgnoreCase(name1);
            });
        }

        if(sortBy.equals("registered_at") && sortOrder.equals("td")) {
            sortEnrollments.sort((e1, e2) ->{
                return e1.getRegistrationdate().compareTo(e2.getRegistrationdate());
            });
        }

        if(sortBy.equals("registered_at") && sortOrder.equals("gd")) {
            sortEnrollments.sort((e1, e2)->{
                return e2.getRegistrationdate().compareTo(e1.getRegistrationdate());
            });
        }

        return sortEnrollments;
    }

    @Override
    public void cancerEnrollment(int enrollmentID) {
        String query = "update enrollment set status = 'CANCER' where id = ?";
        try {
            Connection conn = DBConection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, enrollmentID);
            prest.execute();
            showEnrolment(enrollmentID);
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
                int  student_id = rs.getInt("id");
                String email1 = rs.getString("email");
                String password1 = rs.getString("password");
                System.out.println(email1);
                System.out.println(password1);
                foundStudent = new Student(student_id, email1, password1);
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
