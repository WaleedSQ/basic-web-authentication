package com.authentication.api;

import com.authentication.api.dto.LoginDto;
import com.authentication.api.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource({ "/application-test.properties" })
public class UserAuthTestExpiredToken {

    @Autowired
    private MockMvc mvc;

    @Test
    public void ExpiredTokenAuthentication() throws Exception {
        String uri = "/api/signup";
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("test");
        signUpDto.setEmail("test@mail.com");
        signUpDto.setPassword("test123");

        String inputJson = AuthApplicationTests.mapToJson(signUpDto);
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        uri = "/api/login";
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("test");
        loginDto.setPassword("test123");

        inputJson = AuthApplicationTests.mapToJson(loginDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String token = response.replaceFirst(".*Token\":\"(.*?)\",\"token.*", "$1");

        mvc.perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }
}
