package com.users.api.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class UserListResponse {

    @Valid
    private List<@Valid Resource> resources;

    private Integer itemsPerPage;

    private Integer startIndex;

    private Long totalResults;
}
