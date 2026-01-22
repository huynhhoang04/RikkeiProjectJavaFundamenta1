
import java.util.*;

import business.IAdminServices;
import business.IStudentServices;
import business.impl.AdminServicesImpl;
import business.impl.StudentServicesImpl;
import dao.IAdminDAO;
import dao.IStudentDAO;
import dao.impl.AdminDAOImpl;
import dao.impl.StudentDAOImpl;
import presentation.*;

public class Main {
    public static void main(String[] args) {
        // khởi tạo scanner dùng chung
        // khởi tạo và kết nối các tầng dao -> service -> view

        // 1. kết nối module admin
        Scanner sc = new Scanner(System.in);
        IAdminDAO adminDAO = new AdminDAOImpl();
        IAdminServices adminSevices = new AdminServicesImpl(adminDAO);
        AdminLoginView adminLogin = new AdminLoginView(sc, adminSevices);

        // 2. kết nối module student
        IStudentDAO studentDAO = new StudentDAOImpl();
        IStudentServices studentServices = new StudentServicesImpl(studentDAO);
        StudentLoginView studentLogin = new StudentLoginView(sc, studentServices);

        // vòng lặp chính của chương trình
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ Hệ Thông Quản Lý Đào Tạo");
            System.out.println("1.Login Admin");
            System.out.println("2.Login Student");
            System.out.println("3.Exit");
            System.out.println("═══════════════════════════════════");
            System.out.print("Nhập lựa trọn : ");
            switch (sc.nextLine()) {
                case "1":
                    // vào màn hình admin
                    adminLogin.showAdminLogin();
                    break;
                case "2":
                    // vào màn hình student
                    studentLogin.showStudentLogin();
                    break;
                case "3":
                    System.exit(36);
                default:
                    System.out.println("Lựa chọn Invalid ");
            }

        }
    }

}