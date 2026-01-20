package business.impl;

import business.IAdminSevices;
import dao.IAdminDAO;
import model.Admin;
import dao.impl.AdminDAOImpl;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static uttil.checkPhoneValid.checkPhone;
import static uttil.checkDOBValid.isValidDOB;
import static uttil.checkEmailValid.isValidEmail;

public class AdminSevicesImpl implements IAdminSevices {
    Scanner input = new Scanner(System.in);
    private final IAdminDAO dao;

    public AdminSevicesImpl(IAdminDAO dao) {
        this.dao = dao;
    }


    //region: auth
    @Override
    public Admin login(String username, String password) {
        Admin admin = dao.checkAdmin(username, password);
        return admin;
    }
    //endregion

    //region: coursemanagement
    @Override
    public List<Course> showListCourse() {
        return dao.listCourse();
    }

    @Override
    public boolean addCourse(String name, int duration, String instructor) {
        if (duration <= 0) return false;
        return dao.addCourse(name, duration, instructor);
    }

    @Override
    public boolean updateCourseName(int id, String newName) {
        return dao.updateCourseName(id, newName);
    }

    @Override
    public boolean updateCourseDuration(int id, int newDuration) {
        return dao.updateCourseDuration(id, newDuration);
    }

    @Override
    public boolean updateCourseInstructor(int id, String newInstructor) {
        return dao.updateCourseInstructor(id, newInstructor);
    }

    @Override
    public List<Course> findCourseByKey(String key) {
        return dao.findCourse(key);
    }

    @Override
    public List<Course> sortListCourse(String sortBy, String sortOrder) {
        List<Course> list = dao.listCourse();

        if (list.isEmpty()) {
            return list;
        }

        // 2. Tạo bộ so sánh (Comparator) tùy theo tiêu chí
        Comparator<Course> comparator = null;

        if (sortBy.equalsIgnoreCase("name")) {
            // So sánh theo Tên (String)
            comparator = (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName());
        } else if (sortBy.equalsIgnoreCase("duration")) {
            // So sánh theo Thời lượng (Int)
            // Dùng Integer.compare để an toàn hơn dấu trừ (-)
            comparator = (c1, c2) -> Integer.compare(c1.getDuration(), c2.getDuration());
        }

        // 3. Xử lý chiều Tăng/Giảm
        if (comparator != null) {
            // Nếu chọn GIẢM DẦN (gd) thì đảo ngược bộ so sánh
            if (sortOrder.equalsIgnoreCase("gd")) {
                comparator = comparator.reversed();
            }
            // Bắt đầu sắp xếp
            list.sort(comparator);
        }

        return list;
    }

    @Override
    public boolean deleteCourse(int id) {
        if (dao.checkCourseHasStudent(id)) {
            return false;
        }
        return dao.deleteCourse(id);
    }

    //endregion

    //region: studentmanagement
    @Override
    public List<Student> showListStudent() {
        return dao.listStudent();
    }

    @Override
    public boolean addStudent(String name, Date dob, String email, boolean gender, String phone, String password)  {
        if (dao.checkEmailExists(email)) {
            return false; // Email trùng -> Báo thất bại
        }

        return dao.addStudent(name, dob, email, gender, phone, password);
    }

    @Override
    public boolean editStudent(int id, String fieldName, String newValue)  {
        return dao.editStudent(id, fieldName, newValue);
    }

    @Override
    public List<Student> findStudent(String key, String searchBy) {
        return dao.findStudent(key, searchBy);
    }

    @Override
    public List<Student> sortListStudent(String sortBy, String sortOrder) {
        List<Student> list = dao.listStudent(); //

        // Nếu list rỗng thì trả về luôn
        if (list.isEmpty()) {
            return list;
        }

        // 2. Tạo bộ so sánh (Comparator) tùy theo tiêu chí (Tên hay Email)
        Comparator<Student> comparator = null;

        if (sortBy.equalsIgnoreCase("name")) {
            // So sánh theo TÊN (Bỏ qua hoa thường)
            comparator = (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName());

        } else if (sortBy.equalsIgnoreCase("email")) {
            // So sánh theo EMAIL
            comparator = (s1, s2) -> s1.getEmail().compareToIgnoreCase(s2.getEmail());
        }

        // 3. Xử lý chiều Tăng/Giảm
        if (comparator != null) {
            // Nếu user chọn GIẢM DẦN ("gd") thì đảo ngược bộ so sánh
            if (sortOrder.equalsIgnoreCase("gd")) {
                comparator = comparator.reversed();
            }

            // Bắt đầu sắp xếp
            list.sort(comparator);
        }

        return list;
    }

    @Override
    public boolean deleteStudent(int id) {
        if (!dao.checkStudentExists(id)) {
            return false;
        }
        if (dao.checkStudentAttend(id)) {
            return false;
        }
        return dao.deleteStudent(id);
    }


    //endregion

    //region Enrollment
    @Override
    public List<EnrollmentDetailDTO> getCourseEnrollments(int courseId) {
        if(!dao.checkCourse(courseId)){
            return null;
        }
        return dao.listStudentByCourse(courseId);
    }

    @Override
    public List<EnrollmentDetailDTO> getPendingEnrollments(int courseId) {
        if(!dao.checkCourse(courseId)){
            return null;
        }
        return dao.getPendingEnrollments(courseId);
    }

    @Override
    public boolean approveEnrollment(int enrollmentId) {
        return dao.clarifyEnrollment(enrollmentId, "CONFIRM");
    }

    @Override
    public boolean denyEnrollment(int enrollmentId) {
        return dao.clarifyEnrollment(enrollmentId, "DENIED");
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        return dao.deleteEnrollment(enrollmentId);
    }


    //endregion

    //region analyze
    @Override
    public Map<String, Integer> showTotalCoursesAndStudents() {
        return dao.totalCourseAndStudent();
    }

    @Override
    public Map<String, Integer> showTotalStudentsByCourse() {
        return dao.analyzeTotalStudentByCourse();
    }

    @Override
    public Map<String, Integer> Top5CourseWithStudents() {
        Map<String, Integer> data = dao.analyzeTotalStudentByCourse();

        return data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new ));
    }

    @Override
    public Map<String, Integer> CourseWithMoreThan10Students() {
        Map<String, Integer> data = dao.analyzeTotalStudentByCourse();

        return data.entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new));
    }

    //endregion
}


