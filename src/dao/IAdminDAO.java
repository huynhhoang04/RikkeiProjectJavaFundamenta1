package dao;

import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAdminDAO {
    //đăng nhập
    Admin login(String username, String password);

    //truy cập dữ liệu về khóa học
    List<Course> getAllCourses();
    boolean createCourse(String name, int duration, String instructor);
    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);
    List<Course> searchCourses(String key);
    boolean existsCourseById(int id);
    boolean deleteCourse(int id);
    boolean isCourseInUse(int id);


    //truy cập dữ liệu về học viên
    List<Student> getAllStudents();
    boolean createStudent(String name, Date dob, String email, Boolean gender, String phone, String password);
    boolean existsByEmail(String email);
    boolean updateStudentField(int id, String editPart, String editContent);
    List<Student> searchStudents(String key, String searchBy);
    boolean deleteStudent(int id);
    boolean hasEnrollments(int id);
    boolean existsStudentById(int id);

    //truy cập quản lý đăng ký
    List<EnrollmentDetailDTO> getEnrollmentsByCourse(int id);
    boolean updateEnrollmentStatus(int id, String action);
    List<EnrollmentDetailDTO> getPendingEnrollments(int id);
    boolean deleteEnrollment(int id);

    //truy cập và thông kê thông tin hệ thống
    Map<String ,Integer> getSystemStatistics();
    Map<String, Integer> getStudentCountByCourse();
}
