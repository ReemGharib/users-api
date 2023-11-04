package com.users.api.service;

import com.users.api.dto.UserDetails;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import com.users.api.dto.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;


/**
 * @author reem Gharib
 */
public interface UserService {

    UserListResponse getAllUserDetails(UserDto userDto);

    UserResponse persistUser(UserDetails userDetails);

    UserResponse updateUser(String uid, @Valid UserDetails userDetails);

    /**
     * @param uid user ccgId
     * @returnUserResponse
     */
    UserResponse getUserByUid(String uid);

    /**
     * @param email user mail
     * @returnUserResponse
     */
    UserResponse getUserByEmail(String email);
}
