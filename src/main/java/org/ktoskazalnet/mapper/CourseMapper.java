package org.ktoskazalnet.mapper;

import org.ktoskazalnet.model.api.CourseDTO;
import org.ktoskazalnet.model.entity.Course;

import java.util.UUID;

public class CourseMapper {
    public Course toCourse(CourseDTO courseDTO) {
        return Course.builder()
                .name(courseDTO.getName())
                .uuid(String.valueOf(UUID.randomUUID()))
                .build();
    }
}
