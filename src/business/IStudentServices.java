package business;

import model.Course;

public interface IStudentServices
{
    boolean authStudent(String email, String password);
    void showListCourses();
    void findCourse();
    void registerCourse(int studentID);
    void showRegistreredTicket(int studentID);
    void showSortEnrollment(int studentID);
    void cancerEnrollment(int studentID);
    void changePassword(int studentID);
}
