package presentation.admin;

import business.impl.AdminSevicesImpl;

import java.util.Scanner;

public class CourseManagementMenu {
    Scanner sc = new Scanner(System.in);
    AdminSevicesImpl service = new AdminSevicesImpl();
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
                        service.showListCourse();
                        break;
                    case 2:
                        service.addCourse();
                        break;
                    case 3:
                        service.editCourse();
                        break;
                    case 4:
                        service.deleteCourse();
                        break;
                    case 5:
                        service.findCourseByKey();
                        break;
                    case 6:
                        service.sortListCourse();
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
}
