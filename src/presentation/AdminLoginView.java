package presentation;

import business.IAdminServices;
import model.Admin;

import java.util.Scanner;

public class AdminLoginView {
    private Scanner sc ;
    private IAdminServices services;

    public AdminLoginView(Scanner sc, IAdminServices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showAdminLogin(){
        while(true){
            try {
                System.out.println("═══════════════════════════════════");

                // nhập username
                System.out.print("➜ Enter UserName(exit để trở về) : ");
                String name = sc.nextLine().trim();
                if(name.equals("exit")) return;

                // nhập password
                System.out.print("➜ Enter Password(exit để trở về) : ");
                String pass = sc.nextLine().trim();
                if(pass.equals("exit")) return;
                System.out.println("═══════════════════════════════════");

                // validate rỗng
                if(name.isEmpty() || pass.isEmpty()){
                    System.out.println("⚠ Tên và mật khẩu không được để trống. Vui lòng nập lại!");
                }

                // gọi service xử lý đăng nhập
                Admin admin = services.login(name,pass);
                if(admin == null){
                    System.out.println("⚠ Sai tên hoặc mật khẩu");
                }
                else{
                    // chuyển sang menu admin
                    AdminMenuView adminMenuView = new AdminMenuView(sc,services);
                    System.out.println("✔ Đăng nhập thành công!");
                    boolean logout = adminMenuView.showAdminMenu();
                    // nếu user chọn đăng xuất thì thoát vòng lặp view này để chuyển sang menu admin
                    if(logout){
                        return;
                    }
                }
            }
            catch (Exception e){
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
