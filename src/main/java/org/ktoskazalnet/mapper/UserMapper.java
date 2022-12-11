package org.ktoskazalnet.mapper;

import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.model.api.UserDTO;
import org.ktoskazalnet.model.entity.Course;

public class UserMapper {
    public User toUser(UserDTO userDTO, Course course) {
        return User.builder()
                .course(course)
                .name(userDTO.getName())
                .age(userDTO.getAge())
                .build();
    }
}
