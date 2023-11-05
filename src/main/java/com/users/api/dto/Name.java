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
public class Name {

    private String firstName;

    private String lastName;
}
