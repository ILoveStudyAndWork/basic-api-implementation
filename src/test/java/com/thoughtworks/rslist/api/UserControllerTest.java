package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        UserController.userList = new ArrayList<>();
    }
    @Test
    void should_register_user() throws Exception {
        //String jsonUser = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxia\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        User user = new User("abc","male",20,"abc@abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index","0"))
                .andExpect(status().isCreated());
    }

    @Test
    void user_name_length_should_less_than_8() throws Exception {
        User user = new User("abc8765432345","male",20,"abc@abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void age_should_between_18_and_100() throws Exception {
        User user = new User("abc","male",17,"abc@abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void email_should_suit_format() throws Exception {
        User user = new User("abc","male",20,"abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_suit_format() throws Exception {
        User user = new User("abc","male",20,"abc@abc.com","189786567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void get_user_list() throws Exception {
        User user = new User("abc","male",20,"abc@abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("index","0"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].user_name",is("abc")))
                .andExpect(jsonPath("$[0].user_age",is(20)))
                .andExpect(jsonPath("$[0].user_gender",is("male")))
                .andExpect(jsonPath("$[0].user_email",is("abc@abc.com")))
                .andExpect(jsonPath("$[0].user_phone",is("18978654567")));


    }


}
