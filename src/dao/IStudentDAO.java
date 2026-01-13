package dao;

import model.Course;
import model.Enrollment;
import model.Student;

import java.util.List;

public interface IStudentDAO {
    Student checkStudent(String email, String password);

    List<Course> listCourses();
    List<Course> findCourse(String key);
    void showEnrolment(int id);
    boolean checkCourseExist(int id);
    void registerCourse(int studentID, int courseID);

    List<Enrollment>  listEnrollment(int studentID);
    List<Enrollment> sortEnrollment(int studentID, String sortBy,  String sortOrder);
    void cancerEnrollment(int id);
    boolean checkCheckCancerlation(int studentID, int enrollmentID);
    void changePassword(int studentID,  String password);
    boolean verification(int id, String password, String email);
}
