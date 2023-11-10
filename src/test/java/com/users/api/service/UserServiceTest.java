package com.users.api.service;

import com.users.api.dto.Name;
import com.users.api.dto.UserDetails;
import com.users.api.exception.InvalidRoleException;
import com.users.api.exception.UserAlreadyExistsException;
import com.users.api.exception.UserNotFoundException;
import com.users.api.model.User;
import com.users.api.model.enums.Role;
import com.users.api.repository.UserRepository;
import com.users.api.service.impl.DefaultUserService;
import com.users.api.support.UserSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Reem Gharib
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private DefaultUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSupport userSupport;

    private User user;
    private UserDetails userDetails;
    private MockedStatic<Role> roleStaticMock;

    @BeforeEach
    void setUp() {

        this.user = new User();
        user.setUid("1234567");
        user.setUserName("Reem");
        user.setFirstName("Reem");
        user.setLastName("Gharib");
        user.setEmail("brt.rgharib@cmacgm.com");
        user.setRole(Role.ADMIN);

        this.userDetails = UserDetails.builder()
                .uid("1234567")
                .userName("Reem")
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gharib")
                        .build())
                .role(Role.ADMIN.getName())
                .email("brt.rgharib@cma-cgm.com")
                .active(true).build();
    }

    @AfterEach
    void cleanup() {
        // Close the MockedStatic if it was created during the test
        if (roleStaticMock != null) {
            roleStaticMock.close();
        }
    }

    @Test
    void whenPersist_isOk() {

        this.roleStaticMock = mockStatic(Role.class);

        doNothing().when(this.userRepository).checkUserExistsByUid("1234567");
        doNothing().when(this.userRepository).checkUserExistsByEmail("brt.rgharib@cma-cgm.com");

        this.roleStaticMock.when(() -> Role.checkRoleExists("admin"))
                .thenReturn(Role.ADMIN);

        when(this.userSupport.populateUser(userDetails)).thenReturn(this.user);
        when(this.userRepository.save(user)).thenReturn(this.user);
        when(this.userSupport.populateUserDetails(user)).thenReturn(this.userDetails);

        UserDetails response = this.userService.persistUser(this.userDetails);
        assertNotNull(response);

        verify(this.userRepository, times(1)).checkUserExistsByUid("1234567");
        verify(this.userRepository, times(1)).checkUserExistsByEmail("brt.rgharib@cma-cgm.com");
        verify(this.userSupport, times(1)).populateUser(userDetails);
        verify(this.userRepository, times(1)).save(user);
        verify(this.userSupport, times(1)).populateUserDetails(user);
    }

    @Test
    void whenPersist_wrongUid_throwUserAlreadyExistsException() {

        doThrow(UserAlreadyExistsException.class).when(this.userRepository).checkUserExistsByUid(anyString());

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.persistUser(this.userDetails));

        verify(this.userRepository, times(1)).checkUserExistsByUid(anyString());
        verify(this.userRepository, times(0)).checkUserExistsByEmail(anyString());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(this.userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenPersist_wrongEmail_throwUserAlreadyExistsException() {

        doNothing().when(this.userRepository).checkUserExistsByUid("1234567");
        doThrow(UserAlreadyExistsException.class).when(this.userRepository).checkUserExistsByEmail(userDetails.getEmail());

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.persistUser(this.userDetails));

        verify(this.userRepository, times(1)).checkUserExistsByUid("1234567");
        verify(this.userRepository, times(1)).checkUserExistsByEmail(userDetails.getEmail());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(this.userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenPersist_throwInvalidRoleException() {

        this.userDetails.setRole("ddd");

        this.roleStaticMock = mockStatic(Role.class);

        doNothing().when(this.userRepository).checkUserExistsByUid("1234567");
        doNothing().when(this.userRepository).checkUserExistsByEmail("brt.rgharib@cma-cgm.com");

        this.roleStaticMock.when(() -> Role.checkRoleExists("ddd"))
                .thenReturn(null);

        InvalidRoleException exception = assertThrows(InvalidRoleException.class, () -> this.userService.persistUser(this.userDetails));
        assertEquals("Invalid role: ddd", exception.getMessage());

        verify(this.userRepository, times(1)).checkUserExistsByUid("1234567");
        verify(this.userRepository, times(1)).checkUserExistsByEmail("brt.rgharib@cma-cgm.com");
        verify(this.userSupport, times(0)).populateUser(any());
        verify(this.userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenGetUserByUid_isOK() {

        when(userRepository.findByUid(anyString())).thenReturn(Optional.of(this.user));
        when(this.userSupport.populateUserDetails(user)).thenReturn(this.userDetails);

        UserDetails response = this.userService.getUserByUid("1234567");

        assertNotNull(response);
        assertEquals(this.userDetails, response);
        verify(this.userRepository, times(1)).findByUid("1234567");
    }

    @Test
    void whenGetUserByUid_throwUserNotFoundException() {

        String uid = "nonExistentUid";

        when(userRepository.findByUid(uid)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByUid(uid));

        assertEquals("User Not found with ccgId: [nonExistentUid]", exception.getMessage());
        verify(this.userRepository, times(1)).findByUid(uid);
        verify(this.userSupport, times(0)).populateUserDetails(any());

    }

    @Test
    void whenGetUserByEmail_isOK() {

        String email = "brt.rgharib@cma-cgm.com";

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(this.user));

        when(this.userSupport.populateUserDetails(user)).thenReturn(this.userDetails);

        UserDetails response = this.userService.getUserByEmail(email);

        assertNotNull(response);
        assertEquals(this.userDetails, response);
        verify(this.userRepository, times(1)).findByEmailIgnoreCase(email);
    }

    @Test
    void whenGetUserByEmail_throwUserNotFoundException() {

        String email = "nonExistentEmail";

        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));

        assertEquals("User Not found with email: [nonExistentEmail]", exception.getMessage());
        verify(this.userRepository, times(1)).findByEmailIgnoreCase(email);
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenUpdateUser_isOk() {

        String uid = "123";
        String email = "newEmail";
        this.user.setId(1L);

        UserDetails updateUserDetails = UserDetails.builder()
                .uid(uid)
                .userName("Reem")
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gharib").build())
                .role(Role.ADMIN.getName())
                .email(email)
                .active(true)
                .build();

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUid(uid);
        updatedUser.setUserName("Reem");
        updatedUser.setFirstName("Reem");
        updatedUser.setLastName("Gharib");
        updatedUser.setEmail(email);
        updatedUser.setRole(Role.ADMIN);


        when(this.userRepository.findByUid(user.getUid())).thenReturn(Optional.of(this.user));
        when(this.userRepository.findUserByUidEqualsAndIdIsNot(uid, user.getId())).thenReturn(Optional.empty());
        when(this.userRepository.findUserByEmailEqualsAndIdIsNot(email, user.getId())).thenReturn(Optional.empty());
        when(this.userSupport.populateUser(updateUserDetails)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(this.userSupport.populateUserDetails(updatedUser)).thenReturn(updateUserDetails);

        this.roleStaticMock = mockStatic(Role.class);
        this.roleStaticMock.when(() -> Role.checkRoleExists(anyString()))
                .thenReturn(Role.ADMIN);

        UserDetails response = this.userService.updateUser(user.getUid(), updateUserDetails);

        assertNotNull(response);

        verify(this.userRepository, times(1)).findByUid(user.getUid());
        verify(this.userRepository, times(1)).findUserByUidEqualsAndIdIsNot(uid, user.getId());
        verify(this.userRepository, times(1)).findUserByEmailEqualsAndIdIsNot(email, user.getId());
        verify(this.userSupport, times(1)).populateUser(updateUserDetails);
        verify(userRepository, times(1)).save(updatedUser);
        verify(this.userSupport, times(1)).populateUserDetails(updatedUser);
    }

    @Test
    void whenUpdateUser_throwUserNotFoundException() {

        when(this.userRepository.findByUid(user.getUid())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> this.userService.updateUser(user.getUid(), any()));
        assertEquals("User Not found with ccgId: [1234567]", exception.getMessage());

        verify(this.userRepository, times(1)).findByUid(user.getUid());
        verify(this.userRepository, times(0)).findUserByUidEqualsAndIdIsNot(anyString(), anyLong());
        verify(this.userRepository, times(0)).findUserByEmailEqualsAndIdIsNot(anyString(), anyLong());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenUpdateUser_uid_throwUserAlreadyExistsException() {

        String uid = "123";
        String email = "newEmail";
        this.user.setId(1L);

        UserDetails updateUserDetails =UserDetails.builder()
                .uid(uid)
                .userName("Reem")
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gharib").build())
                .role(Role.ADMIN.getName())
                .email(email)
                .active(true).build();

        User user1 = new User();
        user1.setUid(user1.getUid());
        user1.setId(2L);

        when(this.userRepository.findByUid(user.getUid())).thenReturn(Optional.of(this.user));
        when(this.userRepository.findUserByUidEqualsAndIdIsNot(uid, user.getId())).thenReturn(Optional.of(user1));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> this.userService.updateUser(user.getUid(), updateUserDetails));
        assertEquals("User with same ccgId [123] already exists", exception.getMessage());

        verify(this.userRepository, times(1)).findByUid(user.getUid());
        verify(this.userRepository, times(1)).findUserByUidEqualsAndIdIsNot(uid, user.getId());
        verify(this.userRepository, times(0)).findUserByEmailEqualsAndIdIsNot(anyString(), anyLong());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenUpdateUser_email_throwUserAlreadyExistsException() {

        String uid = "123";
        String email = "newEmail";
        this.user.setId(1L);

        UserDetails updateUserDetails = UserDetails.builder()
                .uid(uid)
                .userName("Reem")
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gharib").build())
                .role(Role.ADMIN.getName())
                .email(email)
                .active(true).build();

        User user1 = new User();
        user1.setEmail(user1.getEmail());
        user1.setId(2L);

        when(this.userRepository.findByUid(user.getUid())).thenReturn(Optional.of(this.user));
        when(this.userRepository.findUserByUidEqualsAndIdIsNot(uid, user.getId())).thenReturn(Optional.empty());
        when(this.userRepository.findUserByEmailEqualsAndIdIsNot(email, user.getId())).thenReturn(Optional.of(user1));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> this.userService.updateUser(user.getUid(), updateUserDetails));
        assertEquals("User with same email [newEmail] already exists", exception.getMessage());

        verify(this.userRepository, times(1)).findByUid(user.getUid());
        verify(this.userRepository, times(1)).findUserByUidEqualsAndIdIsNot(uid, user.getId());
        verify(this.userRepository, times(1)).findUserByEmailEqualsAndIdIsNot(email, user.getId());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }

    @Test
    void whenUpdateUser_throwInvalidRoleException() {

        this.roleStaticMock = mockStatic(Role.class);

        String uid = "123";
        String email = "newEmail";
        this.user.setId(1L);

        UserDetails updateUserDetails = UserDetails.builder()
                .uid(uid)
                .userName("Reem")
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gharib").build())
                .role("ddd")
                .email(email)
                .active(true).build();


        when(this.userRepository.findByUid(user.getUid())).thenReturn(Optional.of(this.user));
        when(this.userRepository.findUserByUidEqualsAndIdIsNot(uid, user.getId())).thenReturn(Optional.empty());
        when(this.userRepository.findUserByEmailEqualsAndIdIsNot(email, user.getId())).thenReturn(Optional.empty());
        this.roleStaticMock.when(() -> Role.checkRoleExists(updateUserDetails.getRole()))
                .thenReturn(null);

        InvalidRoleException exception = assertThrows(InvalidRoleException.class,
                () -> this.userService.updateUser(user.getUid(), updateUserDetails));
        assertEquals("Invalid role: ddd", exception.getMessage());

        verify(this.userRepository, times(1)).findByUid(user.getUid());
        verify(this.userRepository, times(1)).findUserByUidEqualsAndIdIsNot(uid, user.getId());
        verify(this.userRepository, times(1)).findUserByEmailEqualsAndIdIsNot(email, user.getId());
        verify(this.userSupport, times(0)).populateUser(any());
        verify(userRepository, times(0)).save(any());
        verify(this.userSupport, times(0)).populateUserDetails(any());
    }
}
