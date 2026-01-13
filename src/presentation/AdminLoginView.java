package presentation;

import business.impl.AdminSevicesImpl;

import java.util.Scanner;

public class AdminLoginView {
    Scanner sc = new Scanner(System.in);
    AdminSevicesImpl sevices = new AdminSevicesImpl();
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
                if(!sevices.authAdmin(name,pass)){
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
