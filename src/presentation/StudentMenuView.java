package presentation;

import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import presentation.student.RegisteredMenu;
import presentation.student.ReviewCourseMenu;

import java.util.Scanner;

public class StudentMenuView {
    Scanner sc = new Scanner(System.in);
    IStudentDAO dao = new StudentDAOImpl();
    ReviewCourseMenu rcm = new ReviewCourseMenu();
    RegisteredMenu rm = new RegisteredMenu();
    StudentServicesImpl services = new StudentServicesImpl(dao);
    public boolean showStudentMenu(int id){
        while (true){
            System.out.println("===========Menu Học Viên========");
            System.out.println("1. Xem danh sách hóa học");
            System.out.println("2. Đăng kí khóa học");
            System.out.println("3. Xem khóa học đã đăng kí");
            System.out.println("4. Hủy đăng kí (nếu chưa bắt đầu)");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("================================");
            System.out.println("Nhập lựa trọn : ");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    rcm.showMenu();
                    break;
                case 2:
                    handleRegisterCourse(id);
                    break;
                case 3:
                    rm.showMenu(id);
                    break;
                case 4:
                    services.cancerEnrollment(id);
                    break;
                case 5:
                    services.changePassword(id);
                    break;
                case 6:
                    return true;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    public void handleRegisterCourse(int studentId) {

        while (true) {
            System.out.println("========================================");
            System.out.print("Nhập ID khóa học muốn đăng ký (hoặc 0 để thoát): ");

            int courseId = 0;
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;
                courseId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("❌ Lỗi: Vui lòng nhập số nguyên!");
                continue;
            }

            if (courseId == 0) break;
            boolean isSuccess = services.registerCourse(studentId, courseId);

            if (isSuccess) {
                System.out.println("✅ Đăng ký khóa học thành công!");
                break;
            } else {
                System.err.println("❌ Đăng ký thất bại! (Khóa học không tồn tại hoặc đã đăng ký rồi)");
            }
        }
    }
}
