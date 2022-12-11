package org.ktoskazalnet.repository;

import org.ktoskazalnet.model.entity.User;

import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<User, Long> {
    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(User entity) {

    }
}
