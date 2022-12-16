package org.ktoskazalnet.mapper;

import org.ktoskazalnet.model.api.CreateCourseRq;
import org.ktoskazalnet.model.entity.Course;

import java.util.UUID;

public class CourseMapper {
    public Course toCourse(CreateCourseRq createCourseRq) {
        return Course.builder()
                .name(createCourseRq.getName())
                .uuid(String.valueOf(UUID.randomUUID()))
                .build();
    }
}
