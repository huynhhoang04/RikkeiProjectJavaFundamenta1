package presentation.admin;

import business.impl.AdminSevicesImpl;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;
import model.Course;

import java.util.List;
import java.util.Scanner;

public class CourseManagementMenu {
    Scanner sc = new Scanner(System.in);
    IAdminDAO dao = new AdminDAOImpl();
    AdminSevicesImpl services = new AdminSevicesImpl(dao);
    public void showMenu()
    {
        while (true){
            try {
                System.out.println("================================");
                System.out.println("1. Hiển thị danh sách khóa học");
                System.out.println("2. Thêm mới khóa học");
                System.out.println("3. Chỉnh sửa thông tin khóa học ");
                System.out.println("4. Xóa khóa học ");
                System.out.println("5. Tìm kiếm theo tên");
                System.out.println("6. Sắp xếp theo tên hoặc id");
                System.out.println("7. Quay về menu chính");
                System.out.println("================================");
                System.out.print("Nhập lựa chọn : ");

                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        handleShowListCourses();
                        break;
                    case 2:

                        break;
                    case 3:
                        handleUpdateCourse();
                        break;
                    case 4:
                        handleDeleteCourse();
                        break;
                    case 5:
                        handleFindCourse();
                        break;
                    case 6:
                        handleShowListCourses();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Lựa trọn Invalid");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void printCourseList(List<Course> list) {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-25s | %-10s | %-15s |\n", "ID", "Tên Khóa Học", "Thời gian", "Giảng viên");
        System.out.println("-------------------------------------------------------------------------------");
        for (Course c : list) {
            System.out.printf("| %-5d | %-25s | %-10d | %-15s |\n",
                    c.getId(), c.getName(), c.getDuration(), c.getInstructor());
        }
        System.out.println("-------------------------------------------------------------------------------");
    }

    public void handleUpdateCourse() {
        Scanner sc = new Scanner(System.in);

        // 1. Nhập ID khóa học cần sửa
        System.out.print("Nhập ID khóa học muốn sửa: ");
        int id = 0;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ ID phải là số!");
            return;
        }

        // Kiểm tra ID có tồn tại không (Ông có thể thêm hàm checkCourse vào Service nếu muốn kỹ)
        // ...

        while (true) {
            System.out.println("\n========== SỬA KHÓA HỌC ==========");
            System.out.println("1. Sửa Tên");
            System.out.println("2. Sửa Thời lượng");
            System.out.println("3. Sửa Giảng viên");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            String choice = sc.nextLine();
            boolean isSuccess = false;

            switch (choice) {
                case "1":
                    System.out.print("Nhập tên mới: ");
                    String name = sc.nextLine();
                    isSuccess = services.updateCourseName(id, name);
                    break;

                case "2":
                    System.out.print("Nhập thời lượng mới (số): ");
                    try {
                        int duration = Integer.parseInt(sc.nextLine());
                        isSuccess = services.updateCourseDuration(id, duration);
                    } catch (Exception e) {
                        System.out.println("❌ Thời lượng phải là số nguyên!");
                        continue;
                    }
                    break;

                case "3":
                    System.out.print("Nhập tên giảng viên mới: ");
                    String instructor = sc.nextLine();
                    isSuccess = services.updateCourseInstructor(id, instructor);
                    break;

                case "0":
                    return; // Thoát

                default:
                    System.out.println("❌ Chọn sai!");
                    continue;
            }

            // Thông báo kết quả
            if (isSuccess) {
                System.out.println("✅ Cập nhật thành công!");
            } else {
                System.out.println("❌ Cập nhật thất bại (ID không tồn tại hoặc lỗi hệ thống).");
            }
        }
    }

    public void handleShowListCourses() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n========== DANH SÁCH KHÓA HỌC ==========");

        // 1. GỌI SERVICE
        List<Course> list = services.showListCourse();

        // 2. HIỂN THỊ
        if (list.isEmpty()) {
            System.out.println("⚠️ Hệ thống chưa có khóa học nào!");
        } else {
            // Kẻ bảng header
            System.out.println("-------------------------------------------------------------------------------");
            // Giả sử Course có: id, name, duration, instructor, status
            System.out.printf("| %-5s | %-25s | %-10s | %-15s |\n",
                    "ID", "Tên Khóa Học", "Thời gian", "Giảng viên");
            System.out.println("-------------------------------------------------------------------------------");

            // Loop in dữ liệu
            for (Course c : list) {
                System.out.printf("| %-5d | %-25s | %-10d | %-15s |\n",
                        c.getId(),
                        c.getName(),
                        c.getDuration(),
                        c.getInstructor());
            }
            System.out.println("-------------------------------------------------------------------------------");
        }

