package presentation.admin;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;

import java.util.Map;
import java.util.Scanner;

public class AnalyzeMenu {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl services = new AdminSevicesImpl(dao);
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
                    handleTotalStats();
                    break;
                case "2":
                    handleAllCoursesStats();
                    break;
                case "3":
                    handleTop5Stats();
                    break;
                case "4":
                    handleThresholdStats();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    private void printTable(Map<String, Integer> data, boolean showRank) {
        if (data == null || data.isEmpty()) {
            System.out.println("⚠️ Không có dữ liệu nào.");
            return;
        }

        System.out.println("-------------------------------------------------------");
        if (showRank) {
            System.out.printf("| %-5s | %-35s | %-8s |\n", "TOP", "Tên Khóa Học", "Số HV");
        } else {
            System.out.printf("| %-35s | %-10s |\n", "Tên Khóa Học", "Số Lượng");
        }
        System.out.println("-------------------------------------------------------");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (showRank) {
                System.out.printf("| %-5d | %-35s | %-8d |\n", rank++, entry.getKey(), entry.getValue());
            } else {
                System.out.printf("| %-35s | %-10d |\n", entry.getKey(), entry.getValue());
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    private void handleTotalStats() {
        // Hàm này trả về Map có key "total_course" và "total_student"
        Map<String, Integer> stats = services.showTotalCoursesAndStudents();

        System.out.println("\n--- TỔNG QUAN HỆ THỐNG ---");
        // Dùng getOrDefault để tránh lỗi null nếu map rỗng
        System.out.println("📚 Tổng số khóa học : " + stats.getOrDefault("courses", 0));
        System.out.println("👨‍🎓 Tổng số học viên : " + stats.getOrDefault("students", 0));
    }

    private void handleAllCoursesStats() {
        System.out.println("\n--- SỐ LƯỢNG HỌC VIÊN THEO KHÓA ---");
        Map<String, Integer> data = services.showTotalStudentsByCourse();
        printTable(data, false);
    }

    private void handleTop5Stats() {
        System.out.println("\n--- TOP 5 KHÓA HỌC ĐÔNG NHẤT ---");
        Map<String, Integer> data = services.Top5CourseWithStudents();
        printTable(data, true); // true để hiện cột Top 1, 2, 3
    }

    private void handleThresholdStats() {
        System.out.println("\n--- CÁC KHÓA HỌC > 10 HỌC VIÊN ---");
        Map<String, Integer> data = services.CourseWithMoreThan10Students();
        printTable(data, false);
    }
}
