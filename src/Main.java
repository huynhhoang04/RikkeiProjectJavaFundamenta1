
import java.util.*;
import presentation.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AdminLoginView adminLogin = new AdminLoginView();
        StudentLoginView studentLogin = new StudentLoginView();

        while (true) {
            System.out.println("====Hệ Thông Quản Lý Đào Tạo====");
            System.out.println("1.Login Admin");
            System.out.println("2.Login Student");
            System.out.println("3.Exit");
            System.out.println("================================");
            System.out.print("Nhập lựa trọn : ");
            int in = sc.nextInt();

            switch (in) {
                case 1:
                    adminLogin.showAdminLogin();
                    break;
                case 2:
                    studentLogin.showStudentLogin();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn Invalid ");
            }

        }
    }

}