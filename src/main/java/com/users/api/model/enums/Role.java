package com.users.api.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin", "Administrator"),
    PCM("pcm", "project cargo managers"),
    OPS("ops", "operator");

    private final String name;
    private final String description;

    /**
     * Attempts to parse a string into a WeightUnit enum based off of the enum's value,
     * or throws an EnumConstantNotPresentException if no mapping is found
     *
     * @param value the value
     * @return the role
     */
    public static Role fromValue(String value) {

        return Arrays.stream(Role.values())
                .filter(role -> role.getName().equals(value.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(Role.class, value));
    }

    public static Role checkRoleExists(String value) {
        for (Role role : Role.values()) {
            if (role.getName().equals(value.toLowerCase())) {
                return role;
            }
        }
        return null;
    }
}
