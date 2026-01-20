package presentation.admin;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.dto.EnrollmentDetailDTO;

import java.util.List;
import java.util.Scanner;

public class EnrollmentManagementMenu {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl services = new AdminSevicesImpl(dao);
    public void showMenu(){
        menuChinh: while(true){
            System.out.println("======Menu Quản lý đăng ký======");
            System.out.println("1. Hiển thị danh sách sinh viên đăng ký theo từng khóa học");
            System.out.println("2. Duyệt sinh viên đăng ký khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học (Chỉ xóa học viên ĐANG HỌC)");
            System.out.println("4. Trở về");
            System.out.println("================================");
            System.out.print("Nhập lựa trọn : ");
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
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    private void printList(List<EnrollmentDetailDTO> list) {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-20s | %-12s | %-10s |\n",
                "ID", "Tên Học Viên", "Tên Khóa", "Ngày ĐK", "Trạng Thái");
        System.out.println("-------------------------------------------------------------------------------");
        for (EnrollmentDetailDTO e : list) {
            System.out.printf("| %-5d | %-20s | %-20s | %-12s | %-10s |\n",
                    e.getId(), e.getStudentName(), e.getCourseName(), e.getRegisteredAt(), e.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    private int inputCourseId() {
        while (true) {
            System.out.print("👉 Nhập ID Khóa học (hoặc 0 để quay lại): ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) return 0;
            if (input.matches("\\d+")) {
                return Integer.parseInt(input);
            }
            System.out.println("❌ ID phải là số nguyên!");
        }
    }

    private void handleShowEnrollmentByCourse() {
        System.out.println("\n--- XEM DANH SÁCH ĐĂNG KÝ ---");
        int courseId = inputCourseId(); // Gọi hàm phụ nhập ID ở dưới
        if (courseId == 0) return;

        // Gọi Service lấy danh sách
        List<EnrollmentDetailDTO> list = services.getCourseEnrollments(courseId);

        if (list == null) {
            System.out.println("❌ Khóa học không tồn tại!");
        } else if (list.isEmpty()) {
            System.out.println("⚠️ Khóa học này chưa có ai đăng ký.");
        } else {
            // In bảng kết quả
            printList(list);
        }

        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    private void handleApproveDeny() {
        while (true) {
            System.out.println("\n--- DUYỆT PHIẾU ĐĂNG KÝ ---");
            int courseId = inputCourseId();
            if (courseId == 0) return;

            // 1. Lấy danh sách đang chờ (WAITING)
            List<EnrollmentDetailDTO> pendingList = services.getPendingEnrollments(courseId);

            if (pendingList == null) {
                System.out.println("❌ Khóa học không tồn tại!"); return;
            }
            if (pendingList.isEmpty()) {
                System.out.println("✅ Khóa học này không còn phiếu nào cần duyệt.");
                return;
            }

            // 2. Hiển thị danh sách chờ
            System.out.println("Danh sách đang chờ duyệt:");
            printList(pendingList);

            // 3. Nhập ID phiếu để xử lý
            System.out.print("👉 Nhập ID Phiếu muốn xử lý (hoặc 0 để thoát): ");
            String enrIdStr = sc.nextLine().trim();
            if (!enrIdStr.matches("\\d+")) {
                System.out.println("❌ ID phải là số!"); continue;
            }
            int enrollmentId = Integer.parseInt(enrIdStr);
            if (enrollmentId == 0) break;

            // 4. Chọn hành động
            System.out.println("1. Xác nhận (Confirm)");
            System.out.println("2. Từ chối (Deny)");
            System.out.print("👉 Chọn thao tác: ");
            String action = sc.nextLine().trim();

            boolean success = false;
            if (action.equals("1")) {
                success = services.approveEnrollment(enrollmentId);
                if (success) System.out.println("✅ Đã DUYỆT thành công!");
            } else if (action.equals("2")) {
                success = services.denyEnrollment(enrollmentId);
                if (success) System.out.println("🚫 Đã TỪ CHỐI phiếu này!");
            } else {
                System.out.println("❌ Chọn sai thao tác!");
            }

            if (!success && (action.equals("1") || action.equals("2"))) {
                System.out.println("❌ Thất bại (Có thể ID phiếu không đúng hoặc không thuộc khóa học này).");
            }

            // Lặp lại vòng while để admin có thể duyệt tiếp phiếu khác
            System.out.println("------------------------------------------------");
        }
    }

    private void handleDeleteEnrollment() {
        System.out.println("\n--- 🗑️ XÓA HỌC VIÊN KHỎI KHÓA HỌC ---");

        // 1. Nhập ID khóa học để xem danh sách trước
        int courseId = inputCourseId(); // Hàm này ông đã có ở bài trước
        if (courseId == 0) return;

        // 2. Lấy danh sách để admin nhìn ID mà xóa
        List<EnrollmentDetailDTO> list = services.getCourseEnrollments(courseId);

        if (list == null || list.isEmpty()) {
            System.out.println("⚠️ Khóa học này chưa có học viên nào.");
            return;
        }

        // In ra danh sách
        System.out.println("Danh sách hiện tại:");
        printList(list); // Hàm in bảng ông đã có

        while (true) {
            System.out.print("👉 Nhập ID Phiếu đăng ký (Enrollment ID) muốn xóa (hoặc 0 để thoát): ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("❌ ID phải là số!");
                continue;
            }

            int enrollmentId = Integer.parseInt(input);
            if (enrollmentId == 0) break;

            // Hỏi xác nhận cho chắc ăn
            System.out.print("⚠️ Bạn có chắc chắn muốn xóa (đuổi) học viên này khỏi lớp? (y/n): ");
            String confirm = sc.nextLine().trim();

            if (confirm.equalsIgnoreCase("y")) {
                // GỌI SERVICE
                boolean success = services.deleteEnrollment(enrollmentId);

                if (success) {
                    System.out.println("✅ Đã xóa thành công!");
                    break; // Xóa xong thì thoát ra ngoài
                } else {
                    System.out.println("❌ Xóa thất bại!");
                    System.out.println("👉 Lưu ý: Hệ thống chỉ cho phép xóa các phiếu có trạng thái 'CONFIRM'.");
                    System.out.println("   (Nếu phiếu đang Waiting, hãy dùng chức năng Từ chối/Deny).");
                }
            } else {
                System.out.println("🚫 Đã hủy thao tác.");
            }
        }
    }
}
