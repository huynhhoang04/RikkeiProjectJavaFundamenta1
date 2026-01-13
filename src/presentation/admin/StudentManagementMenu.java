package presentation.admin;

import business.impl.AdminSevicesImpl;

import java.util.Scanner;

public class StudentManagementMenu {
    Scanner sc = new Scanner(System.in);
    AdminSevicesImpl service = new AdminSevicesImpl();

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
                        service.showListStudent();
                        break;
                    case "2":
                        service.addStudent();
                        break;
                    case "3":
                        service.editStudent();
                        break;
                    case "4":
                        service.deleteStudent();
                        break;
                    case "5":
                        service.findStudent();
                        break;
                    case "6":
                        service.sortListStudent();
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
}
