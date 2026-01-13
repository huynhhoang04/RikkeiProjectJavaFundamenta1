package dao;

import model.Admin;
import model.Course;
import model.Enrollment;
import model.Student;

import java.util.Date;
import java.util.List;

public interface IAdminDAO {
    void showByStudentID(int id);

    Admin checkAdmin(String username, String password);

    List<Course> listCourse();
    void addCourse(String name, int duration,  String instructor);
    void editCourse(int id, String editPart, String editContent);
    List<Course> findCourse(String key);
    List<Course> sortListCourse(List<Course> courses, String sortBy);
    boolean checkCourse(int id);
    void deleteCourse(int id);
    boolean checkCourseHasStudent(int courseId);

    List<Student>  listStudent();
    void addStudent(String name, Date dob, String email, Boolean gender, String phone, String password);
    boolean checkEmailExists(String email);
    void editStudent(int id, String editPart, String editContent);
    List<Student> findStudent(String key, String searchBy);
    List<Student> sortListStudent(List<Student> students, String sortBy);
    void deleteStudent(int id);
    boolean checkStudentAttend(int id);
    boolean checkStudentExists(int id);

    void listStudentByCourse(int id);
    Student findStudent(int id);
    void clarifyEnrollment(int id, String action);
    List<Enrollment> unclarifyEnrollment(int id);
    boolean checkEnrollment(int id, int course_id);

    void totalCourseAndStudent();
    void analyzeTotalStudentByCourse();
    void Top5CourseWithMostStudents();
    void CourseWithMoreThan10Students();
}
