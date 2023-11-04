package com.users.api.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDetails {

    private String uid;

    private Name name;

    private String userName;

    private String email;

    private String role;

    private Boolean active = false;

}
