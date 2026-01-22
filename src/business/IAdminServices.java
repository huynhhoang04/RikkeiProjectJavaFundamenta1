package business;

import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAdminServices {
    Admin login(String username, String password);

    List<Course> getAllCourses();
    boolean createCourse(String name, int duration, String instructor);
    boolean existsCourseById(int courseId);
    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);
    List<Course> searchCourses(String key);
    List<Course> getSortedCourses(String sortBy, String sortOrder);
    boolean deleteCourse(int id);

    List<Student> getAllStudents();
    boolean createStudent(String name, Date dob, String email, boolean gender, String phone, String password);
    boolean existsStudentById(int id);
    boolean updateStudentField(int id, String fieldName, String newValue) ;
    List<Student> searchStudents(String key, String searchBy);
    List<Student> getSortedStudents(String sortBy, String sortOrder);
    boolean deleteStudent(int id);

    List<EnrollmentDetailDTO> getEnrollmentsByCourse(int courseId);
    List<EnrollmentDetailDTO> getPendingEnrollments(int courseId);
    boolean approveEnrollment(int enrollmentId);
    boolean denyEnrollment(int enrollmentId);
    boolean deleteEnrollment(int enrollmentId);

    Map<String, Integer> getSystemStatistics();
    Map<String, Integer> getStudentCountByCourse();
    Map<String, Integer> getTop5PopularCourses();
    Map<String, Integer> getCoursesWithHighEnrollment();
}
