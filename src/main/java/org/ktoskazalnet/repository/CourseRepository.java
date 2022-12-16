package org.ktoskazalnet.repository;

import lombok.AllArgsConstructor;
import org.ktoskazalnet.model.entity.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.ktoskazalnet.util.DbConnection.*;

@AllArgsConstructor
public class CourseRepository implements CrudRepository<Course, String> {
    private static Connection connection;

    @Override
    public Course save(Course entity) {
        connection = getConnection();
        var sql = "INSERT INTO course VALUES(?, ?)";
        try (var preparedStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getUuid());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.executeUpdate();
            connection.commit();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    while (generatedKeys.next()) {
                        String uuid = generatedKeys.getString("uuid");
                        entity.setUuid(uuid);
                        System.out.printf("%s added with uuid: %s", entity, uuid + '\n');
                    }
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<Course> findById(String uuid) {
        connection = getConnection();
        Course course = null;
        String sql = "SELECT * FROM course WHERE uuid LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    course = Course.builder()
                            .uuid(resultSet.getString("uuid"))
                            .name(resultSet.getString("name"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(course);
    }

    @Override
    public boolean existsById(String uuid) {
        connection = getConnection();
        var sql = "SELECT * FROM course WHERE uuid LIKE ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findAll() {
        connection = getConnection();
        var courseList = new ArrayList<Course>();
        var sql = "SELECT * FROM course";
        try (var statement = connection.createStatement();
            var resultSet = statement.executeQuery(sql)) {

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
        connection = getConnection();
        var sql = "DELETE FROM course WHERE uuid LIKE ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                e.printStackTrace();
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void delete(Course entity) {
        deleteById(entity.getUuid());
    }
}
