package business.impl;


import business.IStudentServices;
import dao.IStudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StudentServicesImpl implements IStudentServices {
    Scanner input = new Scanner(System.in);
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

        if (sortOrder.equalsIgnoreCase("gd")) {
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
            return false; // Sai thông tin cũ -> Từ chối đổi
        }

        // 2. Logic nghiệp vụ: Có thể check thêm (Mật khẩu mới không được trùng mật khẩu cũ)
        if (oldPass.equals(newPass)) {
            // Tùy ông, thường thì không cho trùng, nhưng ở đây tôi return false coi như lỗi logic
            return false;
        }

        // 3. Gọi DAO thực hiện đổi
        return dao.changePassword(studentID, newPass);
    }


}
