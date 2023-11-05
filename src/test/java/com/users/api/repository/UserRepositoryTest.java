package com.users.api.repository;

import com.users.api.model.User;
import com.users.api.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Reem Gharib
 */
@DataJpaTest
@ActiveProfiles({"test"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = new User();
        user.setUid("1234567");
        user.setUserName("Reem");
        user.setFirstName("Reem");
        user.setLastName("Gharib");
        user.setEmail("gharib@ttt.com");
        user.setActive(true);
        user.setRole(Role.ADMIN);
    }

    @Test
    void givenACorrectSetup_thenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    void whenGetAllUsers_isOk() {

        this.userRepository.save(this.user);

        this.userRepository.findByUid("1234567");
    }

    @Test
    void whenFindByUid_isOK() {

        this.userRepository.save(this.user);
        Optional<User> response = this.userRepository.findByUid("1234567");

        assertTrue(response.isPresent());
        assertNotNull(response.get());
        assertEquals(this.user, response.get());
    }

    @Test
    void whenFindByEmailIgnoreCase_isOK() {

        this.userRepository.save(this.user);
        Optional<User> response = this.userRepository.findByEmailIgnoreCase("gharib@ttt.com");

        assertTrue(response.isPresent());
        assertNotNull(response.get());
        assertEquals(this.user, response.get());
    }

    @Test
    void whenFindUserByUidEqualsAndIdIsNot_isOK() {

        this.user.setId(1L);

        this.userRepository.save(this.user);
        Optional<User> response = this.userRepository.findUserByUidEqualsAndIdIsNot("1234567", 2L);

        assertTrue(response.isEmpty());
    }

    @Test
    void whenFindUserByEmailEqualsAndIdIsNot_isOK() {

        User u = new User();
        u.setUid("1234567");
        u.setUserName("Reem");
        u.setFirstName("Reem");
        u.setLastName("Gharib");
        u.setEmail("brt.gharib@ttt.com");
        u.setActive(true);
        u.setRole(Role.ADMIN);
        u.setId(3L);

        this.userRepository.save(u);
        Optional<User> response = this.userRepository.findUserByEmailEqualsAndIdIsNot("brt.gharib@ttt.com", 3L);

        assertFalse(response.isEmpty());
    }
}
