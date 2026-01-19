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
    boolean cancerEnrollment(int id);
    boolean checkCheckCancerlation(int studentID, int enrollmentID);
    void changePassword(int studentID,  String password);
    boolean verification(int id, String password, String email);
}
