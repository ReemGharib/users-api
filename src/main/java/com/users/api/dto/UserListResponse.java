package com.users.api.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

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
public class UserListResponse {

    @Valid
    private List<@Valid UserDetails> resources;

    private Long totalResults;
}
