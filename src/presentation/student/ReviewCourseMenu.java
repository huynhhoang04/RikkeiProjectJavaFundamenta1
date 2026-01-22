package presentation.student;

import business.IStudentServices;
import model.Course;

import java.util.List;
import java.util.Scanner;

public class ReviewCourseMenu {
    private Scanner sc ;
    private IStudentServices services;

    public ReviewCourseMenu(Scanner sc, IStudentServices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu(int id){
        while(true){
            showSuggestCourse(id);
            System.out.println("═══════════════════════════════════");
            System.out.println("1. Xem danh sách hóa học đang có");
            System.out.println("2. Tìm kiếm khóa học ");
            System.out.println("3. Trở về");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa trọn : ");
            String choice = sc.nextLine().trim();
            if (choice.isEmpty()) continue;
            switch (choice) {
                case "1":
                    handleShowListCourses();
                    break;
                case "2":
                    handleFindCourse();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

    private void printCourseList(List<Course> list) {
        System.out.println("┌───────┬────────────────────────────────┬─────────────────┬─────────────────┬────────────┐");
        System.out.printf("│ %-5s │ %-30s │ %-15s │ %-15s │ %-10s │\n", "ID", "Tên Khóa Học", "Thời lượng(giờ)", "Giảng viên", "Ngày tạo");
        System.out.println("├───────┼────────────────────────────────┼─────────────────┼─────────────────┼────────────┤");
        for (Course c : list) {
            System.out.printf("│ %-5d │ %-30s │ %-15d │ %-15s │ %-10s │\n",
                    c.getId(), c.getName(), c.getDuration(), c.getInstructor() , c.getCreated_at());
        }
        System.out.println("└───────┴────────────────────────────────┴─────────────────┴─────────────────┴────────────┘");
    }

    public void handleShowListCourses() {
        System.out.println("𝄜 DANH SÁCH KHÓA HỌC");
        List<Course> list = services.showListCourses();
        printCourseList(list);
        System.out.println("\nẤn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleFindCourse() {
        System.out.println("═══════════════════════════════════");
        System.out.println("⌕ TÌM KIẾM KHÓA HỌC ");
        System.out.print("Nhập từ khóa tên môn học (hoặc ấn exit để thoát): ");
        String key = sc.nextLine().trim();
        System.out.println("═══════════════════════════════════");
        if (key.equalsIgnoreCase("exit")) return;

        List<Course> list = services.findCourse(key);

        if (list.isEmpty()) {
            System.out.println("⚠ Không tìm thấy khóa học nào chứa: \"" + key + "\"");
        } else {
            System.out.println("✔ Tìm thấy " + list.size() + " kết quả:");
            printCourseList(list);
        }
        System.out.println("\nẤn Enter để quay lại...");
        sc.nextLine();
    }

    private void showSuggestCourse(int studentId) {
        List<Course> list = services.getSuggestedCourse(studentId);
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│                     ĐỀ XUẤT KHÓA HỌC PHÙ HỢP                    │");
        System.out.println("├────────────────────────────────┬─────────────────┬──────────────┤");
        System.out.printf("│ %-30s │ %-15s │ %-12s │\n", "Tên khóa", "Giảng viên", "Thời lượng");
        System.out.println("├────────────────────────────────┼─────────────────┼──────────────┤");
        for (Course c : list) {
            System.out.printf("│ %-30s │ %-15s │ %-12s │\n", c.getName(), c.getInstructor(), c.getDuration());
        }
        System.out.println("└────────────────────────────────┴─────────────────┴──────────────┘");
    }
}
