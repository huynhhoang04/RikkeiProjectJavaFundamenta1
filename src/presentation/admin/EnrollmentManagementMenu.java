package presentation.admin;

import business.IAdminSevices;
import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.dto.EnrollmentDetailDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnrollmentManagementMenu {
    private Scanner sc ;
    private IAdminSevices services;

    public EnrollmentManagementMenu(Scanner sc, IAdminSevices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu(){
        while(true){
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ Menu Quản lý đăng ký");
            System.out.println("1. Hiển thị danh sách sinh viên đăng ký theo từng khóa học");
            System.out.println("2. Duyệt sinh viên đăng ký khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học (Chỉ xóa học viên ĐANG HỌC)");
            System.out.println("4. Trở về");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa chọn : ");
            switch(sc.nextLine()){
                case "1":
                    handleShowEnrollmentByCourse();
                    break;
                case "2":
                    handleApproveDeny();
                    break;
                case "3":
                    handleDeleteEnrollment();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

    private void printList(List<EnrollmentDetailDTO> list) {
        System.out.println("┌───────┬──────────────────────┬──────────────────────┬──────────────┬────────────┐");
        System.out.printf("│ %-5s │ %-20s │ %-20s │ %-12s │ %-10s │\n",
                "ID", "Tên Học Viên", "Tên Khóa", "Ngày ĐK", "Trạng Thái");
        System.out.println("├───────┼──────────────────────┼──────────────────────┼──────────────┼────────────┤");
        for (EnrollmentDetailDTO e : list) {
            System.out.printf("│ %-5d │ %-20s │ %-20s │ %-12s │ %-10s │\n",
                    e.getId(), e.getStudentName(), e.getCourseName(), e.getRegisteredAt(), e.getStatus());
        }
        System.out.println("└───────┴──────────────────────┴──────────────────────┴──────────────┴────────────┘");
    }

    private int inputCourseId() {
        while (true) {
            System.out.print("➜ Nhập ID khóa học(0 để trở về) : ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) return 0;
            if (!input.matches("\\d+")) {
                System.out.println("⚠ ID phải là số nguyên!");
                continue;
            }
            if (services.checkCourse(Integer.parseInt(input))) {
                return Integer.parseInt(input);
            }
            else {
                System.out.println("⚠ ID không tồn tại!");
                continue;
            }
        }
    }

    private void handleShowEnrollmentByCourse() {
        int courseId = inputCourseId();
        if (courseId == 0) return;
        System.out.println("══════════════════════════════════════════════════════════════════════════════════");
        System.out.println("𝄜 XEM DANH SÁCH ĐĂNG KÝ ");
        List<EnrollmentDetailDTO> list = services.getCourseEnrollments(courseId);

        if (list == null) {
            System.out.println("⚠ Khóa học không tồn tại!");
        } else if (list.isEmpty()) {
            System.out.println("⚠ Khóa học này chưa có ai đăng ký.");
        } else {
            printList(list);
        }
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    private void handleApproveDeny() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("👍 DUYỆT PHIẾU ĐĂNG KÝ 👎");
            int courseId = inputCourseId();
            if (courseId == 0) return;
            List<EnrollmentDetailDTO> pendingList = services.getPendingEnrollments(courseId);
            List<Integer> validID = new ArrayList<>();
            pendingList.forEach((e) -> {validID.add(e.getId());});
            if (pendingList.isEmpty()) {
                System.out.println("✔ Khóa học này không còn phiếu nào cần duyệt.");
                return;
            }
            System.out.println("═══════════════════════════════════");
            System.out.println("𝄜 Danh sách đang chờ duyệt:");
            printList(pendingList);


            while (true) {
                System.out.print("➜ Nhập ID Phiếu muốn xử lý (hoặc 0 để thoát): ");
                String enrIdStr = sc.nextLine().trim();
                if (!enrIdStr.matches("\\d+")) {
                    System.out.println("❌ ID phải là số!"); continue;
                }
                int enrollmentId = Integer.parseInt(enrIdStr);
                if (enrollmentId == 0) break;
                if (!validID.contains(enrollmentId)) {
                    System.out.println("⚠ Có thể ID không thuộc khóa học này.");
                    continue;
                }

                System.out.println("═══════════════════════════════════");
                System.out.println("1. Xác nhận (Confirm)");
                System.out.println("2. Từ chối (Deny)");
                System.out.println("═══════════════════════════════════");
                System.out.print("➜ Chọn thao tác: ");
                String action = sc.nextLine().trim();

                boolean success = false;
                if (action.equals("1")) {
                    success = services.approveEnrollment(enrollmentId);
                    if (success) System.out.println("✔ Đã DUYỆT thành công!");
                } else if (action.equals("2")) {
                    success = services.denyEnrollment(enrollmentId);
                    if (success) System.out.println(" ⃠  Đã TỪ CHỐI phiếu này!");
                } else {
                    System.out.println("⚠ Chọn sai thao tác!");
                }
            }
        }
    }

    private void handleDeleteEnrollment() {
        System.out.println("═══════════════════════════════════");
        System.out.println("🗑 XÓA HỌC VIÊN KHỎI KHÓA HỌC ");
        int courseId = inputCourseId();
        if (courseId == 0) return;

        List<EnrollmentDetailDTO> list = services.getCourseEnrollments(courseId);
        List<Integer> validID = new ArrayList<>();
        list.forEach((e) -> {validID.add(e.getId());});
        if (list == null || list.isEmpty()) {
            System.out.println("⚠ Khóa học này chưa có học viên nào.");
            return;
        }

        System.out.println("𝄜 Danh sách hiện tại:");
        printList(list);

        while (true) {
            System.out.print("➜ Nhập ID Phiếu đăng ký (Enrollment ID) muốn xóa (hoặc 0 để thoát): ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("❌ ID phải là số!");
                continue;
            }

            int enrollmentId = Integer.parseInt(input);
            if (enrollmentId == 0) break;
            if (!validID.contains(enrollmentId)) {
                System.out.println("⚠ Có thể ID không thuộc khóa học này.");
                continue;
            }
            System.out.print("⚠ Bạn có chắc chắn muốn xóa học viên này khỏi lớp? (y/n): ");
            String confirm = sc.nextLine().trim();

            if (confirm.equalsIgnoreCase("y")) {
                boolean success = services.deleteEnrollment(enrollmentId);

                if (success) {
                    System.out.println("✔ Đã xóa thành công!");
                } else {
                    System.out.println("⚠ Xóa thất bại!");
                }
            } else {
                System.out.println(" ⃠  Đã hủy thao tác.");
            }
        }
    }
}
