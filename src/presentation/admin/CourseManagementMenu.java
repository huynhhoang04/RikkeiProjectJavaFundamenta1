package presentation.admin;

import business.IAdminSevices;
import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Course;

import java.util.List;
import java.util.Scanner;

public class CourseManagementMenu {
    private Scanner sc ;
    private IAdminSevices services;

    public CourseManagementMenu(Scanner sc, IAdminSevices services) {
        this.sc = sc;
        this.services = services;
    }

    public void showMenu()
    {
        while (true){
            try {
                System.out.println("═══════════════════════════════════");
                System.out.println("☰ Menu Quản lý khóa học");
                System.out.println("1. Hiển thị danh sách khóa học");
                System.out.println("2. Thêm mới khóa học");
                System.out.println("3. Chỉnh sửa thông tin khóa học ");
                System.out.println("4. Xóa khóa học ");
                System.out.println("5. Tìm kiếm theo tên");
                System.out.println("6. Sắp xếp theo tên hoặc id");
                System.out.println("7. Quay về menu chính");
                System.out.println("═══════════════════════════════════");
                System.out.print("➜ Nhập lựa chọn : ");

                switch(sc.nextLine()){
                    case "1":
                        handleShowListCourses();
                        break;
                    case "2":
                        handleAddCourse();
                        break;
                    case "3":
                        handleUpdateCourse();
                        break;
                    case "4":
                        handleDeleteCourse();
                        break;
                    case "5":
                        handleFindCourse();
                        break;
                    case "6":
                        handleSortCourse();
                        break;
                    case "7":
                        return;
                    default:
                        System.out.println("⚠ Lựa trọn không hợp lệ!");
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void printCourseList(List<Course> list) {
        System.out.println("┌───────┬────────────────────────────────┬─────────────────┬─────────────────┬────────────┐");
        System.out.printf("│ %-5s │ %-30s │ %-15s │ %-15s │ %-10s │\n", "ID", "Tên Khóa Học", "Thời lượng(giờ)", "Giảng viên", "Ngày tạo");
        System.out.println("├───────┼────────────────────────────────┼─────────────────┼─────────────────┼────────────┤");
        for (Course c : list) {
            System.out.printf("│ %-5d │ %-30s │ %-15d │ %-15s │ %-10s │\n",
                    c.getId(), c.getName(), c.getDuration(), c.getInstructor() , c.getCreated_at());
        }
        System.out.println("└───────┴────────────────────────────────┴─────────────────┴─────────────────┴────────────┘");
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

    public void handleUpdateCourse() {
        int id = inputCourseId();
        if (id == 0) return;
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ Sửa thông tin khóa học");
            System.out.println("1. Sửa Tên");
            System.out.println("2. Sửa Thời lượng");
            System.out.println("3. Sửa Giảng viên");
            System.out.println("4. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa chọn : ");

            String choice = sc.nextLine();
            boolean isSuccess = false;

            switch (choice) {
                case "1":
                    System.out.print("➜ Nhập tên mới: ");
                    String name = sc.nextLine();
                    isSuccess = services.updateCourseName(id, name);
                    break;
                case "2":
                    System.out.print("➜ Nhập thời lượng mới (số giờ): ");
                    try {
                        int duration = Integer.parseInt(sc.nextLine());
                        isSuccess = services.updateCourseDuration(id, duration);
                    } catch (Exception e) {
                        System.out.println("⚠ Thời lượng phải là số nguyên!");
                        continue;
                    }
                    break;
                case "3":
                    System.out.print("➜ Nhập tên giảng viên mới: ");
                    String instructor = sc.nextLine();
                    isSuccess = services.updateCourseInstructor(id, instructor);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("⚠ Lựa trọn không hợp lệ!");
                    continue;
            }

            if (isSuccess) {
                System.out.println("✔ Cập nhật thành công!");
            } else {
                System.out.println("⚠ Cập nhật thất bại lỗi hệ thống.");
            }
        }
    }

    public void handleShowListCourses() {
        System.out.println("═══════════════════════════════════");
        System.out.println("𝄜 DANH SÁCH KHÓA HỌC");
        List<Course> list = services.showListCourse();
        if (list.isEmpty()) {
            System.out.println("⚠ Hệ thống chưa có khóa học nào!");
        } else {
            printCourseList(list);
        }
        System.out.println("═══════════════════════════════════");
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleSortCourse() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("☰ SẮP XẾP KHÓA HỌC ");
            System.out.println("1. Theo Tên (A -> Z)");
            System.out.println("2. Theo Tên (Z -> A)");
            System.out.println("3. Theo Thời lượng (Thấp -> Cao)");
            System.out.println("4. Theo Thời lượng (Cao -> Thấp)");
            System.out.println("5. Quay lại");
            System.out.println("═══════════════════════════════════");
            System.out.print("➜ Nhập lựa chọn : ");

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
                    sortBy = "duration"; sortOrder = "asc";
                    break;
                case "4":
                    sortBy = "duration"; sortOrder = "desc";
                    break;
                default:
                    System.out.println("⚠ Lựa chọn không hợp lệ!");
                    continue;
            }
            List<Course> result = services.sortListCourse(sortBy, sortOrder);
            if (result.isEmpty()) {
                System.out.println("⚠ Danh sách trống.");
            } else {
                printCourseList(result);
            }

            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }

    public void handleFindCourse() {
        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("⌕ TÌM KIẾM KHÓA HỌC ");
            System.out.print("➜ Nhập từ khóa tên môn học (hoặc exit để quay lại): ");
            String key = sc.nextLine().trim();
            System.out.println("═══════════════════════════════════");

            if (key.equalsIgnoreCase("exit")) return;

            List<Course> result = services.findCourseByKey(key);

            if (result.isEmpty()) {
                System.out.println("⚠ Không tìm thấy khóa học nào chứa: \"" + key + "\"");
            } else {
                System.out.println("✔ Tìm thấy " + result.size() + " kết quả:");
                printCourseList(result);
            }
        }
    }

    public void handleDeleteCourse() {

        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("🗑 XÓA KHÓA HỌC ");
            int input = inputCourseId();
            if (input == 0) return;
            System.out.print("➜ Bạn có chắc chắn muốn xóa khóa học ID " + input + "? (y/n): ");
            String confirm = sc.nextLine();
            System.out.println("═══════════════════════════════════");
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println(" ⃠  Đã hủy thao tác xóa.");
                continue;
            }
            boolean isDeleted = services.deleteCourse(input);
            if (isDeleted) {
                System.out.println("✔ Xóa khóa học thành công!");
                break;
            } else {
                System.out.println("⚠ Xóa thất bại lỗi hệ thống!");
            }
        }
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleAddCourse() {

        while (true) {
            System.out.println("═══════════════════════════════════");
            System.out.println("✚ THÊM KHÓA HỌC MỚI ");
            System.out.println("(Gõ 'exit' để hủy và quay lại menu chính)");
            try {
                System.out.print("➜ Nhập tên khóa học: ");
                String name = sc.nextLine().trim();
                if (name.equalsIgnoreCase("exit")) break;
                if (name.isEmpty()) {
                    System.out.println("⚠ Tên khóa học không được để trống!");
                    continue;
                }

                System.out.print("➜ Nhập thời lượng (số giờ): ");
                String durationStr = sc.nextLine().trim();
                if (durationStr.equalsIgnoreCase("exit")) break;

                int duration = 0;
                try {
                    duration = Integer.parseInt(durationStr);
                    if (duration <= 0) {
                        System.out.println("⚠ Thời lượng phải lớn hơn 0!");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Thời lượng phải là một số nguyên (Ví dụ: 18, 36)!");
                    continue;
                }
                System.out.print("➜ Nhập tên giảng viên: ");
                String instructor = sc.nextLine().trim();
                if (instructor.equalsIgnoreCase("exit")) break;

                if (instructor.isEmpty()) {
                    System.out.println("⚠ Tên giảng viên không được để trống!");
                    continue;
                }
                System.out.println("═══════════════════════════════════");
                boolean isSuccess = services.addCourse(name, duration, instructor);
                if (isSuccess) {
                    System.out.println("✔ Thêm khóa học thành công!");
                    System.out.print("➜ Bạn có muốn thêm khóa khác không? (y/n): ");
                    if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                        break;
                    }
                } else {
                    System.out.println("⚠ Thêm thất bại! Có thể do lỗi hệ thống.");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
