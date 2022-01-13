package com.authservice.api.controller;

import com.authservice.AuthApplication;
import com.authservice.api.AuthApplicationTests;
import com.authservice.api.dto.SignUpDto;
import com.authservice.api.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void main() {
        AuthApplication.main(new String[] {});
    }

    @Test
    public void createUser() throws Exception {
        String uri = "/api/signup";
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername("user");
        signUpDto.setEmail("user@mail.com");
        signUpDto.setPassword("test123");

        String inputJson = AuthApplicationTests.mapToJson(signUpDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "User registered successfully");
    }

    @Test
    public void createExistingUsername() throws Exception {
        String uri = "/api/signup";

        SignUpDto signUpDto1 = new SignUpDto();
        signUpDto1.setUsername("test");
        signUpDto1.setEmail("test@mail.com");
        signUpDto1.setPassword("test123");

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setUsername("test");
        signUpDto2.setEmail("test2@mail.com");
        signUpDto2.setPassword("test123");

        String inputJson1 = AuthApplicationTests.mapToJson(signUpDto1);
        String inputJson2 = AuthApplicationTests.mapToJson(signUpDto2);
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson1)).andReturn();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson2)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Username is already taken!");
    }

    @Test
    public void createExistingEmail() throws Exception {
        String uri = "/api/signup";

        SignUpDto signUpDto1 = new SignUpDto();
        signUpDto1.setUsername("test");
        signUpDto1.setEmail("test@mail.com");
        signUpDto1.setPassword("test123");

        SignUpDto signUpDto2 = new SignUpDto();
        signUpDto2.setUsername("test2");
        signUpDto2.setEmail("test@mail.com");
        signUpDto2.setPassword("test123");

        String inputJson1 = AuthApplicationTests.mapToJson(signUpDto1);
        String inputJson2 = AuthApplicationTests.mapToJson(signUpDto2);
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson1)).andReturn();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson2)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Email is already taken!");
    }

    @Test
    public void loginWithUsername() throws Exception {
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
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWithInvalidUser() throws Exception {

        String uri = "/api/login";
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("test3");
        loginDto.setPassword("test1234");

        String inputJson = AuthApplicationTests.mapToJson(loginDto);
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void loginWithEmail() throws Exception {
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
        loginDto.setUsernameOrEmail("test@mail.com");
        loginDto.setPassword("test123");

        inputJson = AuthApplicationTests.mapToJson(loginDto);
        mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andExpect(status().isOk());
    }

    @Test
    public void existingUserTokenAuthentication() throws Exception {
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void invalidTokenAuthentication() throws Exception {
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

        String token = "invalidTokenString";

        mvc.perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void invalidTokenTypeAuthentication() throws Exception {
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
                        .header("Authorization", "Bearer " + token + "x"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void invalidBearerAuthentication() throws Exception {
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
                        .header("Authorization", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void noBearerAuthentication() throws Exception {
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
                        .header("Authorization", ""))
                .andExpect(status().is4xxClientError());
    }
}
