package presentation;

import business.impl.StudentServicesImpl;
import dao.impl.StudentDAOImpl;
import model.Student;

import java.util.Scanner;

public class StudentLoginView {
    Scanner sc = new Scanner(System.in);
    StudentDAOImpl dao = new StudentDAOImpl();
    StudentServicesImpl services = new StudentServicesImpl(dao);
    public void showStudentLogin(){
        while (true){
            try{
                System.out.println("Enter Email address :");
                String email = sc.nextLine().trim();

                System.out.println("Enter Password :");
                String pass = sc.nextLine().trim();

                if(email.isEmpty() || pass.isEmpty()){
                    System.out.println("Email và mật khẩu không được để trống");
                }

                Student student = services.login(email,pass);
                if(student == null){
                    System.out.println("Sai email hoặc mật khẩu!");
                }
                else{
                    System.out.println("Đăng nhập thành công!Id : " + student.getId());
                    StudentMenuView studentMenuView = new StudentMenuView();
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
