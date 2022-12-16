package org.ktoskazalnet;

import org.ktoskazalnet.mapper.CourseMapper;
import org.ktoskazalnet.mapper.UserMapper;
import org.ktoskazalnet.model.api.ApplyUserToCourse;
import org.ktoskazalnet.model.api.CreateCourseRq;
import org.ktoskazalnet.model.api.CreateUserRq;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.repository.CourseRepository;
import org.ktoskazalnet.repository.UserRepository;
import org.ktoskazalnet.service.CourseService;
import org.ktoskazalnet.service.UserService;
import org.ktoskazalnet.util.DbConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main( String[] args ) {

        initDb();

        CourseMapper courseMapper = new CourseMapper();
        CourseRepository courseRepository = new CourseRepository();
        CourseService courseService = new CourseService(courseMapper, courseRepository);

        UserMapper userMapper = new UserMapper();
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(courseService, userMapper, userRepository);

        userService.removeFromCourse(ApplyUserToCourse.builder()
                        .id(3)
                        .courseUuid("aaaa4")
                        .build());


    }

    private static void initDb() {
        try (var connection = DbConnection.getConnection();
             Statement statement = connection.createStatement()) {

            var createCourse = "CREATE TABLE IF NOT EXISTS course (" +
                    "uuid TEXT NOT NULL, " +
                    "name TEXT NOT NULL," +
                    "PRIMARY KEY (uuid)" +
                    ")";

            var createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL," +
                    "name TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "course_uuid TEXT," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (course_uuid)" +
                    "REFERENCES course (uuid)" +
                    ");";

            statement.execute(createCourse);
            statement.execute(createUsers);

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
