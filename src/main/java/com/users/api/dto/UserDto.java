package com.users.api.dto;


import lombok.*;

/**
 * @author Reem Gharib
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDto {

    private String uid;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String role;
    private Boolean isActive;
}