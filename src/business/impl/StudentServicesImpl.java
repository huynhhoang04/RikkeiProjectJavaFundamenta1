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

    // tiêm dao vào service
    public StudentServicesImpl(IStudentDAO dao) {
        this.dao = dao;
    }

    //region authentication (xác thực)
    @Override
    public Student login(String email, String password) {
        // gọi dao check đăng nhập
        return dao.login(email, password);

    }

    @Override
    public boolean changePassword(int studentID, String email, String oldPass, String newPass) {
        // xác thực mật khẩu cũ
        if (!dao.verifyCredentials(studentID, oldPass, email)) {
            return false;
        }
        // mật khẩu mới không được trùng cũ
        if (oldPass.equals(newPass)) {
            return false;
        }
        return dao.updatePassword(studentID, newPass);
    }
    //endregion

    //region course viewing (xem khóa học)
    @Override
    public List<Course> getAllCourses() {
        return dao.getAllCourses();
    }

    @Override
    public List<Course> searchCourses(String key) {
        return dao.searchCourses(key);
    }

    @Override
    public List<Course> getSuggestedCourse(int studentID) {
        List<Course> result =  new ArrayList<>();

        // lấy lịch sử học và danh sách tất cả khóa
        List<Course> allCourses = dao.getAllCourses();
        List<EnrollmentDetailDTO> history = dao.getEnrollmentHistory(studentID);
        Course topCourse = dao.getTopCourse();

        // nếu chưa đăng ký học gì thì gợi ý cái khóa topcourse kìa
        if (history.isEmpty()) {
            if (topCourse == null) { result.add(topCourse); }
            // nếu chưa có ai học gì cả thì lấy bừa khóa đầu tiên
            else {result.add(allCourses.get(0));}
            return result;
        }

        // nếu đã học thig gợi ý dựa trên nội dung khóa dăng ký gần nhất
        // lấy tên các khóa đã dăng ký học để loại trừ
        List<String> AttendCourseName = new ArrayList<>();
        history.forEach(enrollment -> {
            AttendCourseName.add(enrollment.getCourseName());
        });

        // đoạn này lấy khóa học mới nhất
        history.sort((o1, o2) -> o1.getRegisteredAt().compareTo(o2.getRegisteredAt()));
        String lastCourse = history.get(history.size() - 1).getCourseName();

        // tách các key ra từu tên kháo
        String key[] = lastCourse.toLowerCase().replaceAll("[^a-zà-ỹ\\s]", " ").trim().split("\\s+");

        // tìm các khóa học có tên chứa key
        allCourses.forEach(course -> {
            for (String keyword : key) {
                if(course.getName().toLowerCase().contains(keyword) && !AttendCourseName.contains(course.getName())) {
                    result.add(course);
                }
            }
        });

        // nếu lọc không ra gì, lại gợi ý khóa hot:)
        if (result.isEmpty()) {result.add(topCourse);}
        return result;
    }
    //endregion

    //region enrollment actions (đăng ký học)
    @Override
    public boolean registerCourse(int studentID, int courseID) {
        // validate: khóa học phải tồn tại
        if (!dao.existsCourseById(courseID)) {
            return false;
        }
        // validate: chưa đăng ký trùng
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

        // xác định tiêu chí sort
        if (sortBy.equalsIgnoreCase("name")) {
            list.sort((o1, o2) -> o1.getCourseName().compareToIgnoreCase(o2.getCourseName()));
        } else if (sortBy.equalsIgnoreCase("time")) {
            list.sort((o1, o2) -> o1.getRegisteredAt().compareTo(o2.getRegisteredAt()));
        }

        // đảo chiều nếu giảm dần
        if (sortOrder.equalsIgnoreCase("desc")) {
            Collections.reverse(list);
        }
        return list;
    }

    @Override
    public boolean cancelEnrollment(int studentID, int enrollmentID) {
        // chỉ cho hủy nếu đơn đang ở trạng thái waiting
        boolean canCancel = dao.isEnrollmentCancellable(studentID, enrollmentID);
        if (!canCancel) {
            return false;
        }
        return dao.cancelEnrollmentRequest(enrollmentID);
    }
    //endregion
}
