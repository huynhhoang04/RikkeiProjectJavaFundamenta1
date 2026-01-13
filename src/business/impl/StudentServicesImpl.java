package business.impl;


import business.IStudentServices;
import dao.impl.StudentDAOImpl;
import model.Course;
import model.Enrollment;
import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentServicesImpl implements IStudentServices {
    Scanner input = new Scanner(System.in);
    private StudentDAOImpl dao = new StudentDAOImpl();
    @Override
    public boolean authStudent(String email, String password) {
        Student student = dao.checkStudent(email, password);
        if (student != null) {
            return true;
        }
        return false;
    }

    @Override
    public void showListCourses() {
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        List<Course> listCourse = dao.listCourses();
        for (Course course : listCourse) {
            System.out.println(course);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Nhấn phím bất kỳ để trờ về : ");
        input.nextLine();
    }

    @Override
    public void findCourse() {
        while(true){
            try {
                System.out.println("============================");
                System.out.println("Nhập từ khóa : ");
                String key = input.nextLine();
                if(key.isBlank()){
                    System.out.println("Không được bỏ trống! Vui lòng nhập lại : ");
                    continue;
                }
                else{
                    System.out.println("---------------------------------------------------------");
                    List<Course> listCourse = dao.findCourse(key);
                    for (Course course : listCourse) {
                        System.out.println(course);
                    }
                    System.out.println("----------------------------------------------------------");
                    break;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerCourse(int studentID) {
        while(true){
            try {
                System.out.println("============================");
                System.out.print("Nhập id khóa học muốn đăng ký : ");
                int courseID = Integer.parseInt(input.nextLine());

                if(!dao.checkCourseExist(courseID)){
                    System.out.println("Khóa học không tồn tại!");
                    continue;
                }else{
                    dao.registerCourse(studentID, courseID);
                }
                input.nextLine();
                break;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showRegistreredTicket(int studentID) {
        while(true){
            try {
                System.out.println("============================");
                List<Enrollment> listEnrollment = dao.listEnrollment(studentID);
                if(listEnrollment == null){
                    System.out.println("Chưa có phiếu đăng ký nào");
                    break;
                }
                else {
                    for(Enrollment enrollment : listEnrollment){
                        dao.showEnrolment(enrollment.getId());
                    }
                }
                System.out.println("============================");
                input.nextLine();
                break;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showSortEnrollment(int studentID) {
        List<Enrollment> sortlistEnrollment = new ArrayList<>();
        System.out.println("================================");
        System.out.println("1. Sắp xếp theo tên khóa học");
        System.out.println("2. Sắp xếp theo thời gian đăng ký");
        System.out.println("3. Trở về");
        System.out.println("================================");
        while(true){
            try {
                switch (input.nextLine()) {
                    case "1":
                        System.out.println("================================");
                        System.out.println("1. Sắp xếp tăng dần");
                        System.out.println("2. Sắp xếp giảm dần");
                        System.out.println("================================");
                        String input1 = input.nextLine();
                        if(input1.equals("1")){
                            sortlistEnrollment = dao.sortEnrollment(studentID, "name", "td");
                            for(Enrollment enrollment : sortlistEnrollment){
                                dao.showEnrolment(enrollment.getId());
                            }
                            break;
                        }
                        else if(input1.equals("2")){
                            sortlistEnrollment = dao.sortEnrollment(studentID, "name", "gd");
                            for(Enrollment enrollment : sortlistEnrollment){
                                dao.showEnrolment(enrollment.getId());
                            }
                            break;
                        }
                        else {
                            System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
                            continue;
                        }
                    case "2":
                        System.out.println("================================");
                        System.out.println("1. Sắp xếp tăng dần");
                        System.out.println("2. Sắp xếp giảm dần");
                        System.out.println("================================");
                        String input2 = input.nextLine();
                        if(input2.equals("1")){
                            sortlistEnrollment = dao.sortEnrollment(studentID, "registered_at", "td");
                            for(Enrollment enrollment : sortlistEnrollment){
                                dao.showEnrolment(enrollment.getId());
                            }
                            break;
                        }
                        else if(input2.equals("2")){
                            sortlistEnrollment = dao.sortEnrollment(studentID, "registered_at", "gd");
                            for(Enrollment enrollment : sortlistEnrollment){
                                dao.showEnrolment(enrollment.getId());
                            }
                            break;
                        }
                        else {
                            System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
                            continue;
                        }
                    case "3":
                        return;
                    default:
                        System.out.println("Lựa trọn Invalid, vui lòng chọn lại :");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancerEnrollment(int studentID) {
         while (true){
             try {
                 System.out.println("============================");
                 System.out.print("Nhập id phiếu đăng ký muốn hủy : ");
                String strid = input.nextLine();
                if(strid.isBlank()){
                    System.out.println("Không được để trống!");
                    continue;
                }
                if(!strid.matches("\\d+")){
                    System.out.println("Id phải là số");
                    continue;
                }

                int id = Integer.parseInt(strid);
                if(!dao.checkCheckCancerlation(studentID, id)){
                    System.out.println("Phiếu đăng ký không phải của bạn hoặc đã được xác nhận/hủy!");
                    continue;
                }
                else {
                    dao.cancerEnrollment(id);
                    break;
                }
             }
             catch (Exception e){
                 e.printStackTrace();
             }
         }
    }

    @Override
    public void changePassword(int studentID) {
        while (true){
            try {
                System.out.println("============================");
                System.out.println("Xác nhận email : ");
                String email = input.nextLine();
                System.out.println("Xác nhận mật khẩu cũ : ");
                String oldpassword = input.nextLine();
                System.out.println("Nhập mật khẩu mới : ");
                String newpassword = input.nextLine();
                if(oldpassword.equals(newpassword) && newpassword.isBlank() && email.isBlank()){
                    System.out.println("Thông tin không được trống!");
                    continue;
                }
                if(!dao.verification(studentID, oldpassword,email)){
                    System.out.println("Xác thực thât bại!");
                    continue;
                }
                else {
                    dao.changePassword(studentID,newpassword);
                    break;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
