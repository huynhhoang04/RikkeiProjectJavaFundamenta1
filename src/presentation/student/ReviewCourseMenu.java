package presentation.student;

import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;

import java.util.Scanner;

public class ReviewCourseMenu {
    Scanner sc = new Scanner(System.in);
    IStudentDAO dao = new StudentDAOImpl();
    StudentServicesImpl services = new StudentServicesImpl(dao);

    public void showMenu(){
        menuChinh : while(true){
            try {
                System.out.println("================================");
                System.out.println("1. Xem danh sách hóa học đang có");
                System.out.println("2. Tìm kiếm khóa học ");
                System.out.println("3. Trở về");
                System.out.println("================================");
                System.out.println("Nhập lựa chọn : ");
                switch (sc.nextLine()){
                    case "1":
                        services.showListCourses();
                        break;
                    case "2":
                        services.findCourse();
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Lựa trọn Invalid");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
