package business.impl;

import business.IAdminSevices;
import model.Admin;
import dao.impl.AdminDAOImpl;
import model.Course;
import model.Enrollment;
import model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static uttil.checkPhoneValid.checkPhone;
import static uttil.checkDOBValid.isValidDOB;
import static uttil.checkEmailValid.isValidEmail;

public class AdminSevicesImpl implements IAdminSevices {
    Scanner input = new Scanner(System.in);
    private final AdminDAOImpl dao = new AdminDAOImpl();
    //region: auth
    @Override
    public boolean authAdmin(String username, String password) {
        Admin login = dao.checkAdmin(username, password);
        return login != null;
    }
    //endregion

    //region: coursemanagement
    @Override
    public void showListCourse() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        List<Course> listCourse = dao.listCourse();
        for (Course course : listCourse) {
            System.out.println(course);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Nhấn phím bất kỳ để trờ về : ");
        input.nextLine();
    }

    @Override
    public void addCourse() {
        while (true) {
            System.out.println("================================");
            System.out.print("Nhập tên khóa học : ");
            String courseName = input.nextLine();
            System.out.print("Nhập thời lượng : ");
            String courseDuration = input.nextLine();
            System.out.print("Nhập giảng viên :");
            String courseInstructor = input.nextLine();
            System.out.println("================================");

            if (courseName.isBlank() || courseDuration.isBlank() || courseInstructor.isBlank()) {
                System.out.println("Tất cả các thông tin  về khóa học không được để trống vui lòng nhập lại.");
                continue;
            }
            if (!courseDuration.matches("\\d+")) {
                System.out.println("Thời lượng phải là số vui lòng nhập lại");
                continue;
            }
            int duration = Integer.parseInt(courseDuration);
            if (duration <= 0) {
                System.out.println("Thời lượng khóa học phải lớn 0");
            } else {
                dao.addCourse(courseName, duration, courseInstructor);
                System.out.println("Nhập -1 để quay về , 1 để tiếp túc thêm hóa học :");
                menuCon:
                while (true) {
                    switch (input.nextLine()) {
                        case "1":
                            break menuCon;
                        case "-1":
                            return;
                        default:
                            System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
                    }
                }
            }
        }
    }

