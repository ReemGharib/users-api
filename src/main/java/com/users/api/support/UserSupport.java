package com.users.api.support;

import com.users.api.dto.Name;
import com.users.api.dto.Resource;
import com.users.api.dto.UserDetails;
import com.users.api.dto.UserResponse;
import com.users.api.model.User;
import com.users.api.model.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
    public UserResponse populateUserResponse(User user) {

        UserResponse userResponse = new UserResponse();
        UserDetails userDetails = this.populateUserDetails(user);

        userResponse.setUser(userDetails);

        return userResponse;
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
    public List<Resource> populateResources(List<User> users) {

        List<Resource> resources = new ArrayList<>();

        users.forEach(user -> resources.add(Resource.builder()
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
}
