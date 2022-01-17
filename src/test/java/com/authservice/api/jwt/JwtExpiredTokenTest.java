package com.authservice.api.jwt;

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
public class JwtExpiredTokenTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void ExpiredTokenAuthentication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"user\",\"email\":\"user@mail.com\",\"password\":\"test123\"}"))
                .andReturn();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"usernameOrEmail\":\"user\",\"password\":\"test123\"}")).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String token = response.replaceFirst(".*Token\":\"(.*?)\",\"token.*", "$1");

        mvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is4xxClientError());
    }
}
