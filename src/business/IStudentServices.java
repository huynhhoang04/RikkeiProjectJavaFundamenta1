package business;

import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentServices
{
    Student login(String email, String password);
    List<Course> showListCourses();
    List<Course> findCourse(String key);
    boolean registerCourse(int studentID, int courseID);
    List<EnrollmentDetailDTO> getHistory(int Id);
    List<EnrollmentDetailDTO> sortEnrollment(int studentId, String sortBy, String sortOrder);
    boolean cancelEnrollment(int studentID, int enrollmentID);
    boolean changePassword(int studentID, String email, String oldPass, String newPass);
}
