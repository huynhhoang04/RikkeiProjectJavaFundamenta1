package business.impl;

import business.IAdminServices;
import dao.IAdminDAO;
import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.*;
import java.util.stream.Collectors;

public class AdminServicesImpl implements IAdminServices {
    private final IAdminDAO dao;

    // khởi tạo service với dao được tiêm vào
    public AdminServicesImpl(IAdminDAO dao) {
        this.dao = dao;
    }

    //region authentication (xác thực)
    @Override
    public Admin login(String username, String password) {
        // gọi dao để kiểm tra đăng nhập
        return dao.login(username, password);
    }
    //endregion

    //region course management (quản lý khóa học)
    @Override
    public List<Course> getAllCourses() {
        return dao.getAllCourses();
    }

    @Override
    public boolean createCourse(String name, int duration, String instructor) {
        // validate thời lượng phải lớn hơn 0
        if (duration <= 0) return false;
        return dao.createCourse(name, duration, instructor);
    }

    @Override
    public boolean existsCourseById(int courseId) {
        return dao.existsCourseById(courseId);
    }

    @Override
    public boolean updateCourseName(int id, String newName) {
        return dao.updateCourseName(id, newName);
    }

    @Override
    public boolean updateCourseDuration(int id, int newDuration) {
        // validate thời lượng mới
        if (newDuration <= 0) return false;
        return dao.updateCourseDuration(id, newDuration);
    }

    @Override
    public boolean updateCourseInstructor(int id, String newInstructor) {
        return dao.updateCourseInstructor(id, newInstructor);
    }

    @Override
    public List<Course> searchCourses(String key) {
        return dao.searchCourses(key);
    }

    @Override
    public List<Course> getSortedCourses(String sortBy, String sortOrder) {
        List<Course> list = dao.getAllCourses();
        if (list.isEmpty()) {
            return list;
        }
        Comparator<Course> comparator = null;
        // xác định tiêu chí sắp xếp
        if (sortBy.equalsIgnoreCase("name")) {
            comparator = (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName());
        } else if (sortBy.equalsIgnoreCase("duration")) {
            comparator = (c1, c2) -> Integer.compare(c1.getDuration(), c2.getDuration());
        }

        // đảo ngược nếu là giảm dần
        if (comparator != null) {
            if (sortOrder.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            list.sort(comparator);
        }
        return list;
    }

    @Override
    public boolean deleteCourse(int id) {
        // không cho xóa nếu khóa học đang có người học
        if (dao.isCourseInUse(id)) {
            return false;
        }
        return dao.deleteCourse(id);
    }
    //endregion

    //region student management (quản lý sinh viên)
    @Override
    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    @Override
    public boolean createStudent(String name, Date dob, String email, boolean gender, String phone, String password)  {
        // kiểm tra email trùng lặp trước khi tạo
        if (dao.existsByEmail(email)) {
            return false;
        }

        return dao.createStudent(name, dob, email, gender, phone, password);
    }

    @Override
    public boolean existsStudentById(int id) {
        return dao.existsStudentById(id);
    }

    @Override
    public boolean updateStudentField(int id, String fieldName, String newValue)  {
        // nếu sửa email thì phải check trùng
        if (fieldName.equalsIgnoreCase("email") && dao.existsByEmail(newValue)) {
            return false;
        }
        return dao.updateStudentField(id, fieldName, newValue);
    }

    @Override
    public List<Student> searchStudents(String key, String searchBy) {
        return dao.searchStudents(key, searchBy);
    }

    @Override
    public List<Student> getSortedStudents(String sortBy, String sortOrder) {
        List<Student> list = dao.getAllStudents();
        if (list.isEmpty()) {
            return list;
        }
        Comparator<Student> comparator = null;
        if (sortBy.equalsIgnoreCase("name")) {
            comparator = (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName());

        } else if (sortBy.equalsIgnoreCase("email")) {
            comparator = (s1, s2) -> s1.getEmail().compareToIgnoreCase(s2.getEmail());
        }
        if (comparator != null) {
            if (sortOrder.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            list.sort(comparator);
        }
        return list;
    }

    @Override
    public boolean deleteStudent(int id) {
        // không xóa sinh viên đã có lịch sử học
        if (dao.hasEnrollments(id)) {
            return false;
        }
        return dao.deleteStudent(id);
    }
    //endregion

    //region enrollment management (quản lý đăng ký)
    @Override
    public List<EnrollmentDetailDTO> getEnrollmentsByCourse(int courseId) {
        if(!dao.existsCourseById(courseId)){
            return null;
        }
        return dao.getEnrollmentsByCourse(courseId);
    }

    @Override
    public List<EnrollmentDetailDTO> getPendingEnrollments(int courseId) {
        if(!dao.existsCourseById(courseId)){
            return null;
        }
        return dao.getPendingEnrollments(courseId);
    }

    @Override
    public boolean approveEnrollment(int enrollmentId) {
        // duyệt đơn đăng ký (chuyển thành confirm)
        return dao.updateEnrollmentStatus(enrollmentId, "CONFIRM");
    }

    @Override
    public boolean denyEnrollment(int enrollmentId) {
        // từ chối đơn đăng ký (chuyển thành denied)
        return dao.updateEnrollmentStatus(enrollmentId, "DENIED");
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        return dao.deleteEnrollment(enrollmentId);
    }
    //endregion

    //region statistics (thống kê)
    @Override
    public Map<String, Integer> getSystemStatistics() {
        return dao.getSystemStatistics();
    }

    @Override
    public Map<String, Integer> getStudentCountByCourse() {
        return dao.getStudentCountByCourse();
    }

    @Override
    public Map<String, Integer> getTop5PopularCourses() {
        // lấy map thống kê số lượng
        Map<String, Integer> data = dao.getStudentCountByCourse();
        // dùng stream để sắp xếp giảm dần và lấy top 5 bằng limit 5 ở dười
        return data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new ));
    }

    @Override
    public Map<String, Integer> getCoursesWithHighEnrollment() {
        Map<String, Integer> data = dao.getStudentCountByCourse();
        // lọc các khóa học có trên 10 sinh viên bằng filter
        return data.entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new));
    }
    //endregion
}


