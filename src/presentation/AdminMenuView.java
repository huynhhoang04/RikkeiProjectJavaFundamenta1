package presentation;

import business.IAdminSevices;
import presentation.admin.AnalyzeMenu;
import presentation.admin.CourseManagementMenu;
import presentation.admin.EnrollmentManagementMenu;
import presentation.admin.StudentManagementMenu;

import java.util.Scanner;

public class AdminMenuView {

    private Scanner sc ;
    private IAdminSevices services;

    public AdminMenuView(Scanner sc, IAdminSevices services) {
        this.sc = sc;
        this.services = services;
    }

    public boolean showAdminMenu(){
        while (true){
            try {
                System.out.println("═══════════════════════════════════");
                System.out.println("☰ Menu Admin");
                System.out.println("1. Quản lý khóa học");
                System.out.println("2. Quản lý học viên");
                System.out.println("3. Quản lý đăng ký học");
                System.out.println("4. Thống kê");
                System.out.println("5. Đăng xuất");
                System.out.println("═══════════════════════════════════");
                System.out.print("➜ Nhập lựa trọn : ");
                String choice = sc.nextLine().trim();
                if (choice.isEmpty()) continue;
                switch (choice){
                    case "1":
                        CourseManagementMenu  cm = new CourseManagementMenu(sc,services);
                        cm.showMenu();
                        break;
                    case "2":
                        StudentManagementMenu sm = new StudentManagementMenu(sc,services);
                        sm.showMenu();
                        break;
                    case "3":
                        EnrollmentManagementMenu em = new EnrollmentManagementMenu(sc,services);
                        em.showMenu();
                        break;
                    case "4":
                        AnalyzeMenu am = new AnalyzeMenu(sc,services);
                        am.showMenu();
                        break ;
                    case "5":
                        return true;
                    default:
                        System.out.println("⚠ Lựa trọn không hợp lệ!");
                }
            }
            catch (Exception e){
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
