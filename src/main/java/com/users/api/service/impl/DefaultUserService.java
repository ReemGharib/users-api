package com.users.api.service.impl;

import com.users.api.dto.UserDetails;
import com.users.api.dto.UserDto;
import com.users.api.dto.UserListResponse;
import com.users.api.exception.*;
import com.users.api.model.User;
import com.users.api.model.enums.Role;
import com.users.api.repository.UserRepository;
import com.users.api.service.UserService;
import com.users.api.support.CustomPageable;
import com.users.api.support.UserSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Reem Gharib
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserSupport userSupport;

    @Override
    public UserDetails persistUser(UserDetails userDetails) {

        this.userSupport.validateUser(userDetails);

        this.userRepository.checkUserExistsByUid(userDetails.getUid());
        this.userRepository.checkUserExistsByEmail(userDetails.getEmail());

        if (Role.checkRoleExists(userDetails.getRole()) == null) {
            throw new InvalidRoleException(format("Invalid role: %s", userDetails.getRole()));
        }

        User user = this.userSupport.populateUser(userDetails);
        user.setCreatedDate(LocalDateTime.now());

        try {
            return this.userSupport.populateUserDetails(this.userRepository.save(user));
        } catch (Exception e) {
            log.error("Error ", e);
            throw new PersistResourceException("Unable to persist user info of ccgId = " + userDetails.getUid());
        }
    }

    @Override
    public UserDetails getUserByUid(String uid) {

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException(format("User Not found with ccgId: [%s]", uid)));

        return this.userSupport.populateUserDetails(user);
    }

    @Override
    public UserDetails getUserByEmail(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(format("User Not found with email: [%s]", email)));

        return this.userSupport.populateUserDetails(user);
    }

    @Override
    public UserDetails updateUser(String uid, UserDetails userDetails) {

        this.userSupport.validateUser(userDetails);

        User existingUser = this.userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException(format("User Not found with ccgId: [%s]", uid)));

        this.userRepository.findUserByUidEqualsAndIdIsNot(userDetails.getUid(), existingUser.getId())
                .ifPresent(userEntity -> {
                    throw new UserAlreadyExistsException(format("User with same ccgId [%s] already exists", userDetails.getUid()));
                });

        this.userRepository.findUserByEmailEqualsAndIdIsNot(userDetails.getEmail(), existingUser.getId())
                .ifPresent(userEntity -> {
                    throw new UserAlreadyExistsException(format("User with same email [%s] already exists", userDetails.getEmail()));
                });

        if (Role.checkRoleExists(userDetails.getRole()) == null) {
            throw new InvalidRoleException(format("Invalid role: %s", userDetails.getRole()));
        }

        User user = this.userSupport.populateUser(userDetails);

        user.setId(existingUser.getId());
        user.setCreatedDate(existingUser.getCreatedDate());
        user.setLastModifiedDate(LocalDateTime.now());

        try {
            return this.userSupport.populateUserDetails(userRepository.save(user));
        } catch (Exception e) {
            log.info("Error = ", e);
            throw new PersistResourceException(String.format("Error while updating user with ccgId = [%s] ", userDetails.getUid()));
        }
    }

    @Override
    public UserListResponse getAllUserDetails(UserDto userDto) {

        // static values..
        int offset = 0;
        int limit = 10;

        Sort sort = Sort.by(Collections.singletonList(Sort.Order.asc("uid")));
        Pageable pageable = new CustomPageable(limit, offset, sort);

        List<User> users = this.userRepository.findAll(
                this.userRepository.filterUsers(userDto), pageable).getContent();

        if (CollectionUtils.isEmpty(users)) {
            long count = this.userRepository.count(this.userRepository.filterUsers(new UserDto()));
            throw new EmptySearchResultException((int) count, "_ERR", "No result found");
        }

        return UserListResponse.builder()
                .resources(this.userSupport.populateResources(users))
                .build();
    }
}