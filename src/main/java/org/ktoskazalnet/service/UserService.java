package org.ktoskazalnet.service;

import lombok.Data;
import org.ktoskazalnet.exception.CourseNotFoundException;
import org.ktoskazalnet.model.api.ApplyUserToCourse;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.repository.UserRepository;
import org.ktoskazalnet.model.api.CreateUserRq;
import org.ktoskazalnet.exception.UserNotFoundException;
import org.ktoskazalnet.mapper.UserMapper;

import java.util.List;

@Data
public class UserService {
    private final CourseService courseService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User save(final CreateUserRq createUserRq) {
        var entity = userMapper.toUser(createUserRq);
        return userRepository.save(entity);
    }

    public User findById(final long id) {
        return userRepository.findById(id).
                orElseThrow(() ->
                        new UserNotFoundException("User with id: " + id + " not found."));
    }

    public boolean existsById(final long id) {
        return userRepository.existsById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(final long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            System.out.println("User successfully deleted.");
        } else {
            throw new UserNotFoundException("User with id: " + id + " not found.");
        }
    }

    public Course addUserToCourse(ApplyUserToCourse applyUserToCourse) {
        long userId = applyUserToCourse.getId();
        String courseUuid = applyUserToCourse.getCourseUuid();

        if (!existsById(userId))
            throw new UserNotFoundException("User with id: " + userId + " not found.");
        if (!courseService.existsById(courseUuid))
            throw new CourseNotFoundException("Course with id: " + courseUuid + " not found.");
        return userRepository.addUserToCourse(userId, courseUuid);
    }

    public Course removeFromCourse(ApplyUserToCourse applyUserToCourse) {
        long userId = applyUserToCourse.getId();
        String courseUuid = applyUserToCourse.getCourseUuid();

        if (!(existsById(userId)))
            throw new UserNotFoundException("User with id: " + userId + " not found.");
        if (!(courseService.existsById(courseUuid)))
            throw new CourseNotFoundException("Course with id: " + courseUuid + " not found.");
        return userRepository.removeFromCourse(userId, courseUuid);
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }
}
