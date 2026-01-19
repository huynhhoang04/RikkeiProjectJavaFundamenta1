package dao;

import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.List;

public interface IStudentDAO {
    Student checkStudent(String email, String password);

    List<Course> listCourses();
    List<Course> findCourse(String key);
    List<EnrollmentDetailDTO> getHistory(int Id);
    boolean checkCourseExist(int id);
    boolean registerCourse(int studentID, int courseID);

    List<Enrollment>  listEnrollment(int studentID);
    boolean cancelEnrollment(int id);
    boolean checkCheckCancelable(int studentID, int enrollmentID);
    boolean changePassword(int studentID,  String password);
    boolean verification(int id, String password, String email);
}
