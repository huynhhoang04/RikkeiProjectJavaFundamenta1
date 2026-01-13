package presentation;

import business.impl.StudentServicesImpl;
import dao.impl.StudentDAOImpl;
import model.Student;

import java.util.Scanner;

public class StudentLoginView {
    Scanner sc = new Scanner(System.in);
    StudentServicesImpl services = new StudentServicesImpl();
    StudentDAOImpl dao = new StudentDAOImpl();
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
                if(!services.authStudent(email, pass)){
                    System.out.println("Sai tên hoặc mật khẩu");
                }
                else{
                    Student student = dao.checkStudent(email, pass);
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
