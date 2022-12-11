package org.ktoskazalnet.repository;

import lombok.AllArgsConstructor;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.service.CourseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository implements CrudRepository<User, Long> {
    private static final Connection connection;

    private final CourseRepository courseRepository;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/study_db?user=postgres&password=psql";

    static {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User entity) {
        String sql = "INSERT INTO users VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getAge());
            preparedStatement.setString(3, entity.getCourse().getName());
            preparedStatement.executeUpdate();
            System.out.println("User successfully added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .age(resultSet.getInt("age"))
                        .course(courseRepository.findById(resultSet.getString("uuid")).get())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery()
            preparedStatement.setLong(1, id);

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
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .age(resultSet.getInt("age"))
                        .course(courseRepository.findById(resultSet.getString("uuid")).get())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User entity) {
        deleteById(entity.getId());
    }
}
