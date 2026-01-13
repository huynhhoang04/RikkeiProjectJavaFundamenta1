package presentation;

import business.impl.StudentServicesImpl;
import presentation.student.RegisteredMenu;
import presentation.student.ReviewCourseMenu;

import java.util.Scanner;

public class StudentMenuView {
    Scanner sc = new Scanner(System.in);
    ReviewCourseMenu rcm = new ReviewCourseMenu();
    RegisteredMenu rm = new RegisteredMenu();
    StudentServicesImpl services = new StudentServicesImpl();
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
                    services.registerCourse(id);
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
}
