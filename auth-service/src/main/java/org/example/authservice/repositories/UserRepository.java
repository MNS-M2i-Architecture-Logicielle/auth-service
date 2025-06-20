package org.example.authservice.repositories;

import org.example.authservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * Extends JpaRepository to provide standard CRUD operations.
 * Adds custom query methods to find users by email and check email existence.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the specified email address.
     *
     * @param email the email address to check for existence
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);
}
