package presentation;

import business.IStudentServices;
import presentation.student.RegisteredMenu;
import presentation.student.ReviewCourseMenu;

import java.util.Scanner;

public class StudentMenuView {
    private Scanner sc ;
    private IStudentServices services;

    public StudentMenuView(Scanner sc, IStudentServices services) {
        this.sc = sc;
        this.services = services;
    }

    public boolean showStudentMenu(int id){
        while (true){
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ Menu Học Viên");
            System.out.println("1. Xem danh sách hóa học");
            System.out.println("2. Đăng kí khóa học");
            System.out.println("3. Xem khóa học đã đăng kí");
            System.out.println("4. Hủy đăng kí (nếu chưa bắt đầu)");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa trọn : ");
            String choice = sc.nextLine().trim();
            if (choice.isEmpty()) continue;
            switch (choice){
                case "1":
                    ReviewCourseMenu rcm = new ReviewCourseMenu(sc,services);
                    rcm.showMenu(id);
                    break;
                case "2":
                    handleRegisterCourse(id);
                    break;
                case "3":
                    RegisteredMenu rm = new RegisteredMenu(sc,services);
                    rm.showMenu(id);
                    break;
                case "4":
                    handleCancelEnrollment(id);
                    break;
                case "5":
                    handleChangePassword(id);
                    break;
                case "6":
                    return true;
                default:
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
            }
        }
    }

    public void handleRegisterCourse(int studentId) {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.print("Nhập ID khóa học muốn đăng ký (hoặc 0 để thoát): ");

            String input = sc.nextLine().trim();
            if (input.equals("0")) return ;
            if (!input.matches("\\d+")) {
                System.out.println("⚠ ID phải là số nguyên!");
                continue;
            }
            int courseId = Integer.parseInt(input);
            System.out.println("═══════════════════════════════════");
            boolean isSuccess = services.registerCourse(studentId, courseId);

            if (isSuccess) {
                System.out.println("✔ Đăng ký khóa học thành công!");
                break;
            } else {
                System.out.println("⚠ Đăng ký thất bại! (Khóa học không tồn tại hoặc đã đăng ký rồi)");
            }
        }
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleCancelEnrollment(int studentId) {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("⛌ HỦY ĐĂNG KÝ KHÓA HỌC ");
            System.out.print("Nhập ID phiếu đăng ký muốn hủy (hoặc 0 để quay lại): ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.err.println("❌ ID phải là số nguyên! Vui lòng nhập lại.");
                continue;
            }

            int enrollmentId = Integer.parseInt(input);
            if (enrollmentId == 0) return;
            System.out.println("═══════════════════════════════════");
            boolean isSuccess = services.cancelEnrollment(studentId, enrollmentId);

            if (isSuccess) {
                System.out.println("✔ Hủy đăng ký thành công!");
                break;
            } else {
                System.out.println("⚠ Hủy thất bại! (Phiếu không tồn tại, không phải của bạn, hoặc đã được duyệt/hủy trước đó).");
            }
        }
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleChangePassword(int studentId) {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("🗝 ĐỔI MẬT KHẨU ");

            //  Nhập Email
            System.out.print("Nhập xác nhận Email: ");
            String email = sc.nextLine().trim();

            //  Nhập Mật khẩu cũ
            System.out.print("Nhập Mật khẩu cũ: ");
            String oldPass = sc.nextLine().trim();

            //  Nhập Mật khẩu mới
            System.out.print("Nhập Mật khẩu mới: ");
            String newPass = sc.nextLine().trim();

            // 4Xác nhận Mật khẩu mới
            System.out.print("Nhập lại Mật khẩu mới: ");
            String confirmPass = sc.nextLine().trim();
            System.out.println("═══════════════════════════════════");
            // VALIDATE DATA
            if (email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
                System.out.println("⚠ Không được để trống thông tin!");
                continue;
            }
            if (!newPass.equals(confirmPass)) {
                System.out.println("⚠ Mật khẩu xác nhận không khớp!");
                continue;
            }
            if (newPass.equals(oldPass)) {
                System.out.println("⚠ Mật khẩu mới không được trùng với mật khẩu cũ!");
                continue;
            }

            boolean isSuccess = services.changePassword(studentId, email, oldPass, newPass);

            if (isSuccess) {
                System.out.println("✔ Đổi mật khẩu thành công!");
                break;
            } else {
                System.out.println("⚠ Đổi thất bại! (Email hoặc Mật khẩu cũ không đúng).");
                System.out.print("Bạn có muốn thử lại không? (y/n): ");
                String retry = sc.nextLine();
                if (retry.equalsIgnoreCase("n")) break;
            }
        }
    }
}
