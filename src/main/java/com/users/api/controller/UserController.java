package com.users.api.controller;

import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import com.users.api.model.enums.Role;
import com.users.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Reem Gharib
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<UserListResponse> getAllUsers(String range, String role, String uid, String firstName,
                                                        String lastName, String email, Boolean active) {

        UserDto userDto = UserDto.builder()
                .uid(uid)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role != null ? Role.fromValue(role).getName() : null)
                .isActive(active)
                .build();

        return ResponseEntity.ok(
                this.userService.getAllUserDetails(userDto));
    }
}
