package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
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
import java.util.List;

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
    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setUp(){
        UserController.userList = new ArrayList<>();
        userRepository.deleteAll();
    }
    @Test
    void should_register_user() throws Exception {
        //String jsonUser = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxia\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        UserDto userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserDto> userList = userRepository.findAll();
        assertEquals(1,userList.size());
        assertEquals("xiaoming",userList.get(0).getUserName());
        assertEquals(1,userList.get(0).getUserId());
        assertEquals("a@b.com",userList.get(0).getEmail());
    }

    @Test
    void should_return_user_list() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("xiaoming")))
                .andExpect(jsonPath("$.userId", is(1)));
    }


    @Test
    void should_delete_user_by_id() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/user/delete")
                .param("userId", "1"))
                .andExpect(status().isCreated());

        List<UserDto> userList = userRepository.findAll();
        assertEquals(0,userList.size());
    }
    @Test
    void user_name_length_should_less_than_8() throws Exception {
        User user = new User("abc8765432345","male",20,"abc@abc.com","18978654567",10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
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
