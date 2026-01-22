package util;

import model.Student;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper {
    //chức năng để map khóa học nhanh từ resultset ra student tránh thừa code
    public static Student toStudent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Date dateOfBirth = rs.getDate("dob");
        String email = rs.getString("email");
        Boolean gender = rs.getBoolean("gender");
        String genderStr = gender? "Nam" : "Nữ";
        String phone = rs.getString("phone");

        return new Student(id, name, dateOfBirth,email, genderStr, phone);
    }
}