package presentation.admin;

import business.IAdminServices;

import java.util.Map;
import java.util.Scanner;

public class StatisticsMenu {
    private Scanner sc ;
    private IAdminServices services;

    public StatisticsMenu(Scanner sc, IAdminServices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu(){
        while(true){
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ Thống kê ");
            System.out.println("1. Thống kê số lượng khóa học và tổng học viên");
            System.out.println("2. Thống kê tổng số học viên theo từng khóa");
            System.out.println("3. Thống kê top 5 khóa học đông sinh viên nhất");
            System.out.println("4. Liệt kê các khóa học có trên 10 học viên");
            System.out.println("5. Trở về");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa trọn : ");
            String choice = sc.nextLine().trim();
            if (choice.isEmpty()) continue;
            switch(choice){
                case "1":
                    handleTotalStats(); // tổng quan
                    break;
                case "2":
                    handleAllCoursesStats(); // chi tiết từng khóa
                    break;
                case "3":
                    handleTop5Stats(); // top 5
                    break;
                case "4":
                    handleThresholdStats(); // khóa trên 10 hv
                    break;
                case "5":
                    return;
                default:
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

    // in bảng thống kê từ map
    private void printTable(Map<String, Integer> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("⚠️ Không có dữ liệu nào.");
            return;
        }

        System.out.println("┌───────┬─────────────────────────────────────┬──────────┐");
        System.out.printf("│ %-5s │ %-35s │ %-8s │\n", "STT", "Tên Khóa Học", "Số HV");
        System.out.println("├───────┼─────────────────────────────────────┼──────────┤");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            System.out.printf("│ %-5d │ %-35s │ %-8d │\n", rank++, entry.getKey(), entry.getValue());
        }
        System.out.println("└───────┴─────────────────────────────────────┴──────────┘");
    }

    // thống kê tổng quan
    private void handleTotalStats() {
        Map<String, Integer> stats = services.getSystemStatistics();
        System.out.println("══════════════════════════════════════════");
        System.out.println("∑ TỔNG QUAN HỆ THỐNG ");
        System.out.println("📖 Tổng số khóa học : " + stats.getOrDefault("courses", 0));
        System.out.println("👤 Tổng số học viên : " + stats.getOrDefault("students", 0));
        System.out.println("══════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    // thống kê số lượng hv theo khóa
    private void handleAllCoursesStats() {
        System.out.println("𝄜 SỐ LƯỢNG HỌC VIÊN THEO KHÓA ");
        Map<String, Integer> data = services.getStudentCountByCourse();
        printTable(data);
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    // thống kê top 5
    private void handleTop5Stats() {
        System.out.println("♕ TOP 5 KHÓA HỌC ĐÔNG NHẤT ");
        Map<String, Integer> data = services.getTop5PopularCourses();
        printTable(data);
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    // thống kê khóa học trên 10
    private void handleThresholdStats() {
        System.out.println("𝇕 CÁC KHÓA HỌC ĐẠT TRÊN 10 HỌC VIÊN ");
        Map<String, Integer> data = services.getCoursesWithHighEnrollment();
        printTable(data);
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }
}
