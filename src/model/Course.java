package model;

import java.util.Date;

public class Course implements Comparable<Course>{

    private int id;
    private String name;
    private int duration;
    private String instructor;
    private Date created_at;

    public Course(int id, String name, int duration, String instructor, Date created_at) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
        this.created_at = created_at;
    }


    public Course(String name, int duration, String instructor, Date created_at) {
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public Date getCreated_at() {
        return created_at;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return String.format("| Tên khóa học : %-25s | Thời lượng : %-5d Giờ | Giảng viên : %-20s | Thời gian tạo : %s |",  name, duration, instructor,  created_at);
    }

    @Override
    public int compareTo(Course other) {
        return 0;
    }
}
