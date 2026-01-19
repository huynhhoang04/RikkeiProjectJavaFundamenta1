package presentation.student;

import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import model.Course;

import java.util.List;
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
                        handleShowListCourses();
                        break;
                    case "2":
                        handleFindCourse();
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

    public void handleShowListCourses() {
        System.out.println("\n========== DANH SÁCH KHÓA HỌC ==========");

        // Gọi Service lấy danh sách
        List<Course> list = services.showListCourses();

        if (list.isEmpty()) {
            System.out.println("⚠️ Hiện chưa có khóa học nào.");
        } else {
            // In tiêu đề bảng
            System.out.printf("| %-5s | %-25s | %-15s |\n", "ID", "Tên Khóa Học", "Mô tả");
            System.out.println("-------------------------------------------------------");

            for (Course c : list) {
                // Giả sử Course có getId, getName, getDescription
                // Nếu course của ông chỉ có toString, ông có thể dùng System.out.println(c);
                System.out.printf("| %-5d | %-25s | %-15s |\n", c.getId(), c.getName(), c.getInstructor());
            }
        }

        // Dừng màn hình tí cho người dùng đọc
        Scanner sc = new Scanner(System.in);
        System.out.println("\nẤn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleFindCourse() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== TÌM KIẾM KHÓA HỌC ==========");
            System.out.print("Nhập từ khóa tên môn học (hoặc ấn Enter để thoát): ");
            String key = sc.nextLine().trim();

            // Nếu không nhập gì thì thoát
            if (key.isEmpty()) break;

            // Gọi Service tìm kiếm
            List<Course> list = services.findCourse(key);

            if (list.isEmpty()) {
                System.out.println("❌ Không tìm thấy khóa học nào chứa từ khóa: " + key);
            } else {
                System.out.println("✅ Kết quả tìm kiếm:");
                System.out.printf("| %-5s | %-25s | %-15s |\n", "ID", "Tên Khóa Học", "Mô tả");
                System.out.println("-------------------------------------------------------");

                for (Course c : list) {
                    System.out.printf("| %-5d | %-25s | %-15s |\n", c.getId(), c.getName(), c.getInstructor());
                }
            }
        }
    }
}
