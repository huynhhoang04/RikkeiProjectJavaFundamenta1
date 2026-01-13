package model;

import java.util.Date;

public class Enrollment {
    private int id;
    private int courseid;
    private int studentid;
    private Date registrationdate;
    private String status;

    public Enrollment(int id, int studentid, int courseid, Date registrationdate, String status) {
        this.id = id;
        this.courseid = courseid;
        this.studentid = studentid;
        this.registrationdate = registrationdate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCourseid() {
        return courseid;
    }

    public int getStudentid() {
        return studentid;
    }

    public Date getRegistrationdate() {
        return registrationdate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public void setRegistrationdate(Date registrationdate) {
        this.registrationdate = registrationdate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "|" + id + "|" + courseid + "|" + studentid + "|" + registrationdate + "|" + status + "|";
    }
}
