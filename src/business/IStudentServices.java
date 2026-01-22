package business;

import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentServices
{
    //đăng nhập
    Student login(String email, String password);
    //lấy tất cả kháo
    List<Course> getAllCourses();
    //tiìm khóa học
    List<Course> searchCourses(String key);
    //dăng ký khoa học
    boolean registerCourse(int studentID, int courseID);
    // lấy ls đăng ký
    List<EnrollmentDetailDTO> getEnrollmentHistory(int Id);
    //sắp sếp ls đăng ký
    List<EnrollmentDetailDTO> getSortedHistory(int studentId, String sortBy, String sortOrder);
    //hủy đăng ký
    boolean cancelEnrollment(int studentID, int enrollmentID);
    //đổi mk
    boolean changePassword(int studentID, String email, String oldPass, String newPass);
    //ds gợi ý khóa học
    List<Course> getSuggestedCourse(int studentID);
}
