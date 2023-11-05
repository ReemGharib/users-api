package com.users.api.controller;

import com.users.api.dto.UserDetails;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import com.users.api.model.enums.Role;
import com.users.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Reem Gharib
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(@Valid @RequestParam(value = "uid", required = false) String uid,
                                                        @Valid @RequestParam(value = "firstName", required = false) String firstName,
                                                        @Valid @RequestParam(value = "lastName", required = false) String lastName,
                                                        @Valid @RequestParam(value = "email", required = false) String email,
                                                        @Valid @RequestParam(value = "role", required = false) String role,
                                                        @Valid @RequestParam(value = "active", required = false) Boolean active) {

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

    @PostMapping
    public ResponseEntity<UserDetails> createUser(@Valid @RequestBody UserDetails userDetails) {

        return ResponseEntity.ok(this.userService.persistUser(userDetails));
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDetails> getUserByUid(@PathVariable("uid") String uid) {

        return ResponseEntity.ok(userService.getUserByUid(uid));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDetails> getUserByEmail(@PathVariable("email") String email) {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/{uid}")
    public ResponseEntity<UserDetails> updateUser(@PathVariable("uid") String uid,
                                                  @Valid @RequestBody UserDetails userDetails) {

        return ResponseEntity.ok(userService.updateUser(uid, userDetails));
    }
}