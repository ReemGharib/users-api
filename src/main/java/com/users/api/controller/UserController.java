package com.users.api.controller;

import com.users.api.dto.ErrorResponse;
import com.users.api.dto.UserDetails;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import com.users.api.model.enums.Role;
import com.users.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "users returned successfully"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @Operation(summary = "Get All Users")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User object created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Create user object")
    @PostMapping
    public ResponseEntity<UserDetails> createUser(@Valid @RequestBody UserDetails userDetails) {

        return ResponseEntity.ok(this.userService.persistUser(userDetails));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user returned successfully"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Get user by uid")
    @GetMapping("/{uid}")
            public ResponseEntity<UserDetails> getUserByUid(@PathVariable("uid") String uid) {

        return ResponseEntity.ok(userService.getUserByUid(uid));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user returned successfully"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(summary = "Get user by email")
    @GetMapping("/emails/{email}")
    public ResponseEntity<UserDetails> getUserByEmail(@PathVariable("email") String email) {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User object updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
                    })
    })
    @Operation(summary = "Update object by uid")
    @PutMapping("/{uid}")
    public ResponseEntity<UserDetails> updateUser(@PathVariable("uid") String uid,
                                                  @Valid @RequestBody UserDetails userDetails) {

        return ResponseEntity.ok(userService.updateUser(uid, userDetails));
    }
}