package presentation.admin;

import business.IAdminSevices;
import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static util.checkDOBValid.isValidDOB;
import static util.checkEmailValid.isValidEmail;
import static util.checkPhoneValid.checkPhone;

public class StudentManagementMenu {
    private Scanner sc ;
    private IAdminSevices services;

    public StudentManagementMenu(Scanner sc, IAdminSevices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu(){
        while(true){
            try {
                System.out.println("═══════════════════════════════════");
                System.out.println("☰ Menu Quản lý học viên");
                System.out.println("1. Hiển thị danh sách học viên");
                System.out.println("2. Thêm mới học viên");
                System.out.println("3. Chỉnh sửa thông tin học viên");
                System.out.println("4. Xóa học viên");
                System.out.println("5. Tìm kiếm theo id, tên hoặc email học viên");
                System.out.println("6. Sắp xếp theo tên hoặc email học viên");
                System.out.println("7. Quay về menu chính");
                System.out.println("═══════════════════════════════════");
                System.out.print("➜ Nhập lựa chọn : ");
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
                        System.out.println("⚠ Lựa trọn không hợp lệ!");
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
        System.out.println("┌───────┬──────────────────────┬──────────────┬───────────────────────────┬────────────┬────────────┐");
        System.out.printf("│ %-5s │ %-20s │ %-12s │ %-25s │ %-10s │ %-10s │\n",
                "ID", "Tên Học Viên", "Ngày Sinh", "Email", "SĐT", "Giới tính");
        System.out.println("├───────┼──────────────────────┼──────────────┼───────────────────────────┼────────────┼────────────┤");

        for (Student s : list) {

            System.out.printf("│ %-5d │ %-20s │ %-12s │ %-25s │ %-10s │ %-10s │\n",
                    s.getId(), s.getName(), s.getDateOfBirth(), s.getEmail(), s.getPhoneNumber(), s.getGender());
        }
        System.out.println("└───────┴──────────────────────┴──────────────┴───────────────────────────┴────────────┴────────────┘");
    }

    private int inputStudentId() {
        while (true) {
            System.out.print("➜ Nhập ID học viên : ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) return 0;
            if (!input.matches("\\d+")) {
                System.out.println("⚠ ID phải là số nguyên!");
                continue;
            }
            if (services.checkStudent(Integer.parseInt(input))) {
                return Integer.parseInt(input);
            }
            else {
                System.out.println("⚠ ID không tồn tại!");
                continue;
            }
        }
    }

    public void handleShowListStudent() {
        System.out.println("═══════════════════════════════════");
        System.out.println("𝄜 DANH SÁCH HỌC VIÊN ");
        List<Student> list = services.showListStudent();
        printStudentList(list);
        System.out.println("═══════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleAddStudent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("✚ THÊM HỌC VIÊN MỚI ");
            System.out.println("(Gõ 'exit' để hủy và quay lại menu chính)");
            try {
                System.out.print("1. Nhập tên: ");
                String name = sc.nextLine().trim();
                if (name.equalsIgnoreCase("exit")) break;
                if (name.isEmpty()) {
                    System.out.println("⚠ Tên không được để trống!");
                    continue;
                }

                System.out.print("2. Nhập ngày sinh (yyyy-MM-dd): ");
                String dobStr = sc.nextLine().trim();
                if (dobStr.equalsIgnoreCase("exit")) break;
                if (dobStr.isEmpty()) {
                    System.out.println("⚠ Ngày sinh không được để trống!");
                    continue;
                }
                if (!isValidDOB(dobStr)) {
                    System.out.println("⚠ Ngày sinh sai định dạng!");
                    continue;}
                Date dob = sdf.parse(dobStr);

                System.out.print("3. Nhập Email: ");
                String email = sc.nextLine().trim();
                if (email.equalsIgnoreCase("exit")) break;
                if (email.isEmpty()) {
                    System.out.println("⚠ Email không được để trống!");
                    continue;
                }
                if (!isValidEmail(email)) {
                    System.out.println("⚠ Email sai định dạng!");
                    continue;
                }

                System.out.print("4. Giới tính (1: Nam, 2: Nữ): ");
                String genderChoice = sc.nextLine().trim();
                if (genderChoice.equalsIgnoreCase("exit")) break;
                boolean gender = genderChoice.equals("1") || genderChoice.equalsIgnoreCase("Nam");

                System.out.print("5. Số điện thoại: ");
                String phone = sc.nextLine().trim();
                if (phone.equalsIgnoreCase("exit")) break;
                if (phone.isEmpty()) {
                    System.out.println("⚠ SĐT không được để trống!");
                    continue;
                }
                if (!checkPhone(phone)) {
                    System.out.println("⚠ SĐT sai định dạng!");
                    continue;
                }

                System.out.print("6. Mật khẩu: ");
                String pass = sc.nextLine().trim();
                if (pass.equalsIgnoreCase("exit")) break;
                if (pass.isEmpty()) {
                    System.out.println("⚠ MK không được để trống!");
                    continue;
                }

                System.out.println("═══════════════════════════════════");

                boolean success = services.addStudent(name, dob, email, gender, phone, pass);

                if (success) {
                    System.out.println("✔ Thêm học viên thành công!");
                    break;
                } else {
                    System.out.println("⚠ Thêm thất bại! (Email có thể đã tồn tại).");
                }

            } catch (Exception e) {
                System.out.println("⚠ Lỗi nhập liệu: " + e.getMessage());
            }
        }
    }

    public void handleEditStudent() {
        int id = inputStudentId();
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ CHỌN MỤC CẦN SỬA");
            System.out.println("1. Tên | 2. Ngày sinh | 3. Email | 4. Giới tính | 5. SĐT | 6. Mật khẩu | 7. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa chọn : ");
            String choice = sc.nextLine().trim();

            if (choice.equals("7")) break;

            String fieldName = "";
            String label = "";

            switch (choice) {
                case "1": fieldName = "name";     label = "Tên"; break;
                case "2": fieldName = "dob";      label = "Ngày sinh (yyyy-MM-dd)"; break;
                case "3": fieldName = "email";    label = "Email"; break;
                case "4": fieldName = "gender";   label = "Giới tính (1: Nam, 0: Nữ)"; break;
                case "5": fieldName = "phone";    label = "Số điện thoại"; break;
                case "6": fieldName = "password"; label = "Mật khẩu"; break;
                default: System.out.println("⚠ Lựa trọn không hợp lệ!"); continue;
            }

            System.out.printf("➜ Nhập %s mới: ", label);
            String newValue = sc.nextLine().trim();

            if (newValue.isEmpty()) {
                System.out.println("⚠ Không được để trống!"); continue;
            }

            if (fieldName.equals("dob") && !isValidDOB(newValue)) {
                System.out.println("⚠ Ngày sinh sai định dạng (yyyy-MM-dd)!"); continue;
            }

            if (fieldName.equals("email") && !isValidEmail(newValue)) {
                System.out.println("⚠ Email không hợp lệ!"); continue;
            }

            if (fieldName.equals("phone") && !checkPhone(newValue)) {
                System.out.println("⚠ SĐT không hợp lệ!"); continue;
            }

            if (fieldName.equals("gender")) {
                if (newValue.equalsIgnoreCase("Nam") || newValue.equals("1")) newValue = "1";
                else if (newValue.equalsIgnoreCase("Nu") || newValue.equals("0") || newValue.equals("2")) newValue = "0";
                else {
                    System.out.println("⚠ Giới tính không hợp lệ!"); continue;
                }
            }

            boolean success = services.editStudent(id, fieldName, newValue);
            if (success) {
                System.out.println("✔ Cập nhật thành công!");
            } else {
                System.out.println("⚠ Thất bại (Lỗi hệ thống hoặc trùng Email)!");
            }
        }
    }

    public void handleSortStudent() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ SẮP XẾP HỌC VIÊN ");
            System.out.println("1. Theo Tên (A -> Z)");
            System.out.println("2. Theo Tên (Z -> A)");
            System.out.println("3. Theo Email (A -> Z)");
            System.out.println("4. Theo Email (Z -> A)");
            System.out.println("5. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Mời chọn: ");

            String choice = sc.nextLine().trim();
            if (choice.equals("5")) break;

            String sortBy = "";
            String sortOrder = "";

            switch (choice) {
                case "1":
                    sortBy = "name"; sortOrder = "asc";
                    break;
                case "2":
                    sortBy = "name"; sortOrder = "desc";
                    break;
                case "3":
                    sortBy = "email"; sortOrder = "asc";
                    break;
                case "4":
                    sortBy = "email"; sortOrder = "desc";
                    break;
                default:
                    System.out.println("⚠ Lựa chọn không hợp lệ!");
                    continue;
            }
            List<Student> result = services.sortListStudent(sortBy, sortOrder);
            if (result.isEmpty()) {
                System.out.println("⚠ Danh sách trống!");
            } else {
                printStudentList(result);
            }
            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }

    public void handleDeleteStudent() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("🗑 XÓA HỌC VIÊN");
            int studentID = inputStudentId();
            System.out.print("⚠️ Bạn có chắc chắn muốn xóa học viên ID " + studentID + "? (y/n): ");
            String confirm = sc.nextLine();
            System.out.println("═══════════════════════════════════");
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println(" ⃠  Đã hủy thao tác xóa.");
                continue;
            }

            boolean isDeleted = services.deleteStudent(studentID);
            if (isDeleted) {
                System.out.println("✔ Xóa học viên thành công!");
                break;
            } else {
                System.err.println("⚠ Xóa thất bại!");
                System.out.println("👉 Nguyên nhân: Học viên đang đang tham gia khóa học!");
            }
        }
    }

    public void handleFindStudent() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("⌕ TÌM KIẾM HỌC VIÊN ");
            System.out.println("1. Tìm theo Tên");
            System.out.println("2. Tìm theo Email");
            System.out.println("3. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Chọn tiêu chí: ");

            String choice = sc.nextLine().trim();
            if (choice.equals("3")) break;

            String searchBy = "";
            switch (choice) {
                case "1": searchBy = "name"; break;
                case "2": searchBy = "email"; break;
                default:
                    System.out.println("⚠  Lựa chọn không hợp lệ!");
                    continue;
            }

            System.out.print("➜ Nhập từ khóa tìm kiếm: ");
            String key = sc.nextLine().trim();
            if (key.isEmpty()) {
                System.out.println("⚠  Từ khóa không được để trống!");
                continue;
            }
            List<Student> result = services.findStudent(key, searchBy);
            if (result.isEmpty()) {
                System.out.println("⚠  Không tìm thấy học viên nào phù hợp.");
            } else {
                System.out.println("✔ Tìm thấy " + result.size() + " kết quả:");
                printStudentList(result);
            }
        }
    }
}
