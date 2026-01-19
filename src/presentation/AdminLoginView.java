package presentation;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Admin;

import java.util.Scanner;

public class AdminLoginView {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl sevices = new AdminSevicesImpl(dao);
    public void showAdminLogin(){
        menuChinh : while(true){
            try {
                System.out.print("Enter UserName :");
                String name = sc.nextLine().trim();

                System.out.print("Enter Password :");
                String pass = sc.nextLine().trim();

                if(name.isEmpty() || pass.isEmpty()){
                    System.out.println("Tên và mật khẩu không được để trống");
                }

                Admin admin = sevices.login(name,pass);
                if(admin == null){
                    System.out.println("Sai tên hoặc mật khẩu");
                }
                else{
                    AdminMenuView adminMenuView = new AdminMenuView();
                    System.out.println("Đăng nhập thành công!");
                    boolean logout = adminMenuView.showAdminMenu();
                    if(logout){
                        return;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
