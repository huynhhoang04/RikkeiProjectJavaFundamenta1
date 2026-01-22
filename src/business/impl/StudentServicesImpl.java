package business.impl;


import business.IStudentServices;
import dao.IStudentDAO;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StudentServicesImpl implements IStudentServices {
    private final IStudentDAO dao;

    public StudentServicesImpl(IStudentDAO dao) {
        this.dao = dao;
    }

    @Override
    public Student login(String email, String password) {
        Student student = dao.login(email, password);
        return student;
    }

    @Override
    public List<Course> getAllCourses() {
        return dao.getAllCourses();
    }

    @Override
    public List<Course> searchCourses(String key) {
        return dao.searchCourses(key);
    }

    @Override
    public boolean registerCourse(int studentID, int courseID) {
        if (!dao.existsCourseById(courseID)) {
            return false;
        }
        if (!dao.isAlreadyEnrolled(studentID, courseID)) {
            return false;
        }
        return dao.createEnrollment(studentID, courseID);
    }

    @Override
    public List<EnrollmentDetailDTO> getEnrollmentHistory(int studentId) {
        return dao.getEnrollmentHistory(studentId);
    }

    @Override
    public List<EnrollmentDetailDTO> getSortedHistory(int studentId, String sortBy, String sortOrder) {
        List<EnrollmentDetailDTO> list = dao.getEnrollmentHistory(studentId);
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
        boolean canCancel = dao.isEnrollmentCancellable(studentID, enrollmentID);
        if (!canCancel) {
            return false;
        }
        return dao.cancelEnrollmentRequest(enrollmentID);
    }

    @Override
    public boolean changePassword(int studentID, String email, String oldPass, String newPass) {
        if (!dao.verifyCredentials(studentID, oldPass, email)) {
            return false;
        }
        if (oldPass.equals(newPass)) {
            return false;
        }
        return dao.updatePassword(studentID, newPass);
    }

    @Override
    public List<Course> getSuggestedCourse(int studentID) {
        List<Course> result =  new ArrayList<>();

        List<Course> allCourses = dao.getAllCourses();
        List<EnrollmentDetailDTO> history = dao.getEnrollmentHistory(studentID);
        Course topCourse = dao.getTopCourse();

        if (history.isEmpty()) {
            if (topCourse == null) { result.add(topCourse); }
            else {result.add(allCourses.get(0));}
            return result;
        }

        List<String> AttendCourseName = new ArrayList<>();
        history.forEach(enrollment -> {
            AttendCourseName.add(enrollment.getCourseName());
        });
        history.sort((o1, o2) -> o1.getRegisteredAt().compareTo(o2.getRegisteredAt()));
        String lastCourse = history.get(history.size() - 1).getCourseName();
        String key[] = lastCourse.toLowerCase().replaceAll("[^a-zà-ỹ\\s]", " ").trim().split("\\s+");

        allCourses.forEach(course -> {
            for (String keyword : key) {
                if(course.getName().toLowerCase().contains(keyword) && !AttendCourseName.contains(course.getName())) {
                    result.add(course);
                }
            }
        });
        if (result.isEmpty()) {result.add(topCourse);}
        return result;
    }
}
