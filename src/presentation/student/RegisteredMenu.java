package presentation.student;

import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import model.dto.EnrollmentDetailDTO;

import java.util.List;
import java.util.Scanner;

public class RegisteredMenu {
    Scanner sc = new Scanner(System.in);
    IStudentDAO dao = new StudentDAOImpl();
    StudentServicesImpl services = new StudentServicesImpl(dao);
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
                    showMyHistory(studentID);
                    break;
                case "2":
                    handleSortMenu(studentID);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    public void showMyHistory(int studentId) {
        System.out.println("========== LỊCH SỬ ĐĂNG KÝ ==========");

        List<EnrollmentDetailDTO> list = services.getHistory(studentId);

        if (list.isEmpty()) {
            System.out.println("⚠️ Bạn chưa đăng ký khóa học nào!");
        } else {
            System.out.printf("║ %-20s ║ %-15s ║ %-10s ║\n", "Khóa học", "Ngày ĐK", "Trạng thái");

            for (EnrollmentDetailDTO dto : list) {
                System.out.printf("║ %-20s ║ %-15s ║ %-10s ║\n",
                        dto.getCourseName(),
                        dto.getRegisteredAt(),
                        dto.getStatus());
            }
        }
    }


    public void handleSortMenu(int studentId) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("======= SẮP XẾP LỊCH SỬ =======");
            System.out.println("1. Theo tên khóa học (A-Z)");
            System.out.println("2. Theo tên khóa học (Z-A)");
            System.out.println("3. Theo ngày đăng ký (Mới nhất)");
            System.out.println("4. Theo ngày đăng ký (Cũ nhất)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            String choice = sc.nextLine();

            // Biến chuẩn bị gửi sang Service
            String sortBy = "";
            String sortDir = "";

            switch (choice) {
                case "1": sortBy = "name"; sortDir = "td"; break;
                case "2": sortBy = "name"; sortDir = "gd"; break;
                case "3": sortBy = "time"; sortDir = "gd"; break; // Mới nhất là giảm dần
                case "4": sortBy = "time"; sortDir = "td"; break;
                case "0": return;
                default: System.out.println("❌ Chọn sai!"); continue;
            }

            // --- GỌI SERVICE ---
            List<EnrollmentDetailDTO> sortedList = services.sortEnrollment(studentId, sortBy, sortDir);

            // --- IN KẾT QUẢ ---
            if (sortedList.isEmpty()) {
                System.out.println("⚠️ Chưa có dữ liệu.");
            } else {
                System.out.printf("║ %-20s ║ %-15s ║ %-10s ║\n", "Khóa học", "Ngày ĐK", "Trạng thái");
                for (EnrollmentDetailDTO dto : sortedList) {
                    System.out.printf("║ %-20s ║ %-15s ║ %-10s ║\n",
                            dto.getCourseName(), dto.getRegisteredAt(), dto.getStatus());
                }
            }
            // Dừng lại cho user đọc xong mới hiện lại menu
            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }
}
