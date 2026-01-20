package dao;

import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IAdminDAO {

    Admin checkAdmin(String username, String password);

    List<Course> listCourse();
    boolean addCourse(String name, int duration,  String instructor);

    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);

    List<Course> findCourse(String key);
    boolean checkCourse(int id);
    boolean deleteCourse(int id);
    boolean checkCourseHasStudent(int id);

    List<Student>  listStudent();
    boolean addStudent(String name, Date dob, String email, Boolean gender, String phone, String password);
    boolean checkEmailExists(String email);
    boolean editStudent(int id, String editPart, String editContent);
    List<Student> findStudent(String key, String searchBy);
    boolean deleteStudent(int id);
    boolean checkStudentAttend(int id);
    boolean checkStudentExists(int id);

    List<EnrollmentDetailDTO> listStudentByCourse(int id);
    boolean clarifyEnrollment(int id, String action);
    List<EnrollmentDetailDTO> getPendingEnrollments(int id);
    boolean deleteEnrollment(int id);

    Map<String ,Integer> totalCourseAndStudent();
    Map<String, Integer> analyzeTotalStudentByCourse();
}
