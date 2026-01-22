package presentation.admin;

import business.IAdminSevices;

import java.util.Map;
import java.util.Scanner;

public class AnalyzeMenu {
    private Scanner sc ;
    private IAdminSevices services;

    public AnalyzeMenu(Scanner sc, IAdminSevices services) {
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
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

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

    private void handleTotalStats() {
        Map<String, Integer> stats = services.showTotalCoursesAndStudents();
        System.out.println("══════════════════════════════════════════");
        System.out.println("∑ TỔNG QUAN HỆ THỐNG ");
        System.out.println("📖 Tổng số khóa học : " + stats.getOrDefault("courses", 0));
        System.out.println("👤 Tổng số học viên : " + stats.getOrDefault("students", 0));
        System.out.println("══════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    private void handleAllCoursesStats() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("𝄜 SỐ LƯỢNG HỌC VIÊN THEO KHÓA ");
        Map<String, Integer> data = services.showTotalStudentsByCourse();
        printTable(data);
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    private void handleTop5Stats() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("♕ TOP 5 KHÓA HỌC ĐÔNG NHẤT ");
        Map<String, Integer> data = services.Top5CourseWithStudents();
        printTable(data);
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    private void handleThresholdStats() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("𝇕 CÁC KHÓA HỌC ĐẠT TRÊN 10 HỌC VIÊN ");
        Map<String, Integer> data = services.CourseWithMoreThan10Students();
        printTable(data);
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }
}
