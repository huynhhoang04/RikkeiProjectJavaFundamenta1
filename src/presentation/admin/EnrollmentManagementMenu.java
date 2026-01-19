package presentation.admin;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;

import java.util.Scanner;

public class EnrollmentManagementMenu {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl sevices = new AdminSevicesImpl(dao);
    public void showMenu(){
        menuChinh: while(true){
            System.out.println("======Menu Quản lý đăng ký======");
            System.out.println("1. Hiển thị danh sách sinh viên đăng ký theo từng khóa học");
            System.out.println("2. Duyệt sinh viên đăng ký khóa học");
            System.out.println("3. Trở về");
            System.out.println("================================");
            System.out.print("Nhập lựa trọn : ");
            switch(sc.nextLine()){
                case "1":
                    sevices.showRegistered();
                    break;
                case "2":
                    sevices.comfirmRegisteration();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }
}
