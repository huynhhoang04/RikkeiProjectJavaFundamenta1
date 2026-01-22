package business;

import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentServices
{
    Student login(String email, String password);
    List<Course> getAllCourses();
    List<Course> searchCourses(String key);
    boolean registerCourse(int studentID, int courseID);
    List<EnrollmentDetailDTO> getEnrollmentHistory(int Id);
    List<EnrollmentDetailDTO> getSortedHistory(int studentId, String sortBy, String sortOrder);
    boolean cancelEnrollment(int studentID, int enrollmentID);
    boolean changePassword(int studentID, String email, String oldPass, String newPass);

    List<Course> getSuggestedCourse(int studentID);
}
