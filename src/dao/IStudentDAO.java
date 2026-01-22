package dao;

import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentDAO {
    //đăng nhânpj
    Student login(String email, String password);

    //truy cập thông tin về khóa học
    List<Course> getAllCourses();
    List<Course> searchCourses(String key);

    //truy cập thông tin về lịch sử đăng ký
    List<EnrollmentDetailDTO> getEnrollmentHistory(int Id);

    //kiểm tra thông tin và đăng ký học
    boolean existsCourseById(int id);
    boolean isAlreadyEnrolled(int studentID, int courseID);
    boolean createEnrollment(int studentID, int courseID);

    //kiểm tra thông tin về hủy khóa học
    boolean cancelEnrollmentRequest(int id);
    boolean isEnrollmentCancellable(int studentID, int enrollmentID);

    //cập nhật mật khẩu
    boolean updatePassword(int studentID, String password);
    boolean verifyCredentials(int id, String password, String email);

    //sử dụng cho gửi ý để lấy khóa đông học viên nhất
    Course getTopCourse();

}
