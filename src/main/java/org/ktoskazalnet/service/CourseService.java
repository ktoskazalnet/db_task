package org.ktoskazalnet.service;

import lombok.Data;
import org.ktoskazalnet.mapper.CourseMapper;
import org.ktoskazalnet.repository.CourseRepository;
import org.ktoskazalnet.model.api.CourseDTO;
import org.ktoskazalnet.model.entity.Course;
import org.ktoskazalnet.exception.CourseNotFoundException;

import java.util.List;

@Data
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;


    public Course save(CourseDTO courseDTO) {
        return courseRepository.save(courseMapper.toCourse(courseDTO));
    }

    public Course findById(String uuid) {
        return courseRepository.findById(uuid)
                .orElseThrow(() -> new CourseNotFoundException("Course with uuid: " + uuid + " not found."));
    }

    public boolean existsById(String uuid) {
        return courseRepository.existsById(uuid);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void deleteById(String uuid) {
        if (courseRepository.existsById(uuid)) {
            courseRepository.deleteById(uuid);
            System.out.println("Course successfully deleted.");
        } else {
            throw new CourseNotFoundException("Course with id: " + uuid + " not found.");
        }
    }

    public void delete(CourseDTO courseDTO) {
        courseRepository.delete(courseMapper.toCourse(courseDTO));
    }



}
