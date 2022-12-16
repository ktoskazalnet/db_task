package org.ktoskazalnet.model.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {
    private String name;
    private String uuid;
    private List<User> userList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return uuid.equals(course.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
