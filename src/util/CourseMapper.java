package util;

import model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper {
    public static Course toCourse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int duration = rs.getInt("duration");
        String instructor = rs.getString("instructor");
        java.util.Date created_at = rs.getDate("created_at");

        return new Course(id, name, duration, instructor, created_at);
    }
}
