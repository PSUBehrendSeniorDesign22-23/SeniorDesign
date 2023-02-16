package com.behrend.contestmanager.repository;

import com.behrend.contestmanager.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    //This helps with repository search when a user is logging in
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findById(long id);
}
