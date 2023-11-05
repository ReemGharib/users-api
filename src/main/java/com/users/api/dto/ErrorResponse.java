package com.users.api.dto;


import lombok.*;

/**
 * @author Reem Gharib
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorResponse {

    private String reason;

    private String code;

    private String description;

}
