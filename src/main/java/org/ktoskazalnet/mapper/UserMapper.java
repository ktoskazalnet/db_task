package org.ktoskazalnet.mapper;

import org.ktoskazalnet.model.entity.User;
import org.ktoskazalnet.model.api.CreateUserRq;

public class UserMapper {
    public User toUser(CreateUserRq createUserRq) {
        return User.builder()
                .name(createUserRq.getName())
                .age(createUserRq.getAge())
                .build();
    }
}
