package model;

import java.util.Date;

public class Student implements Comparable<Student>{

    private int id;
    private String name;
    private Date dateOfBirth;
    private String email;
    private String gender;
    private String phoneNumber;
    private String password;
    private Date created_at;

    public Student(int id, String name, Date dateOfBirth, String email, String gender, String phoneNumber, String password, Date created_at) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.created_at = created_at;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
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

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }


    @Override
    public String toString() {
        return "|" + id + "|" + name + "|" + dateOfBirth + "|" + email + "|" + gender + "|" + phoneNumber + "|" ;
    }
    @Override
    public int compareTo(Student o) {
        return 0;
    }
}
