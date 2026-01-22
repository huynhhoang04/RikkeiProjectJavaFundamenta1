package presentation;

import business.IStudentServices;
import model.Student;

import java.util.Scanner;

public class StudentLoginView {
    private Scanner sc ;
    private IStudentServices services;

    public StudentLoginView(Scanner sc, IStudentServices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showStudentLogin(){
        while (true){
            try{
                System.out.println("═══════════════════════════════════");

                // nhập email
                System.out.print("Enter Email Address(exit để trở về) : ");
                String email = sc.nextLine().trim();
                if(email.equals("exit")) return;

                // nhập password
                System.out.print("Enter Password(exit để trở về) : ");
                String pass = sc.nextLine().trim();
                if(pass.equals("exit")) return;
                System.out.println("═══════════════════════════════════");
                if(email.isEmpty() || pass.isEmpty()){
                    System.out.println("⚠ Email và mật khẩu không được để trống");
                }

                // gọi service login
                Student student = services.login(email,pass);
                if(student == null){
                    System.out.println("⚠ Sai email hoặc mật khẩu!");
                }
                else{
                    // chuyển sang menu học viên
                    System.out.println("✔ Đăng nhập thành công!Id : " + student.getId());
                    StudentMenuView studentMenuView = new StudentMenuView(sc,services);
                    boolean logout = studentMenuView.showStudentMenu(student.getId());
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
