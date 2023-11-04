package com.users.api.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserResponse {

    private UserDetails user;

    private Meta meta;
}
