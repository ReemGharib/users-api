package com.users.api.repository;


import com.users.api.dto.UserDto;
import com.users.api.exception.UserAlreadyExistsException;
import com.users.api.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Reem Gharib
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findUserByUidEqualsAndIdIsNot(String uid, long id);

    Optional<User> findUserByEmailEqualsAndIdIsNot(String email, long id);

    Page<User> findAll(Specification<User> userSpecification, Pageable pageable);

    Long count(Specification<User> caseSpecification);

    /**
     * Default implementation of filter Users
     *
     * @param user the user entity
     * @return Specification<User>
     */
    default Specification<User> filterUsers(UserDto user) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (user.getUid() != null) {
                predicates.add(criteriaBuilder.like(root.get("uid"), "%" + user.getUid() + "%"));
            }

            if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("firstName")), "%" + user.getFirstName().toUpperCase() + "%"));
            }

            if (user.getLastName() != null && !user.getLastName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("lastName")), "%" + user.getLastName().toUpperCase() + "%"));
            }

            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("email")), "%" + user.getEmail().toUpperCase() + "%"));
            }

            if (user.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), criteriaBuilder.literal(user.getRole().toUpperCase())));
            }

            if (user.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), user.getIsActive()));
            }

            return !predicates.isEmpty() ? criteriaBuilder.and(predicates.toArray(new Predicate[0])) : null;
        };
    }

    /**
     * y
     * Check if user already exists by uid
     *
     * @param uid the uid
     */
    default void checkUserExistsByUid(String uid) {
        findByUid(uid).ifPresent(existingUser -> {
            throw new UserAlreadyExistsException(String.format("User with ccgId [%s] already exists", uid));
        });
    }

    /**
     * Check if user already exists by email
     *
     * @param email the email
     */
    default void checkUserExistsByEmail(String email) {
        findByEmailIgnoreCase(email).ifPresent(existingUser -> {
            throw new UserAlreadyExistsException(String.format("User with email [%s] already exists", email));
        });
    }
}