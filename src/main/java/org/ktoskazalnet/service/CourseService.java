package org.ktoskazalnet.service;

import lombok.RequiredArgsConstructor;
import org.ktoskazalnet.mapper.CourseMapper;
import org.ktoskazalnet.repository.CourseRepository;
import org.ktoskazalnet.model.api.CreateCourseRq;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.exception.CourseNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;

    public Course save(final CreateCourseRq createCourseRq) {
        var entity = courseMapper.toCourse(createCourseRq);
        return courseRepository.save(entity);
    }

    public Course findById(final String uuid) {
        return courseRepository.findById(uuid)
                .orElseThrow(() -> new CourseNotFoundException("Course with uuid: " + uuid + " not found."));
    }

    public boolean existsById(final String uuid) {
        return courseRepository.existsById(uuid);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void deleteById(final String uuid) {
        if (courseRepository.existsById(uuid)) {
            courseRepository.deleteById(uuid);
            System.out.println("Course successfully deleted.");
        } else {
            throw new CourseNotFoundException("Course with id: " + uuid + " not found.");
        }
    }

    public void delete(Course course) {
        courseRepository.delete(course);
    }
}
