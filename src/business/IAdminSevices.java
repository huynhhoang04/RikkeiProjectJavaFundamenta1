package business;

import model.Admin;
import model.Course;
import model.Student;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IAdminSevices {
    Admin login(String username, String password);

    List<Course> showListCourse();
    boolean addCourse(String name, int duration, String instructor);
    boolean updateCourseName(int id, String newName);
    boolean updateCourseDuration(int id, int newDuration);
    boolean updateCourseInstructor(int id, String newInstructor);
    List<Course> findCourseByKey(String key);
    List<Course> sortListCourse(String sortBy, String sortOrder);
    boolean deleteCourse(int id);

    List<Student> showListStudent();
    boolean addStudent(String name, Date dob, String email, boolean gender, String phone, String password);
    boolean editStudent(int id, String fieldName, String newValue) throws ParseException;
    List<Student> findStudent(String key, String searchBy);
    List<Student> sortListStudent(String sortBy, String sortOrder);
    boolean deleteStudent(int id);

    void showRegistered();
    void comfirmRegisteration();

    void showTotalCoursesAndStudents();
    void sgowTotalStudentsByCourse();
    void Top5CourseWithStudents();
    void CourseWithMoreThan10Students();
}
