package org.ktoskazalnet.service;

import lombok.Data;
import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.repository.UserRepository;
import org.ktoskazalnet.model.api.UserDTO;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.exception.UserNotFoundException;
import org.ktoskazalnet.mapper.UserMapper;

import java.util.List;

@Data
public class UserService {
    private final CourseService courseService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public User save(UserDTO userDTO) {
        return userRepository.save(userMapper.toUser(
                userDTO, courseService.findById(userDTO.getUuid())));
    }

    public User findById(UserDTO userDTO) {
        return userRepository.findById(userMapper.toUser(
                userDTO, courseService.findById(userDTO.getUuid())).getId())
                .orElseThrow(() ->
                        new UserNotFoundException("User with id: " + courseService.findById(userDTO.getUuid()) + " not found."));
    }

    public boolean existsById(UserDTO userDTO) {
        Course course = courseService.findById(userDTO.getUuid());
        return userRepository.existsById(userMapper.toUser(userDTO, course).getId());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(UserDTO userDTO) {
        Course course = courseService.findById(userDTO.getUuid());
        Long id = userMapper.toUser(userDTO, course).getId();

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            System.out.println("User successfully deleted.");
        } else {
            throw new UserNotFoundException("User with id: " + id + " not found.");
        }
    }

    public void delete(UserDTO userDTO) {
        Course course = courseService.findById(userDTO.getUuid());
        userRepository.delete(userMapper.toUser(userDTO, course));
    }
}
