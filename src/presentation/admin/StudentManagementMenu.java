package presentation.admin;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static uttil.checkDOBValid.isValidDOB;
import static uttil.checkEmailValid.isValidEmail;
import static uttil.checkPhoneValid.checkPhone;

public class StudentManagementMenu {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl services = new AdminSevicesImpl(dao);

    public void showMenu(){
        menuChinh: while(true){
            try {
                System.out.println("================================");
                System.out.println("1. Hiển thị danh sách học viên");
                System.out.println("2. Thêm mới học viên");
                System.out.println("3. Chỉnh sửa thông tin học viên");
                System.out.println("4. Xóa học viên");
                System.out.println("5. Tìm kiếm theo id, tên hoặc email học viên");
                System.out.println("6. Sắp xếp theo tên hoặc email học viên");
                System.out.println("7. Quay về menu chính");
                System.out.println("================================");
                System.out.print("Nhập lựa chọn : ");
                switch(sc.nextLine()){
                    case "1":
                        handleShowListStudent();
                        break;
                    case "2":
                        handleAddStudent();
                        break;
                    case "3":
                        handleEditStudent();
                        break;
                    case "4":
                        handleDeleteStudent();
                        break;
                    case "5":
                        handleFindStudent();
                        break;
                    case "6":
                        handleSortStudent();
                        break;
                    case "7":
                        return;
                    default:
                        System.out.println("Lựa trọn Invalid");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void printStudentList(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("⚠️ Danh sách trống.");
            return;
        }

        // Kẻ bảng đẹp trai
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-12s | %-25s | %-10s | %-6s |\n",
                "ID", "Tên Học Viên", "Ngày Sinh", "Email", "SĐT", "Giới");
        System.out.println("----------------------------------------------------------------------------------------------------");

        for (Student s : list) {// Giả sử isGender() trả về boolean
            // Hoặc nếu s.getGender() trả về bit/int thì ông tự map nhé

            System.out.printf("| %-5d | %-20s | %-12s | %-25s | %-10s | %-6s |\n",
                    s.getId(),
                    s.getName(),
                    s.getDateOfBirth(), // Nhớ format date nếu cần (SimpleDateFormat)
                    s.getEmail(),
                    s.getPhoneNumber(),
                    s.getGender());
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public void handleShowListStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n========== DANH SÁCH HỌC VIÊN ==========");

        // Gọi Service lấy danh sách
        List<Student> list = services.showListStudent();

        // In ra (Dùng hàm phụ tôi viết ở dưới)
        printStudentList(list);

        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleAddStudent() {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while (true) {
            System.out.println("\n========== THÊM HỌC VIÊN MỚI ==========");
            try {
                System.out.print("1. Nhập tên: ");
                String name = sc.nextLine().trim();

                System.out.print("2. Nhập ngày sinh (yyyy-MM-dd): ");
                String dobStr = sc.nextLine().trim();
                if (!isValidDOB(dobStr)) { // Validate format trước
                    System.out.println("❌ Ngày sinh sai định dạng!"); continue;
                }
                Date dob = sdf.parse(dobStr); // Parse sang Date để gửi cho Service

                System.out.print("3. Nhập Email: ");
                String email = sc.nextLine().trim();
                if (!isValidEmail(email)) {
                    System.out.println("❌ Email sai định dạng!"); continue;
                }

                System.out.print("4. Giới tính (1: Nam, 2: Nữ): ");
                String genderChoice = sc.nextLine().trim();
                boolean gender = genderChoice.equals("1") || genderChoice.equalsIgnoreCase("Nam");

                System.out.print("5. Số điện thoại: ");
                String phone = sc.nextLine().trim();
                if (!checkPhone(phone)) {
                    System.out.println("❌ SĐT sai định dạng!"); continue;
                }

                System.out.print("6. Mật khẩu: ");
                String pass = sc.nextLine().trim();

                // GỌI SERVICE
                boolean success = services.addStudent(name, dob, email, gender, phone, pass);

                if (success) {
                    System.out.println("✅ Thêm học viên thành công!");
                    break; // Thoát ra menu cha
                } else {
                    System.out.println("❌ Thêm thất bại! (Email có thể đã tồn tại).");
                }

            } catch (Exception e) {
                System.out.println("❌ Lỗi nhập liệu: " + e.getMessage());
            }
        }
    }

    public void handleEditStudent() {
        Scanner sc = new Scanner(System.in);

        // 1. Nhập ID (Giữ nguyên)
        System.out.println("\n========== CHỈNH SỬA THÔNG TIN HỌC VIÊN ==========");
        System.out.print("👉 Nhập ID học viên cần sửa (hoặc 0 để thoát): ");
        String idStr = sc.nextLine().trim();
        if (!idStr.matches("\\d+")) {
            System.out.println("❌ ID phải là số nguyên!"); return;
        }
        int id = Integer.parseInt(idStr);
        if (id == 0) return;

        // 2. Vòng lặp sửa
        while (true) {
            System.out.println("\n--- CHỌN MỤC CẦN SỬA ---");
            System.out.println("1. Tên | 2. Ngày sinh | 3. Email | 4. Giới tính | 5. SĐT | 6. Mật khẩu | 0. Quay lại");
            System.out.print("👉 Chọn số: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("0")) break;

            String fieldName = "";
            String label = "";

            // --- BƯỚC 1: CẤU HÌNH (Mapping) ---
            switch (choice) {
                case "1": fieldName = "name";     label = "Tên"; break;
                case "2": fieldName = "dob";      label = "Ngày sinh (yyyy-MM-dd)"; break;
                case "3": fieldName = "email";    label = "Email"; break;
                case "4": fieldName = "gender";   label = "Giới tính (1: Nam, 0: Nữ)"; break;
                case "5": fieldName = "phone";    label = "Số điện thoại"; break;
                case "6": fieldName = "password"; label = "Mật khẩu"; break;
                default: System.out.println("❌ Chọn sai!"); continue;
            }

            // --- BƯỚC 2: NHẬP LIỆU (Input 1 lần duy nhất) ---
            System.out.printf("👉 Nhập %s mới: ", label);
            String newValue = sc.nextLine().trim();

            // --- BƯỚC 3: VALIDATE & CHUYỂN ĐỔI DỮ LIỆU ---
            if (newValue.isEmpty()) {
                System.out.println("❌ Không được để trống!"); continue;
            }

            if (fieldName.equals("dob") && !isValidDOB(newValue)) { //
                System.out.println("❌ Ngày sinh sai định dạng (yyyy-MM-dd)!"); continue;
            }

            if (fieldName.equals("email") && !isValidEmail(newValue)) { //
                System.out.println("❌ Email không hợp lệ!"); continue;
            }

            if (fieldName.equals("phone") && !checkPhone(newValue)) { //
                System.out.println("❌ SĐT không hợp lệ (Phải là số VN)!"); continue;
            }

            if (fieldName.equals("password") && newValue.length() < 6) {
                System.out.println("⚠️ Mật khẩu nên dài hơn 6 ký tự!");
                // Chỉ cảnh báo, vẫn cho sửa
            }

            // Xử lý riêng cho Gender: Chuyển chữ "Nam/Nu" thành "1/0" để DB hiểu
            if (fieldName.equals("gender")) {
                if (newValue.equalsIgnoreCase("Nam") || newValue.equals("1")) newValue = "1";
                else if (newValue.equalsIgnoreCase("Nu") || newValue.equals("0") || newValue.equals("2")) newValue = "0";
                else {
                    System.out.println("❌ Giới tính không hợp lệ (Nhập 1 hoặc 0)!"); continue;
                }
            }

            // --- BƯỚC 4: GỌI SERVICE ---
            boolean success = services.editStudent(id, fieldName, newValue);
            if (success) {
                System.out.println("✅ Cập nhật thành công!");
            } else {
                System.out.println("❌ Thất bại (Lỗi hệ thống hoặc trùng Email)!");
            }
        }
    }

    public void handleSortStudent() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== SẮP XẾP HỌC VIÊN ==========");
            System.out.println("1. Theo Tên (A -> Z)");
            System.out.println("2. Theo Tên (Z -> A)");
            System.out.println("3. Theo Email (A -> Z)");
            System.out.println("4. Theo Email (Z -> A)");
            System.out.println("0. Quay lại");
            System.out.print("👉 Mời chọn: ");

            String choice = sc.nextLine().trim();
            if (choice.equals("0")) break;

            String sortBy = "";
            String sortOrder = "";

            switch (choice) {
                case "1":
                    sortBy = "name"; sortOrder = "td"; // Tên tăng
                    break;
                case "2":
                    sortBy = "name"; sortOrder = "gd"; // Tên giảm
                    break;
                case "3":
                    sortBy = "email"; sortOrder = "td"; // Email tăng
                    break;
                case "4":
                    sortBy = "email"; sortOrder = "gd"; // Email giảm
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
                    continue;
            }

            // --- GỌI SERVICE ---
            List<Student> result = services.sortListStudent(sortBy, sortOrder);

            // --- IN KẾT QUẢ ---
            if (result.isEmpty()) {
                System.out.println("⚠️ Danh sách trống!");
            } else {
                System.out.println("---------------------------------------------------------------");
                System.out.printf("| %-5s | %-20s | %-25s | %-10s |\n", "ID", "Tên", "Email", "SĐT");
                System.out.println("---------------------------------------------------------------");
                for (Student s : result) {
                    System.out.printf("| %-5d | %-20s | %-25s | %-10s |\n",
                            s.getId(), s.getName(), s.getEmail(), s.getPhoneNumber());
                }
                System.out.println("---------------------------------------------------------------");
            }

            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }

    public void handleDeleteStudent() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== XÓA HỌC VIÊN ==========");
            System.out.print("👉 Nhập ID học viên cần xóa (hoặc 0 để quay lại): ");
            String input = sc.nextLine().trim();

            // Validate nhập số
            if (!input.matches("\\d+")) {
                System.out.println("❌ ID phải là số nguyên!");
                continue;
            }

            int studentId = Integer.parseInt(input);
            if (studentId == 0) break;

            // Cảnh báo quan trọng
            System.out.print("⚠️ Bạn có chắc chắn muốn xóa học viên ID " + studentId + "? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println("🚫 Đã hủy thao tác xóa.");
                continue;
            }

            // GỌI SERVICE
            boolean isDeleted = services.deleteStudent(studentId);

            // XỬ LÝ KẾT QUẢ
            if (isDeleted) {
                System.out.println("✅ Xóa học viên thành công!");
                break; // Xóa xong thoát luôn
            } else {
                System.err.println("❌ Xóa thất bại!");
                System.out.println("👉 Nguyên nhân: ID không tồn tại HOẶC Học viên đang đi học (Có trong bảng Enrollment).");
                System.out.println("👉 (Gợi ý: Cần xóa hết lịch sử đăng ký của học viên này trước).");
            }
        }
    }

    public void handleFindStudent() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== TÌM KIẾM HỌC VIÊN ==========");
            System.out.println("1. Tìm theo Tên");
            System.out.println("2. Tìm theo Email");
            System.out.println("0. Quay lại");
            System.out.print("👉 Chọn tiêu chí: ");

            String choice = sc.nextLine().trim();
            if (choice.equals("0")) break;

            String searchBy = "";
            switch (choice) {
                case "1": searchBy = "name"; break;
                case "2": searchBy = "email"; break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
                    continue;
            }

            System.out.print("👉 Nhập từ khóa tìm kiếm: ");
            String key = sc.nextLine().trim();
            if (key.isEmpty()) {
                System.out.println("❌ Từ khóa không được để trống!");
                continue;
            }

            // GỌI SERVICE
            List<Student> result = services.findStudent(key, searchBy);

            // HIỂN THỊ KẾT QUẢ
            if (result.isEmpty()) {
                System.out.println("⚠️ Không tìm thấy học viên nào phù hợp.");
            } else {
                System.out.println("✅ Tìm thấy " + result.size() + " kết quả:");
                printStudentList(result);
            }

            System.out.println("Ấn Enter để tiếp tục tìm kiếm (hoặc gõ 0 để thoát)...");
            if (sc.nextLine().equals("0")) break;
        }
    }
}
