package presentation.student;

import business.IStudentServices;
import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import model.dto.EnrollmentDetailDTO;

import java.util.List;
import java.util.Scanner;

public class RegisteredMenu {
    private Scanner sc ;
    private IStudentServices services;

    public RegisteredMenu(Scanner sc, IStudentServices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu(int studentID) {
        while(true){
            System.out.println("═══════════════════════════════════");
            System.out.println("1. Xem khóa học đã đăng ký");
            System.out.println("2. Sắp sếp khóa học");
            System.out.println("3. Trở về");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa trọn : ");
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
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

    private void printList(List<EnrollmentDetailDTO> list) {
        System.out.println("┌───────┬──────────────────────┬────────────────────────────────┬──────────────┬────────────┐");
        System.out.printf("│ %-5s │ %-20s │ %-30s │ %-12s │ %-10s │\n",
                "ID", "Tên Học Viên", "Tên Khóa", "Ngày ĐK", "Trạng Thái");
        System.out.println("├───────┼──────────────────────┼────────────────────────────────┼──────────────┼────────────┤");
        for (EnrollmentDetailDTO e : list) {
            System.out.printf("│ %-5d │ %-20s │ %-30s │ %-12s │ %-10s │\n",
                    e.getId(), e.getStudentName(), e.getCourseName(), e.getRegisteredAt(), e.getStatus());
        }
        System.out.println("└───────┴──────────────────────┴────────────────────────────────┴──────────────┴────────────┘");
    }

    public void showMyHistory(int studentId) {
        System.out.println("⏱ LỊCH SỬ ĐĂNG KÝ ");

        List<EnrollmentDetailDTO> list = services.getHistory(studentId);

        if (list.isEmpty()) {
            System.out.println("⚠ Bạn chưa đăng ký khóa học nào!");
        } else {
            printList(list);
        }
        System.out.println("\nẤn Enter để quay lại...");
        sc.nextLine();
    }


    public void handleSortMenu(int studentId) {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("⇄ SẮP XẾP LỊCH SỬ ");
            System.out.println("1. Theo tên khóa học (A-Z)");
            System.out.println("2. Theo tên khóa học (Z-A)");
            System.out.println("3. Theo ngày đăng ký (Cũ nhất)");
            System.out.println("4. Theo ngày đăng ký (Mới nhất)");
            System.out.println("0. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa trọn : ");

            String choice = sc.nextLine();
            String sortBy = "";
            String sortDir = "";

            switch (choice) {
                case "1": sortBy = "name"; sortDir = "asc"; break;
                case "2": sortBy = "name"; sortDir = "desc"; break;
                case "3": sortBy = "time"; sortDir = "asc"; break;
                case "4": sortBy = "time"; sortDir = "desc"; break;
                case "0": return;
                default: System.out.println("⚠ Lựa trọn không hợp lệ!"); continue;
            }

            List<EnrollmentDetailDTO> sortedList = services.sortEnrollment(studentId, sortBy, sortDir);

            if (sortedList.isEmpty()) {
                System.out.println("⚠ Chưa có dữ liệu.");
            } else {
                printList(sortedList);
            }
            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }
}
