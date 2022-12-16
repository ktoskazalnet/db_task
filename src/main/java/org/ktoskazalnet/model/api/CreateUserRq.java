package org.ktoskazalnet.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRq {
    private String name;
    private int age;
}
