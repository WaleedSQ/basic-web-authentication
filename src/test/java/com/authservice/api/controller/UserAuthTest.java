package com.authservice.api.controller;

import com.authservice.AuthApplication;
import com.authservice.api.jwt.JwtTokenProvider;
import com.authservice.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    //@MockBean
    //private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void main() {
        AuthApplication.main(new String[] {});
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        Mockito.doReturn(false).when(userRepository).existsByUsername("user");
        Mockito.doReturn(false).when(userRepository).existsByEmail("user@mail.com");

        MvcResult mvcResult = mvc
                 .perform(MockMvcRequestBuilders.post("/api/signup")
                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                         .content("{\"username\":\"user\",\"email\":\"user@mail.com\",\"password\":\"test123\"}"))
                 .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("User registered successfully", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername() throws Exception {
        Mockito.doReturn(true).when(userRepository).existsByUsername("user");
        Mockito.doReturn(false).when(userRepository).existsByEmail("user@mail.com");

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"user\",\"email\":\"user@mail.com\",\"password\":\"test123\"}"))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
        assertEquals("Username is already taken!", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldNotRegisterUserWithExistingEmail() throws Exception {
        Mockito.doReturn(false).when(userRepository).existsByUsername("user");
        Mockito.doReturn(true).when(userRepository).existsByEmail("user@mail.com");

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"user\",\"email\":\"user@mail.com\",\"password\":\"test123\"}"))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
        assertEquals("Email is already taken!", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldLoginWithUsername() throws Exception {

        Mockito.doReturn(authentication).when(authenticationManager).authenticate(any());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"usernameOrEmail\":\"user\",\"password\":\"password\"}"))
                .andReturn();
    }

}
