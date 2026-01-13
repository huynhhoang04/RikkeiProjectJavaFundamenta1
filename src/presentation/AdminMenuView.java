package presentation;

import presentation.admin.AnalyzeMenu;
import presentation.admin.CourseManagementMenu;
import presentation.admin.EnrollmentManagementMenu;
import presentation.admin.StudentManagementMenu;

import java.util.Scanner;

public class AdminMenuView {

    Scanner sc = new Scanner(System.in);
    CourseManagementMenu  cm = new CourseManagementMenu();
    StudentManagementMenu sm = new StudentManagementMenu();
    EnrollmentManagementMenu em = new EnrollmentManagementMenu();
    AnalyzeMenu am = new AnalyzeMenu();
    public boolean showAdminMenu(){
        menuChinh : while (true){
            System.out.println("============Menu Admin==========");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê");
            System.out.println("5. Đăng xuất");
            System.out.println("================================");
            System.out.print("Nhập lựa trọn : ");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    cm.showMenu();
                    break;
                case 2:
                    sm.showMenu();
                    break;
                case 3:
                    em.showMenu();
                    break;
                case 4:
                    am.showMenu();
                    break ;
                case 5:
                    return true;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }
}
