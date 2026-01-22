package dao;

import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentDAO {
    Student login(String email, String password);

    List<Course> getAllCourses();
    List<Course> searchCourses(String key);
    List<EnrollmentDetailDTO> getEnrollmentHistory(int Id);
    boolean existsCourseById(int id);
    boolean isAlreadyEnrolled(int studentID, int courseID);
    boolean createEnrollment(int studentID, int courseID);

    boolean cancelEnrollmentRequest(int id);
    boolean isEnrollmentCancellable(int studentID, int enrollmentID);
    boolean updatePassword(int studentID, String password);
    boolean verifyCredentials(int id, String password, String email);
    Course getTopCourse();
}
