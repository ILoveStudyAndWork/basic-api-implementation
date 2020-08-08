package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MockMvc mockMvc;
    UserDto userDto;
    RsEventDto rsEventDto;
    VoteDto voteDto;
    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        localDateTime = LocalDateTime.now();
        userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();
        userRepository.save(userDto);
        rsEventDto = RsEventDto.builder().keyWord("娱乐").eventName("乘风破浪的姐姐更新了").user(userDto).voteNum(100).build();
        rsEventRepository.save(rsEventDto);
        voteDto = VoteDto.builder().voteNum(3).voteTime(localDateTime).user(userDto).rsEvent(rsEventDto).build();

    }


    @Test
    void should_get_vote_record() throws Exception {
        voteRepository.save(voteDto);
        mockMvc.perform(get("/voteRecord?rsEventId=1&userId=1"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].rsEventId",is(rsEventDto.getId())))
                .andExpect(jsonPath("$.[0].voteNum",is(3)))
                .andExpect(jsonPath("$.[0].userId",is(userDto.getId())));
    }

    @AfterEach
    void tearDown() {

    }
}
