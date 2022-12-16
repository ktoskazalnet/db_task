package org.ktoskazalnet.repository;

import lombok.AllArgsConstructor;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.model.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.ktoskazalnet.util.DbConnection.getConnection;

@AllArgsConstructor
public class UserRepository implements CrudRepository<User, Long> {
    private static Connection connection;

    @Override
    public User save(final User entity) {
        connection = getConnection();
        var sql = "INSERT INTO users (name, age, course_uuid) VALUES (?, ?, null)";
        try (var preparedStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getAge());
            preparedStatement.executeUpdate();
            connection.commit();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                while (generatedKeys.next()) {
                    long id = generatedKeys.getLong("id");
                    entity.setId(id);
                    System.out.printf("%s added with id %s", entity, id + '\n');
                }
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    @Override
    public Optional<User> findById(final Long id) {
        connection = getConnection();
        var sql = "SELECT * FROM users FULL JOIN course ON users.course_uuid = course.uuid WHERE id = ?";
        User user = null;
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    user = User.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .age(resultSet.getInt("age"))
                            .course(Course.builder()
                                    .uuid(resultSet.getString("uuid"))
                                    .name(resultSet.getString(6))
                                    .build())
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean existsById(final Long id) {
        connection = getConnection();
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> findAll() {
        connection = getConnection();
        var userList = new ArrayList<User>();
        var sql = "SELECT * FROM users FULL JOIN course ON users.course_uuid = course.uuid";

        try (var preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .age(resultSet.getInt("age"))
                        .course(Course.builder()
                                .uuid(resultSet.getString("uuid"))
                                .name(resultSet.getString(6))
                                .build())
                        .build();
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    @Override
    public void deleteById(final Long id) {
        connection = getConnection();
        var sql = "DELETE FROM users WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Course addUserToCourse(final Long id, final String uuid) {
        connection = getConnection();
        Course course = Course.builder().uuid(uuid).build();

        var update = "UPDATE users SET course_uuid = ? WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setLong(2, id);
            connection.commit();

            course.setName(getCourseNameByUuid(uuid));
            course.setUserList(getListOfCourseUsers(uuid));
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                throw new RuntimeException();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return course;
    }

    public Course removeFromCourse(final long id, final String uuid) {
        connection = getConnection();
        var delete = "DELETE FROM users WHERE id = ?";
        Course course = Course.builder()
                .uuid(uuid)
                .name(getCourseNameByUuid(uuid))
                .userList(getListOfCourseUsers(uuid))
                .build();

        try (var preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            System.out.printf("User with id: %s deleted from course: %s with uuid: %s", id, course.getName(), uuid);

            course.getUserList().remove(findById(id).get());
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return course;
    }

    private List<User> getListOfCourseUsers(final String uuid) {
        connection = getConnection();
        var userList = new ArrayList<User>();
        var select = "SELECT * FROM users JOIN course ON users.course_uuid = course.uuid WHERE course_uuid = ?";

        try (var preparedStatement1 = connection.prepareStatement(select)) {
            preparedStatement1.setString(1, uuid);
            try (var resultSet = preparedStatement1.executeQuery()) {

                while (resultSet.next()) {
                    User user = User.builder()
                            .id(resultSet.getLong(1))
                            .name(resultSet.getString(2))
                            .age(resultSet.getInt(3))
                            .course(Course.builder()
                                    .uuid(resultSet.getString(4))
                                    .name(resultSet.getString(6))
                                    .build())
                            .build();
                    userList.add(user);
                }
                return userList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
            connection.rollback();
            throw new RuntimeException();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    private String getCourseNameByUuid(final String uuid) {
        connection = getConnection();
        String courseName = null;
        var sql = "SELECT * FROM course WHERE uuid LIKE ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    courseName = resultSet.getString("name");
                }
            }

        } catch (SQLException e) {
            try {
                e.printStackTrace();
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return courseName;
    }

    @Override
    public void delete(final User entity) {
        deleteById(entity.getId());
    }
}
