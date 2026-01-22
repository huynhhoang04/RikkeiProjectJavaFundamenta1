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
                System.out.print("➜ Enter UserName(exit để trở về) : ");
                String name = sc.nextLine().trim();
                if(name.equals("exit")) return;
                System.out.print("➜ Enter Password(exit để trở về) : ");
                String pass = sc.nextLine().trim();
                if(pass.equals("exit")) return;
                System.out.println("═══════════════════════════════════");
                if(name.isEmpty() || pass.isEmpty()){
                    System.out.println("⚠ Tên và mật khẩu không được để trống. Vui lòng nập lại!");
                }
                Admin admin = services.login(name,pass);
                if(admin == null){
                    System.out.println("⚠ Sai tên hoặc mật khẩu");
                }
                else{
                    AdminMenuView adminMenuView = new AdminMenuView(sc,services);
                    System.out.println("✔ Đăng nhập thành công!");
                    boolean logout = adminMenuView.showAdminMenu();
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
