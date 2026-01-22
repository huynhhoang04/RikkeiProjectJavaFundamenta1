package business.impl;

import business.IAdminSevices;
import dao.IAdminDAO;
import model.Admin;
import model.Course;
import model.Student;
import model.dto.EnrollmentDetailDTO;

import java.util.*;
import java.util.stream.Collectors;

public class AdminSevicesImpl implements IAdminSevices {
    private final IAdminDAO dao;

    public AdminSevicesImpl(IAdminDAO dao) {
        this.dao = dao;
    }

    @Override
    public Admin login(String username, String password) {
        Admin admin = dao.checkAdmin(username, password);
        return admin;
    }

    @Override
    public List<Course> showListCourse() {
        return dao.listCourse();
    }

    @Override
    public boolean addCourse(String name, int duration, String instructor) {
        if (duration <= 0) return false;
        return dao.addCourse(name, duration, instructor);
    }

    @Override
    public boolean checkCourse(int courseId) {
        return dao.checkCourse(courseId);
    }

    @Override
    public boolean updateCourseName(int id, String newName) {
        return dao.updateCourseName(id, newName);
    }

    @Override
    public boolean updateCourseDuration(int id, int newDuration) {
        return dao.updateCourseDuration(id, newDuration);
    }

    @Override
    public boolean updateCourseInstructor(int id, String newInstructor) {
        return dao.updateCourseInstructor(id, newInstructor);
    }

    @Override
    public List<Course> findCourseByKey(String key) {
        return dao.findCourse(key);
    }

    @Override
    public List<Course> sortListCourse(String sortBy, String sortOrder) {
        List<Course> list = dao.listCourse();
        if (list.isEmpty()) {
            return list;
        }
        Comparator<Course> comparator = null;
        if (sortBy.equalsIgnoreCase("name")) {
            comparator = (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName());
        } else if (sortBy.equalsIgnoreCase("duration")) {
            comparator = (c1, c2) -> Integer.compare(c1.getDuration(), c2.getDuration());
        }
        if (comparator != null) {
            if (sortOrder.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            list.sort(comparator);
        }
        return list;
    }

    @Override
    public boolean deleteCourse(int id) {
        if (dao.checkCourseHasStudent(id)) {
            return false;
        }
        return dao.deleteCourse(id);
    }

    @Override
    public List<Student> showListStudent() {
        return dao.listStudent();
    }

    @Override
    public boolean addStudent(String name, Date dob, String email, boolean gender, String phone, String password)  {
        if (dao.checkEmailExists(email)) {
            return false;
        }

        return dao.addStudent(name, dob, email, gender, phone, password);
    }

    @Override
    public boolean checkStudent(int id) {
        return dao.checkStudentExists(id);
    }

    @Override
    public boolean editStudent(int id, String fieldName, String newValue)  {
        if (fieldName.equalsIgnoreCase("email") && dao.checkEmailExists(newValue)) {
            return false;
        }
        return dao.editStudent(id, fieldName, newValue);
    }

    @Override
    public List<Student> findStudent(String key, String searchBy) {
        return dao.findStudent(key, searchBy);
    }

    @Override
    public List<Student> sortListStudent(String sortBy, String sortOrder) {
        List<Student> list = dao.listStudent();
        if (list.isEmpty()) {
            return list;
        }
        Comparator<Student> comparator = null;
        if (sortBy.equalsIgnoreCase("name")) {
            comparator = (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName());

        } else if (sortBy.equalsIgnoreCase("email")) {
            comparator = (s1, s2) -> s1.getEmail().compareToIgnoreCase(s2.getEmail());
        }
        if (comparator != null) {
            if (sortOrder.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            list.sort(comparator);
        }
        return list;
    }

    @Override
    public boolean deleteStudent(int id) {
        if (dao.checkStudentAttend(id)) {
            return false;
        }
        return dao.deleteStudent(id);
    }

    @Override
    public List<EnrollmentDetailDTO> getCourseEnrollments(int courseId) {
        if(!dao.checkCourse(courseId)){
            return null;
        }
        return dao.listStudentByCourse(courseId);
    }

    @Override
    public List<EnrollmentDetailDTO> getPendingEnrollments(int courseId) {
        if(!dao.checkCourse(courseId)){
            return null;
        }
        return dao.getPendingEnrollments(courseId);
    }

    @Override
    public boolean approveEnrollment(int enrollmentId) {
        return dao.clarifyEnrollment(enrollmentId, "CONFIRM");
    }

    @Override
    public boolean denyEnrollment(int enrollmentId) {
        return dao.clarifyEnrollment(enrollmentId, "DENIED");
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        return dao.deleteEnrollment(enrollmentId);
    }

    @Override
    public Map<String, Integer> showTotalCoursesAndStudents() {
        return dao.totalCourseAndStudent();
    }

    @Override
    public Map<String, Integer> showTotalStudentsByCourse() {
        return dao.analyzeTotalStudentByCourse();
    }

    @Override
    public Map<String, Integer> Top5CourseWithStudents() {
        Map<String, Integer> data = dao.analyzeTotalStudentByCourse();
        return data.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new ));
    }

    @Override
    public Map<String, Integer> CourseWithMoreThan10Students() {
        Map<String, Integer> data = dao.analyzeTotalStudentByCourse();
        return data.entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e2, LinkedHashMap::new));
    }
}


