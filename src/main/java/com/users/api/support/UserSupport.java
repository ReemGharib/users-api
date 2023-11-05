package com.users.api.support;

import com.users.api.dto.Name;
import com.users.api.dto.UserDetails;
import com.users.api.model.User;
import com.users.api.model.enums.Role;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * @author Reem Gharib
 */
@Component
public class UserSupport {

    public User populateUser(UserDetails userDetails) {

        User user = new User();
        user.setUid(userDetails.getUid());
        user.setUserName(userDetails.getUserName());
        user.setFirstName(userDetails.getName().getFirstName());
        user.setLastName(userDetails.getName().getLastName());
        user.setEmail(userDetails.getEmail());
        user.setActive(userDetails.getActive());
        user.setRole(Role.fromValue(userDetails.getRole()));

        return user;
    }

    /**
     * Populate User Response
     *
     * @param user the user
     * @return UserResponse
     */
    public UserDetails populateUserResponse(User user) {
        return this.populateUserDetails(user);
    }

    /**
     * Populate user details
     *
     * @param userEntity the user entity
     * @return UserDetails
     */
    public UserDetails populateUserDetails(User userEntity) {
        UserDetails userDetails = new UserDetails();

        userDetails.setUid(userEntity.getUid());

        Name name = Name.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();

        userDetails.setName(name);

        userDetails.setUserName(userEntity.getUserName());
        userDetails.setEmail(userEntity.getEmail());
        userDetails.setRole(userEntity.getRole().getName());
        userDetails.setActive(userEntity.getActive());

        return userDetails;
    }


    /**
     * Populate resources of UserListResponse
     *
     * @param users the users
     * @return list of resources
     */
    public List<UserDetails> populateResources(List<User> users) {

        List<UserDetails> resources = new ArrayList<>();

        users.forEach(user -> resources.add(UserDetails.builder()
                .uid(user.getUid())
                .name(Name.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName()).build())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.getActive())
                .build())
        );

        return resources;
    }

    /**
     * Convert local date time to offset date time
     *
     * @param localDateTime the local date time
     * @return OffsetDateTime
     */
    private OffsetDateTime convertLocalDateTimeToOffsetDateTime(LocalDateTime localDateTime) {

        return localDateTime != null
                ? localDateTime.atOffset(ZoneId.systemDefault().getRules().getOffset(localDateTime))
                : null;
    }


    /**
     * Validate user
     *
     * @param userDto the user details
     */
    public void validateUser(UserDetails userDto) {

        Validate.notBlank(userDto.getUid(), "User doesn't have uid");

        // Validate email
        if (!this.validateEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is not valid!");
        }

        Validate.notBlank(userDto.getUserName(), format("User of ccgId [%s] doesn't have username", userDto.getUid()));
        Validate.notBlank(userDto.getName().getFirstName(), format("User of ccgId [%s] doesn't have firstName", userDto.getUid()));
        Validate.notBlank(userDto.getName().getLastName(), format("User of ccgId [%s] doesn't have lastName", userDto.getUid()));
        Validate.notBlank(userDto.getRole(), format("User of ccgId [%s] doesn't have role", userDto.getUid()));
    }

    /**
     * Validate email
     *
     * @param emailAddress the email
     * @return boolean
     */
    public boolean validateEmail(String emailAddress) {

        Validate.notBlank(emailAddress, format("User of ccgId [%s] doesn't have email", emailAddress));

        String regexPattern = "^[A-Za-z0-9+_.-]+@(.+\\.com)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
