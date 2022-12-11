package org.ktoskazalnet.repository;

import org.ktoskazalnet.model.entity.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository implements CrudRepository<Course, String> {
    private static final Connection connection;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/study_db?user=postgres&password=psql";

    static {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Course save(Course entity) {
        String sql = "INSERT INTO course VALUES(?, ?)";
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getUuid());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.executeUpdate();
            System.out.println("Course successfully added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<Course> findById(String uuid) {
        Course course = null;
        String sql = "SELECT * FROM course WHERE uuid LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                course = Course.builder()
                        .uuid(resultSet.getString("uuid"))
                        .name(resultSet.getString("name"))
                        .build();
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(course);
    }

    @Override
    public boolean existsById(String uuid) {
        String sql = "SELECT * FROM course WHERE uuid LIKE ?";
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public List<Course> findAll() {
        List<Course> courseList = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Course course = Course.builder()
                        .uuid(resultSet.getString("uuid"))
                        .name(resultSet.getString("name"))
                        .build();
                courseList.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    @Override
    public void deleteById(String uuid) {
        String sql = "DELETE FROM course WHERE uuid LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Course entity) {
        deleteById(entity.getUuid());
    }
}