    @Override
    public void editCourse() {
        System.out.println("================================");
        System.out.print("Nhập id khóa học để edit: ");
        int id = Integer.parseInt(input.nextLine());
        System.out.println("Nhập thành phần muốn sửa :");
        System.out.println("1. Sửa tên khóa học");
        System.out.println("2. Sửa thời lượng khóa học");
        System.out.println("3. Sửa giảng viên");
        System.out.println("4. Trở về");
        System.out.println("================================");
        while (true) {
            System.out.print("Nhập lựa chọn : ");
            switch (input.nextLine()) {
                case "1":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String name = input.nextLine();
                        if (name.isBlank()) {
                            System.out.print("Tất cả các thông tin  về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        } else {
                            dao.editCourse(id, "name", name);
                            break;
                        }
                    }
                    break;
                case "2":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String strDuration = input.nextLine();
                        if (!strDuration.matches("\\d+")) {
                            System.out.print("Thời lượng phải là số vui lòng nhập lại : ");
                            continue;
                        }
                        int duration = Integer.parseInt(strDuration);
                        if (duration <= 0) {
                            System.out.print("Thời lượng khóa học phải lớn 0, vui lòng nhập lại : ");
                            continue;
                        } else {
                            dao.editCourse(id, "duration", String.valueOf(duration));
                            break;
                        }
                    }
                    break;
                case "3":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String strInstructor = input.nextLine();
                        if (strInstructor.isBlank()) {
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        } else {
                            dao.editCourse(id, "instructor", strInstructor);
                            break;
                        }
                    }
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    @Override
    public void findCourseByKey() {
        System.out.println("================================");
        System.out.print("Nhập từ khóa tìm kiếm : ");
        while (true) {
            String key = input.nextLine();
            if (key.isBlank()) {
                System.out.print("Từ khóa tìm kiếm không được bỏ trống : ");
                continue;
            }
            else {
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                List<Course> findCourse = dao.findCourse(key);
                for (Course course : findCourse) {
                    System.out.println(course);
                }
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            }
            System.out.println("Nhấn phím bất kỳ để trờ về : ");
            input.nextLine();
        }
    }

    @Override
    public void sortListCourse() {
        List<Course> courses = dao.listCourse();
        System.out.println("================================");
        System.out.println("1. Sắp xếp theo tên khóa học");
        System.out.println("2. Sắp xếp theo thời lượng khóa học");
        System.out.println("3. Trở về");
        System.out.println("================================");
        while (true) {
            System.out.print("Nhập lựa chọn : ");
            switch (input.nextLine()) {
                case "1":
                    List<Course> sortedCoursebyName = dao.sortListCourse(courses, "name");
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                    for (Course course : sortedCoursebyName) {
                        System.out.println(course);
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.println("Nhấn phím bất kỳ để trờ về : ");
                    input.nextLine();
                case "2":
                    List<Course> sortedCoursebyDuration = dao.sortListCourse(courses, "duration");
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                    for (Course course : sortedCoursebyDuration) {
                        System.out.println(course);
                    }
                    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.println("Nhấn phím bất kỳ để trờ về : ");
                    input.nextLine();
                case "3":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
            }
        }
    }

    @Override
    public void deleteCourse() {
        while (true) {
            System.out.print("Nhập id khóa : ");
            String key = input.nextLine();
            if (key.isBlank()) {
                System.out.println("Không được trống!");
                continue;
            }
            if(!key.matches("\\d+")) {
                System.out.println("Phải là số!");
                continue;
            }
            int id = Integer.parseInt(key);
            if(!dao.checkCourse(id)){
                System.out.println("id không tồn tại!");
                continue;
            }
            if(dao.checkCourseHasStudent(id)){
                System.out.println("Khóa học hiện đang có học viên!");
                continue;
            }
            else{
                dao.deleteCourse(id);
                break;
            }
        }
    }

    //endregion

    //region: studentmanagement
    @Override
    public void showListStudent() {
        List<Student> students = dao.listStudent();
        System.out.println("=================================");
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("=================================");
        System.out.println("Nhấn phím bất kỳ để trờ về : ");
        input.nextLine();
    }

    @Override
    public void addStudent() throws ParseException {
        menuChinh: while (true) {
            System.out.println("=================================");
            System.out.print("Nhập tên học viên : ");
            String name = input.nextLine();
            System.out.print("Nhập ngày sinh : ");
            String dob = input.nextLine();
            System.out.println("Nhập email học viên : ");
            String email = input.nextLine();
            System.out.println("Chọn giới tính(Nam/Nữ) : ");
            String strGender =  input.nextLine();
            System.out.println("Nhập số điện thoại : ");
            String phoneNumber = input.nextLine();
            System.out.println("Nhập mật khẩu : ");
            String password = input.nextLine();

            if(name.isBlank() || dob.isBlank() || email.isBlank() || strGender.isBlank() || phoneNumber.isBlank() || password.isBlank()) {
                System.out.println("Mọi thông tin không được để trống!");
                continue ;
            }

            if(!isValidDOB(dob)){
                System.out.println("Ngày sinh không hợp lệ hoặc sai định dạng!");
                continue ;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = null;
            try {
                birthDate = sdf.parse(dob);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if(!isValidEmail(email)){
                System.out.println("Email không hợp lệ hoặc sai định dạng!");
                continue ;
            }

            if(dao.checkEmailExists(email)){
                System.out.println("Email đã tồn tại");
                continue ;
            }

            if(!strGender.equals("Nam") && !strGender.equals("Nữ")){
                System.out.println("Giới tính không hợp lệ!");
                continue ;
            }

            Boolean gender = strGender.equals("Nam");

            if(!checkPhone(phoneNumber)){
                System.out.println("Số điện thoại không hợp lệ!");
                continue ;
            }
            dao.addStudent(name, birthDate, email, gender, phoneNumber, password);
            menuCon : while (true) {
                System.out.print("Nhập -1 để quay về , 1 để tiếp túc thêm hóa học : ");
                switch (input.nextLine()) {
                    case "1":
                        break menuCon;
                    case "-1":
                        return;
                    default:
                        System.out.println("Lựa trọn Invalid");
                }
            }

        }
    }

    @Override
    public void editStudent() throws ParseException {
        System.out.println("================================");
        System.out.print("Nhập id học viên để edit: ");
        int id = Integer.parseInt(input.nextLine());
        System.out.println("Nhập thành phần muốn sửa :");
        System.out.println("1. Sửa tên học viên");
        System.out.println("2. Sửa ngày sinh học viên");
        System.out.println("3. Sửa email");
        System.out.println("4. Sửa giới tính");
        System.out.println("5. Sửa số điện thoại");
        System.out.println("6. Sửa mật khẩu");
        System.out.println("7. Trở về");
        System.out.println("================================");
        while (true) {
            System.out.print("Nhập lựa chọn : ");
            switch (input.nextLine()) {
                case "1":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String name = input.nextLine();
                        if(name.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        else{
                            dao.editStudent(id, "name", name);
                            break;
                        }
                    }
                    break;
                case "2":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String dob = input.nextLine();
                        if(dob.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        if(!isValidDOB(dob)){
                            System.out.println("Ngày sinh không hợp lệ hoặc sai định dạng!");
                            continue ;
                        }
                        else{
                            dao.editStudent(id, "dob", dob);
                            break;
                        }
                    }
                    break;
                case "3":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String email = input.nextLine();
                        if(email.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        if(!isValidEmail(email)){
                            System.out.println("Email không hợp lệ hoặc sai định dạng!");
                            continue ;
                        }
                        if(dao.checkEmailExists(email)){
                            System.out.println("Email không hợp lệ hoặc sai định dạng!");
                            continue ;
                        }
                        else {
                            dao.editStudent(id, "email", email);
                            break;
                        }
                    }
                    break;
                case "4":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String gender = input.nextLine();
                        if(gender.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        if(!gender.equals("Nam")  && !gender.equals("Nữ")){
                            System.out.println("Giới tính không hợp lệ!");
                            continue ;
                        }
                        if(gender.equals("Nam")){
                            dao.editStudent(id, "gender", "1");
                            break;
                        }
                        else{
                            dao.editStudent(id, "gender", "0");
                            break;
                        }
                    }
                    break;
                case "5":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String phoneNumber = input.nextLine();
                        if(phoneNumber.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        if(!checkPhone(phoneNumber)){
                            System.out.println("Số điện thoại không hợp lệ!");
                            continue ;
                        }
                        else {
                            dao.editStudent(id, "phone", phoneNumber);
                            break;
                        }
                    }
                    break;
                case "6":
                    System.out.print("Nhập nội dung sửa đổi : ");
                    while (true) {
                        String password = input.nextLine();
                        if(password.isBlank()){
                            System.out.print("Tất cả các thông tin về khóa học không được để trống vui lòng nhập lại : ");
                            continue;
                        }
                        else {
                            dao.editStudent(id, "password", password);
                            break;
                        }
                    }
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    @Override
    public void findStudent() {
        System.out.println("================================");
        System.out.println("1. Tìm theo tên");
        System.out.println("2. Tìm theo email");
        System.out.println("3. Trở về");
        System.out.println("================================");
        while (true) {
            System.out.println("Nhập lựa chọn : ");
            String key = new String();
            switch (input.nextLine()) {
                case "1":
                    key = input.nextLine();
                    if(key.isBlank()){
                        System.out.print("Từ khóa tìm kiếm không được bỏ trống : ");
                        continue;
                    }
                    else {
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        List<Student> students = dao.findStudent(key, "name");
                        for (Student student : students) {
                            System.out.println(student);
                        }
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        break;
                    }
                case "2":
                    key = input.nextLine();
                    if(key.isBlank()){
                        System.out.print("Từ khóa tìm kiếm không được bỏ trống : ");
                        continue;
                    }
                    else {
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        List<Student> students = dao.findStudent(key, "email");
                        for (Student student : students) {
                            System.out.println(student);
                        }
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        break;
                    }
                case "3":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }

    }

    @Override
    public void sortListStudent() {
        List<Student> students = dao.listStudent();
        System.out.println("================================");
        System.out.println("1. Sắp xếp theo tên");
        System.out.println("2. Sắp xếp theo email");
        System.out.println("3. Trở về");
        System.out.println("================================");
         while (true) {
             System.out.print("Nhập lựa chọn : ");
             switch (input.nextLine()) {
                 case "1":
                     List<Student> students1 = dao.sortListStudent(students,  "name");
                     System.out.println("================================");
                     for (Student student : students1) {
                         System.out.println(student);
                     }
                     System.out.println("================================");
                     break;
                 case "2":
                     List<Student> students2 = dao.sortListStudent(students,  "email");
                     System.out.println("=================================");
                     for (Student student : students2) {
                         System.out.println(student);
                     }
                     System.out.println("===============================");
                     break;
                 case "3":
                    return;
                 default:
                     System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
             }
         }
    }

    @Override
    public void deleteStudent() {
        while (true) {
            System.out.print("Nhập id khóa : ");
            String strid = input.nextLine();
            if (strid.isBlank()) {
                System.out.println("Không được trống!");
                continue;
            }
            if (!strid.matches("\\d+")) {
                System.out.println("Phải là số!");
                continue;
            }
            int id = Integer.parseInt(strid);
            if (!dao.checkStudentExists(id)) {
                System.out.println("Học viên không tồn tại!");
                continue;
            }
            if (dao.checkStudentAttend(id)) {
                System.out.println("Học viên đã đăng ký khóa học không xóa được!");
                continue;
            } else {
                dao.deleteStudent(id);
                break;
            }
        }
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


