package business;

import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAdminServices {
    //dăng nhập
    Admin login(String username, String password);
    //lấy tất cả kháo học
    List<Course> getAllCourses();
    //tạo khóa học
    boolean createCourse(String name, int duration, String instructor);
    //kiểm tra khóa tồn tại không
    boolean existsCourseById(int courseId);
    //cập nhật khóa
    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);
    // tìm kiếm khóa
    List<Course> searchCourses(String key);
    //sắp xếp khóa
    List<Course> getSortedCourses(String sortBy, String sortOrder);
    //xóa khóa
    boolean deleteCourse(int id);
    //lấy ds tất cả học sinh
    List<Student> getAllStudents();
    //tạo học viên
    boolean createStudent(String name, Date dob, String email, boolean gender, String phone, String password);
    //ktra học viên tồn tại
    boolean existsStudentById(int id);
    //cập nhật học viên
    boolean updateStudentField(int id, String fieldName, String newValue) ;
    //tìm kiếm hvieen
    List<Student> searchStudents(String key, String searchBy);
    //sắp xếp học viên
    List<Student> getSortedStudents(String sortBy, String sortOrder);
    //xóa học viên
    boolean deleteStudent(int id);
    //lấy thông thông tin đăng ký theo khóa
    List<EnrollmentDetailDTO> getEnrollmentsByCourse(int courseId);
    //cũng là lấy thông thông tin đăng ký theo khóa nhưng đang đợi duyệt
    List<EnrollmentDetailDTO> getPendingEnrollments(int courseId);
    //xác nhận dky
    boolean approveEnrollment(int enrollmentId);
    //từ chối dky
    boolean denyEnrollment(int enrollmentId);
    //xóa đăng ký (chỉ xóa được khi đã xác nhận khóa)
    boolean deleteEnrollment(int enrollmentId);

    //lấy thông tin thống kê
    Map<String, Integer> getSystemStatistics();
    Map<String, Integer> getStudentCountByCourse();
    Map<String, Integer> getTop5PopularCourses();
    Map<String, Integer> getCoursesWithHighEnrollment();
}
