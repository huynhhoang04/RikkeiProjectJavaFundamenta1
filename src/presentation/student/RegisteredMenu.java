package presentation.student;

import business.impl.StudentServicesImpl;

import java.util.Scanner;

public class RegisteredMenu {
    Scanner sc = new Scanner(System.in);
    StudentServicesImpl services = new StudentServicesImpl();
    public void showMenu(int studentID) {
        while(true){
            System.out.println("===========Menu Học Viên========");
            System.out.println("1. Xem khóa học đã đăng ký");
            System.out.println("2. Sắp sếp khóa học");
            System.out.println("3. Trở về");
            System.out.println("================================");
            System.out.println("Nhập lựa trọn : ");
            switch (sc.nextLine()){
                case "1":
                    services.showRegistreredTicket(studentID);
                    break;
                case "2":
                    services.showSortEnrollment(studentID);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }
}
