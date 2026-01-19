package util;

import model.Student;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper {
    public static Student toStudent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Date dateOfBirth = rs.getDate("dob");
        String email = rs.getString("email");
        Boolean gender = rs.getBoolean("gender");
        String genderStr = gender? "Nam" : "Nữ";
        String phone = rs.getString("phone");
        String password = rs.getString("password");
        Date created_at = rs.getDate("created_at");

        return new Student(id, name, dateOfBirth,email, genderStr, phone, password, created_at);
    }
}