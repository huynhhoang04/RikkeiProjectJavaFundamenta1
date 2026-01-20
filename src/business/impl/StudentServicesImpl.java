package business.impl;


import business.IStudentServices;
import dao.IStudentDAO;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.Collections;
import java.util.List;


public class StudentServicesImpl implements IStudentServices {
    private final IStudentDAO dao;

    public StudentServicesImpl(IStudentDAO dao) {
        this.dao = dao;
    }

    @Override
    public Student login(String email, String password) {
        Student student = dao.checkStudent(email, password);
        return student;
    }

    @Override
    public List<Course> showListCourses() {
        return dao.listCourses();
    }

    @Override
    public List<Course> findCourse(String key) {
        return dao.findCourse(key);
    }

    @Override
    public boolean registerCourse(int studentID, int courseID) {
        if (!dao.checkCourseExist(courseID)) {
            return false;
        }
        return dao.registerCourse(studentID, courseID);
    }

    @Override
    public List<EnrollmentDetailDTO> getHistory(int studentId) {
        return dao.getHistory(studentId);
    }

    @Override
    public List<EnrollmentDetailDTO> sortEnrollment(int studentId, String sortBy, String sortOrder) {
        List<EnrollmentDetailDTO> list = dao.getHistory(studentId);
        if (list.isEmpty()) return list;
        if (sortBy.equalsIgnoreCase("name")) {
            list.sort((o1, o2) -> o1.getCourseName().compareToIgnoreCase(o2.getCourseName()));
        } else if (sortBy.equalsIgnoreCase("time")) {
            list.sort((o1, o2) -> o1.getRegisteredAt().compareTo(o2.getRegisteredAt()));
        }
        if (sortOrder.equalsIgnoreCase("desc")) {
            Collections.reverse(list);
        }
        return list;
    }

    @Override
    public boolean cancelEnrollment(int studentID, int enrollmentID) {
        boolean canCancel = dao.checkCheckCancelable(studentID, enrollmentID);
        if (!canCancel) {
            return false;
        }
        return dao.cancelEnrollment(enrollmentID);
    }

    @Override
    public boolean changePassword(int studentID, String email, String oldPass, String newPass) {
        if (!dao.verification(studentID, oldPass, email)) {
            return false;
        }
        if (oldPass.equals(newPass)) {
            return false;
        }
        return dao.changePassword(studentID, newPass);
    }
}
