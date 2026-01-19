package business.impl;

import business.IAdminSevices;
import dao.IAdminDAO;
import model.Admin;
import dao.impl.AdminDAOImpl;
import model.Course;
import model.Enrollment;
import model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public void showRegistered() {
        while (true) {
            System.out.println("nhập id khóa hocjh : ");
            String strid = input.nextLine();
            if(strid.isBlank()){
                System.out.println("không được đẻ trống");
                continue;
            }
            if(!strid.matches("\\d+")){
                System.out.println("Id phải là số!");
                continue;
            }
            int id = Integer.parseInt(strid);
            if(!dao.checkCourse(id)){
                System.out.println("Khóa học không tồn tại");
                continue;
            }
            else {
                dao.listStudentByCourse(id);
                break;
            }
        }
    }

    @Override
    public void comfirmRegisteration() {
        nemuChinh : while (true) {
            System.out.println("nhập id khóa hocjh : ");
            String strid = input.nextLine();
            if(strid.isBlank()){
                System.out.println("không được đẻ trống");
                continue;
            }
            if(!strid.matches("\\d+")){
                System.out.println("Id phải là số!");
                continue;
            }
            int id = Integer.parseInt(strid);
            if(!dao.checkCourse(id)){
                System.out.println("Khóa học không tồn tại");
                continue;
            }
            else{
                System.out.println("==========================");
                System.out.println("1. Xem phiếu đăng ký đang đợi xác nhận");
                System.out.println("2. Chấp nhận phiếu đang ký");
                System.out.println("3. Từ chối phiếu đăng ký");
                System.out.println("4. Trở về");
                System.out.println("==========================");
                System.out.print("Nhập lựa chọn : ");
                switch (input.nextLine()) {
                    case "1":
                        List<Enrollment> enrollments = dao.unclarifyEnrollment(id);
                        for (Enrollment enrollment : enrollments) {
                            System.out.println(enrollment);
                        }
                        break;
                    case "2":
                        System.out.println("Nhập id phiếu đăng ký");
                        String strenrollmentId = input.nextLine();
                        if(strenrollmentId.isBlank()){
                            System.out.println("Không được để trống!");
                            continue;
                        }
                        if(!strenrollmentId.matches("\\d+")){
                            System.out.println("ID phải là số");
                            continue ;
                        }
                        int enrollmentId = Integer.parseInt(strenrollmentId);
                        if(!dao.checkEnrollment(enrollmentId, id)){
                            System.out.println("Invalid!");
                            continue ;
                        }
                        else {
                            dao.clarifyEnrollment(enrollmentId, "Confirm");
                            break;
                        }
                    case "3":
                        System.out.println("Nhập id phiếu đăng ký");
                        String strenrollmentId2 = input.nextLine();
                        if(strenrollmentId2.isBlank()){
                            System.out.println("Không được để trống!");
                            continue;
                        }
                        if(!strenrollmentId2.matches("\\d+")){
                            System.out.println("ID phải là số");
                            continue ;
                        }
                        int enrollmentId2 = Integer.parseInt(strenrollmentId2);
                        if(!dao.checkEnrollment(enrollmentId2, id)){
                            System.out.println("Invalid!");
                            continue ;
                        }
                        else {
                            dao.clarifyEnrollment(enrollmentId2, "Denied");
                            break;
                        }
                    case "4":
                        break nemuChinh;
                    default:
                        System.out.println("Lựa chọn Invalid!");
                }
            }
        }
    }

    //endregion

    //region analyze
    @Override
    public void showTotalCoursesAndStudents() {
        dao.totalCourseAndStudent();
        System.out.print("Nhập bất kì đẻ trở về : ");
        input.nextLine();
    }

    @Override
    public void sgowTotalStudentsByCourse() {
        dao.analyzeTotalStudentByCourse();
        System.out.print("Nhập bất kì đẻ trở về : ");
        input.nextLine();
    }

    @Override
    public void Top5CourseWithStudents() {
        System.out.println("==========Top 5 khóa học có nhiều học sinh nhất===========");
        dao.Top5CourseWithMostStudents();
        System.out.print("Nhập bất kì đẻ trở về : ");
        input.nextLine();
    }

    @Override
    public void CourseWithMoreThan10Students() {
        System.out.println("==========Khóa học có nhiều hơn 10 học sinh ===========");
        dao.CourseWithMoreThan10Students();
        System.out.print("Nhập bất kì đẻ trở về : ");
        input.nextLine();
    }


    //endregion
}


