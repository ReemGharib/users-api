package com.users.api.service;

import com.users.api.dto.UserDetails;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import jakarta.validation.Valid;


/**
 * The interface User Service
 *
 * @author reem Gharib
 */
public interface UserService {

    /**
     * Get all users details
     *
     * @param userDto the user dto
     * @return UserListResponse
     */
    UserListResponse getAllUserDetails(UserDto userDto);

    /**
     * Persist a new user
     *
     * @param userDetails the user details to save
     * @return the created user
     */
    UserDetails persistUser(UserDetails userDetails);

    /**
     * Update user associated with uid
     *
     * @param uid         update user
     * @param userDetails the new user
     * @return the Updated user
     */
    UserDetails updateUser(String uid, @Valid UserDetails userDetails);

    /**
     * Get user by uid
     *
     * @param uid user ccgId
     * @return UserResponse
     */
    UserDetails getUserByUid(String uid);

    /**
     * Get user by email
     *
     * @param email user mail
     * @return UserResponse
     */
    UserDetails getUserByEmail(String email);
}
