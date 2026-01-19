package model.dto;

import java.util.Date;

public class EnrollmentDetailDTO {
    private int id;
    private String studentName;
    private String courseName;
    private Date registeredAt;
    private String status;

    public EnrollmentDetailDTO(int id, String studentName, String courseName, Date registeredAt, String status) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
        this.registeredAt = registeredAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
