package presentation;

import business.impl.StudentServicesImpl;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import presentation.student.RegisteredMenu;
import presentation.student.ReviewCourseMenu;

import java.util.Scanner;

public class StudentMenuView {
    Scanner sc = new Scanner(System.in);
    IStudentDAO dao = new StudentDAOImpl();
    ReviewCourseMenu rcm = new ReviewCourseMenu();
    RegisteredMenu rm = new RegisteredMenu();
    StudentServicesImpl services = new StudentServicesImpl(dao);
    public boolean showStudentMenu(int id){
        while (true){
            System.out.println("===========Menu Học Viên========");
            System.out.println("1. Xem danh sách hóa học");
            System.out.println("2. Đăng kí khóa học");
            System.out.println("3. Xem khóa học đã đăng kí");
            System.out.println("4. Hủy đăng kí (nếu chưa bắt đầu)");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("================================");
            System.out.println("Nhập lựa trọn : ");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    rcm.showMenu();
                    break;
                case 2:
                    handleRegisterCourse(id);
                    break;
                case 3:
                    rm.showMenu(id);
                    break;
                case 4:
                    handleCancelEnrollment(id);
                    break;
                case 5:
                    handleChangePassword(id);
                    break;
                case 6:
                    return true;
                default:
                    System.out.println("Lựa trọn Invalid");
            }
        }
    }

    public void handleRegisterCourse(int studentId) {

        while (true) {
            System.out.println("========================================");
            System.out.print("Nhập ID khóa học muốn đăng ký (hoặc 0 để thoát): ");

            int courseId = 0;
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) continue;
                courseId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("❌ Lỗi: Vui lòng nhập số nguyên!");
                continue;
            }

            if (courseId == 0) break;
            boolean isSuccess = services.registerCourse(studentId, courseId);

            if (isSuccess) {
                System.out.println("✅ Đăng ký khóa học thành công!");
                break;
            } else {
                System.err.println("❌ Đăng ký thất bại! (Khóa học không tồn tại hoặc đã đăng ký rồi)");
            }
        }
    }

    public void handleCancelEnrollment(int studentId) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== HỦY ĐĂNG KÝ KHÓA HỌC ==========");
            // Mẹo: Nên hiển thị lại lịch sử để user biết ID nào mà hủy
            // showMyHistory(studentId); (Nếu ông muốn)

            System.out.print("Nhập ID phiếu đăng ký muốn hủy (hoặc 0 để quay lại): ");
            String input = sc.nextLine().trim();

            // 1. Validate đầu vào (Phải là số)
            if (!input.matches("\\d+")) {
                System.err.println("❌ ID phải là số nguyên! Vui lòng nhập lại.");
                continue;
            }

            int enrollmentId = Integer.parseInt(input);

            if (enrollmentId == 0) break; // Thoát

            // 2. GỌI SERVICE
            boolean isSuccess = services.cancelEnrollment(studentId, enrollmentId);

            // 3. Xử lý kết quả
            if (isSuccess) {
                System.out.println("✅ Hủy đăng ký thành công!");
                break;
            } else {
                System.err.println("❌ Hủy thất bại! (Phiếu không tồn tại, không phải của bạn, hoặc đã được duyệt/hủy trước đó).");
                System.out.println("👉 Chỉ có thể hủy các phiếu đang ở trạng thái 'WAITING'.");
            }
        }
    }

    public void handleChangePassword(int studentId) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== ĐỔI MẬT KHẨU ==========");

            // 1. Nhập Email (Để xác thực theo logic DAO của ông)
            System.out.print("Nhập xác nhận Email: ");
            String email = sc.nextLine().trim();

            // 2. Nhập Mật khẩu cũ
            System.out.print("Nhập Mật khẩu cũ: ");
            String oldPass = sc.nextLine().trim();

            // 3. Nhập Mật khẩu mới
            System.out.print("Nhập Mật khẩu mới: ");
            String newPass = sc.nextLine().trim();

            // 4. Xác nhận Mật khẩu mới (Logic UI quan trọng)
            System.out.print("Nhập lại Mật khẩu mới: ");
            String confirmPass = sc.nextLine().trim();

            // --- VALIDATE DATA (Kiểm tra dữ liệu rác trước khi gọi Service) ---

            // Check rỗng
            if (email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
                System.err.println("❌ Không được để trống thông tin!");
                continue;
            }

            // Check mật khẩu mới xác nhận không khớp
            if (!newPass.equals(confirmPass)) {
                System.err.println("❌ Mật khẩu xác nhận không khớp!");
                continue;
            }

            // Check mật khẩu mới trùng mật khẩu cũ (Optional)
            if (newPass.equals(oldPass)) {
                System.err.println("❌ Mật khẩu mới không được trùng với mật khẩu cũ!");
                continue;
            }

            // --- GỌI SERVICE ---
            boolean isSuccess = services.changePassword(studentId, email, oldPass, newPass);

            // --- XỬ LÝ KẾT QUẢ ---
            if (isSuccess) {
                System.out.println("✅ Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
                // Thường đổi pass xong sẽ bắt logout, tùy ông xử lý
                break;
            } else {
                System.err.println("❌ Đổi thất bại! (Email hoặc Mật khẩu cũ không đúng).");

                // Hỏi user có muốn thử lại không
                System.out.print("Bạn có muốn thử lại không? (y/n): ");
                String retry = sc.nextLine();
                if (retry.equalsIgnoreCase("n")) break;
            }
        }
    }
}
