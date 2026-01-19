package business.impl;


import business.IStudentServices;
import dao.IStudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StudentServicesImpl implements IStudentServices {
    Scanner input = new Scanner(System.in);
    private final IStudentDAO dao;

    public StudentServicesImpl(IStudentDAO dao) {
        this.dao = dao;
    }

    @Override
    public Student login(String email, String password) {
        Student student = dao.checkStudent(email, password);
        return student;
    }

    @Override
    public List<Course> showListCourses() {
        return dao.listCourses();
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
    public boolean registerCourse(int studentID, int courseID) {
        if (!dao.checkCourseExist(courseID)) {
            return false;
        }
        return dao.registerCourse(studentID, courseID);
    }

    @Override
    public List<EnrollmentDetailDTO> getHistory(int studentId) {
        return dao.getHistory(studentId);
    }

    @Override
    public List<EnrollmentDetailDTO> sortEnrollment(int studentId, String sortBy, String sortOrder) {
        List<EnrollmentDetailDTO> list = dao.getHistory(studentId);

        if (list.isEmpty()) return list;

        if (sortBy.equalsIgnoreCase("name")) {
            list.sort((o1, o2) -> o1.getCourseName().compareToIgnoreCase(o2.getCourseName()));

        } else if (sortBy.equalsIgnoreCase("time")) {
            list.sort((o1, o2) -> o1.getRegisteredAt().compareTo(o2.getRegisteredAt()));
        }

        if (sortOrder.equalsIgnoreCase("gd")) {
            Collections.reverse(list);
        }

        return list;
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
