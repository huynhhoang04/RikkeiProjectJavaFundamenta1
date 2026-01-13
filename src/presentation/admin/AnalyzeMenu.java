package presentation.admin;

import business.impl.AdminSevicesImpl;

import java.util.Scanner;

public class AnalyzeMenu {
    Scanner sc = new Scanner(System.in);
    AdminSevicesImpl sevices = new AdminSevicesImpl();
    public void showMenu(){
        menuChinh: while(true){
            System.out.println("============Thống kê=============");
            System.out.println("1. Thống kê số lượng khóa học và tổng học viên");
            System.out.println("2. Thống kê tổng số học viên theo từng khóa");
            System.out.println("3. Thống kê top 5 khóa học đông sinh viên nhất");
            System.out.println("4. Liệt kê các khóa học có trên 10 học viên");
            System.out.println("5. Trở về");
            System.out.println("================================");
            System.out.print("Nhập lựa trọn : ");
            switch(sc.nextLine()){
                case "1":
                    sevices.showTotalCoursesAndStudents();
                    break;
                case "2":
                    sevices.sgowTotalStudentsByCourse();
                    break;
                case "3":
                    sevices.Top5CourseWithStudents();
                    break;
                case "4":
                    sevices.CourseWithMoreThan10Students();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }
}
