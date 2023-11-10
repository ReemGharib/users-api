package com.users.api.controller;

import com.users.api.dto.Name;
import com.users.api.dto.UserDetails;
import com.users.api.dto.UserListResponse;
import com.users.api.model.enums.Role;
import com.users.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @MockBean
    private UserListResponse userListResponse;

    @MockBean
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {

        mockMvc = standaloneSetup(userController)
                .setControllerAdvice(new Throwable())
                .build();


        this.userDetails = UserDetails.builder()
                .uid("123")
                .userName("Reem")
                .email("reemgharib97@outlook.com")
                .role(Role.ADMIN.name())
                .name(Name.builder()
                        .firstName("Reem")
                        .lastName("Gh")
                        .build())
                .active(true)
                .build();

        this.userListResponse.setResources(Collections.singletonList(userDetails));
    }

    @Test
    void whenGetAllUsers_isOK() throws Exception {

        when(userService.getAllUserDetails(any())).thenReturn(this.userListResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    void whenGetUserByUid_isOK() throws Exception {

        when(userService.getUserByUid("123")).thenReturn(this.userDetails);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/{uid}", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetUserByEmail_isOK() throws Exception {

        when(userService.getUserByEmail("reemgharib97@outlook.com")).thenReturn(this.userDetails);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/emails/{email}", "reemgharib97@outlook.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
