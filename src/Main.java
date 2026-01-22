
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
        Scanner sc = new Scanner(System.in);
        IAdminDAO adminDAO = new AdminDAOImpl();
        IAdminServices adminSevices = new AdminServicesImpl(adminDAO);
        AdminLoginView adminLogin = new AdminLoginView(sc, adminSevices);

        IStudentDAO studentDAO = new StudentDAOImpl();
        IStudentServices studentServices = new StudentServicesImpl(studentDAO);
        StudentLoginView studentLogin = new StudentLoginView(sc, studentServices);

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
                    adminLogin.showAdminLogin();
                    break;
                case "2":
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