        // Dừng màn hình để user đọc
        System.out.println("Ấn Enter để quay lại...");
        sc.nextLine();
    }

    public void handleSortCourse() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== SẮP XẾP KHÓA HỌC ==========");
            System.out.println("1. Theo Tên (A -> Z)");
            System.out.println("2. Theo Tên (Z -> A)");
            System.out.println("3. Theo Thời lượng (Thấp -> Cao)");
            System.out.println("4. Theo Thời lượng (Cao -> Thấp)");
            System.out.println("0. Quay lại");
            System.out.print("👉 Mời chọn: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("0")) break;

            String sortBy = "";
            String sortOrder = "";

            switch (choice) {
                case "1":
                    sortBy = "name"; sortOrder = "td"; // Tăng dần
                    break;
                case "2":
                    sortBy = "name"; sortOrder = "gd"; // Giảm dần
                    break;
                case "3":
                    sortBy = "duration"; sortOrder = "td"; // Tăng dần
                    break;
                case "4":
                    sortBy = "duration"; sortOrder = "gd"; // Giảm dần
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ!");
                    continue;
            }

            // --- GỌI SERVICE ---
            List<Course> result = services.sortListCourse(sortBy, sortOrder);

            // --- IN KẾT QUẢ ---
            if (result.isEmpty()) {
                System.out.println("⚠️ Danh sách trống.");
            } else {
                System.out.println("-------------------------------------------------------------------------------");
                System.out.printf("| %-5s | %-25s | %-10s | %-15s |\n", "ID", "Tên Khóa Học", "Thời gian", "Giảng viên");
                System.out.println("-------------------------------------------------------------------------------");

                for (Course c : result) {
                    System.out.printf("| %-5d | %-25s | %-10d | %-15s |\n",
                            c.getId(), c.getName(), c.getDuration(), c.getInstructor());
                }
                System.out.println("-------------------------------------------------------------------------------");
            }

            System.out.println("Ấn Enter để tiếp tục...");
            sc.nextLine();
        }
    }

    public void handleFindCourse() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== TÌM KIẾM KHÓA HỌC ==========");
            System.out.print("👉 Nhập từ khóa tên môn học (hoặc ấn Enter để quay lại): ");
            String key = sc.nextLine().trim();

            // Nếu user không nhập gì và enter -> Thoát
            if (key.isEmpty()) break;

            // GỌI SERVICE (Hàm findCourseByKey ông vừa viết)
            List<Course> result = services.findCourseByKey(key);

            // HIỂN THỊ KẾT QUẢ
            if (result.isEmpty()) {
                System.out.println("❌ Không tìm thấy khóa học nào chứa: \"" + key + "\"");
            } else {
                System.out.println("✅ Tìm thấy " + result.size() + " kết quả:");
                // Gọi lại hàm in bảng cho đẹp (Hàm printCourseList tôi đưa ở tin nhắn trước)
                printCourseList(result);
            }
        }
    }

    public void handleDeleteCourse() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== XÓA KHÓA HỌC ==========");
            // Nên hiển thị danh sách để user biết ID mà xóa
            // services.showListCourse(); (Tùy ông)

            System.out.print("👉 Nhập ID khóa học cần xóa (hoặc 0 để quay lại): ");
            String input = sc.nextLine().trim();

            // Validate nhập số
            if (!input.matches("\\d+")) {
                System.out.println("❌ ID phải là số nguyên!");
                continue;
            }

            int courseId = Integer.parseInt(input);
            if (courseId == 0) break; // Thoát

            // Xác nhận lại cho chắc (Xóa là việc quan trọng)
            System.out.print("⚠️ Bạn có chắc chắn muốn xóa khóa học ID " + courseId + "? (y/n): ");
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("🚫 Đã hủy thao tác xóa.");
                continue;
            }

            // GỌI SERVICE
            boolean isDeleted = services.deleteCourse(courseId);

            // XỬ LÝ KẾT QUẢ
            if (isDeleted) {
                System.out.println("✅ Xóa khóa học thành công!");
                break; // Xóa xong thì thoát ra menu chính luôn
            } else {
                System.out.println("❌ Xóa thất bại!");
                System.out.println("👉 Nguyên nhân: ID không tồn tại HOẶC Khóa học đang có học viên theo học.");
            }
        }
    }
}
