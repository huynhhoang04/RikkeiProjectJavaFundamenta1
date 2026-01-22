package business;

import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAdminSevices {
    Admin login(String username, String password);

    List<Course> showListCourse();
    boolean addCourse(String name, int duration, String instructor);
    boolean checkCourse(int courseId);
    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);
    List<Course> findCourseByKey(String key);
    List<Course> sortListCourse(String sortBy, String sortOrder);
    boolean deleteCourse(int id);

    List<Student> showListStudent();
    boolean addStudent(String name, Date dob, String email, boolean gender, String phone, String password);
    boolean checkStudent(int id);
    boolean editStudent(int id, String fieldName, String newValue) ;
    List<Student> findStudent(String key, String searchBy);
    List<Student> sortListStudent(String sortBy, String sortOrder);
    boolean deleteStudent(int id);

    List<EnrollmentDetailDTO> getCourseEnrollments(int courseId);
    List<EnrollmentDetailDTO> getPendingEnrollments(int courseId);
    boolean approveEnrollment(int enrollmentId);
    boolean denyEnrollment(int enrollmentId);
    boolean deleteEnrollment(int enrollmentId);

    Map<String, Integer> showTotalCoursesAndStudents();
    Map<String, Integer> showTotalStudentsByCourse();
    Map<String, Integer> Top5CourseWithStudents();
    Map<String, Integer> CourseWithMoreThan10Students();
}
