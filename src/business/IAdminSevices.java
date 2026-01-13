package business;

import java.text.ParseException;

public interface IAdminSevices {
    boolean authAdmin(String username, String password);

    void showListCourse();
    void addCourse();
    void editCourse();
    void findCourseByKey();
    void sortListCourse();
    void deleteCourse();

    void showListStudent();
    void addStudent() throws ParseException;
    void editStudent() throws ParseException;
    void findStudent();
    void sortListStudent();
    void deleteStudent();

    void showRegistered();
    void comfirmRegisteration();

    void showTotalCoursesAndStudents();
    void sgowTotalStudentsByCourse();
    void Top5CourseWithStudents();
    void CourseWithMoreThan10Students();
}
