package com.authservice.api.service;

import com.authservice.api.model.User;
import com.authservice.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
public class UserDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Test
    void findUserByValidName() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@mail.com");
        user.setPassword("test123");

        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsernameOrEmail(user.getUsername(), user.getUsername());
        UserDetails expectedUser = customUserDetailsService.loadUserByUsername(user.getUsername());
        assertNotNull(expectedUser);
        assertEquals(expectedUser.getUsername(), user.getUsername());
    }

    @Test
    void findUserByWrongName() {
        User user = new User();
        user.setUsername("test1");

        Mockito.doThrow(new UsernameNotFoundException(user.getUsername())).when(userRepository).findByUsernameOrEmail(user.getUsername(), user.getEmail());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername( user.getUsername());
        }, "User not found with username or email:" + user.getUsername());
    }

}
