package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    UserDto userDtoToolMan;
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
        userDtoToolMan= UserDto.builder()
                .userName("toolMan")
                .age(20)
                .email("tool@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(100)
                .build();
        userRepository.save(userDto);
        userRepository.save(userDtoToolMan);
        rsEventDto = RsEventDto.builder().keyWord("娱乐").eventName("乘风破浪的姐姐更新了").user(userDto).voteNum(100).build();
        rsEventRepository.save(rsEventDto);
        voteDto = VoteDto.builder().voteNum(3).voteTime(localDateTime).user(userDto).rsEvent(rsEventDto).build();
    }

    void initDataForgetRecordByTime(){
        for (int i = 1; i < 5; i++){
            LocalDateTime timeStamp = LocalDateTime.of(2020, 8, i, 20, 20, 17);

            VoteDto voteDtoToBeAdd = VoteDto.builder()
                    .voteNum(i)
                    .voteTime(timeStamp)
                    .user(userDtoToolMan)
                    .rsEvent(rsEventDto).build();
            voteRepository.save(voteDtoToBeAdd);
        }
    }

    @Test
    void should_get_vote_record() throws Exception {
        voteRepository.save(voteDto);

        mockMvc.perform(get("/voteRecord?rsEventId="+rsEventDto.getId()+"&userId="+userDto.getId()))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$.[0].rsEventId",is(rsEventDto.getId())))
                .andExpect(jsonPath("$.[0].voteNum",is(3)))
                .andExpect(jsonPath("$.[0].userId",is(userDto.getId())));
    }

    @Test
    void should_get_vote_record_by_time() throws Exception {
        //prepare vote record
        initDataForgetRecordByTime();
       //pass time but not timeStamp
        String startTime = "2020-08-01 00:00:00";
        String endTime = "2020-08-04 23:00:00";
        mockMvc.perform(get("/voteRecordByTime")
                .param("startTime",startTime)
                .param("endTime",endTime))
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$.[0].rsEventId",is(rsEventDto.getId())))
                .andExpect(jsonPath("$.[0].voteNum",is(1)))
                .andExpect(jsonPath("$.[0].userId",is(userDtoToolMan.getId())))
                .andExpect(jsonPath("$.[0].voteTime",is(LocalDateTime.of(2020, 8, 1, 20, 20, 17).toString())));
    }
    @Test
    void should_throw_error_when_get_vote_record_by_time_given_time_not_reach_yet() throws Exception {
        //prepare vote record
        initDataForgetRecordByTime();
        //pass time but not timeStamp
        String startTime = "2020-08-01 00:00:00";
        String endTime = "2020-09-04 23:00:00";
        mockMvc.perform(get("/voteRecordByTime")
                .param("startTime",startTime)
                .param("endTime",endTime))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("input time invalid:please input endDate before now!")));

    }

    @Test
    void should_throw_error_when_get_vote_record_by_time_given_time_is_not_date_format() throws Exception {
        //prepare vote record
        initDataForgetRecordByTime();
        //pass time but not timeStamp
        String startTime = "2020080100:00:00";
        String endTime = "2020-09-04 23:00:00";
        mockMvc.perform(get("/voteRecordByTime")
                .param("startTime",startTime)
                .param("endTime",endTime))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("input time invalid:the format is not a date format")));

    }

    @AfterEach
    void tearDown() {

    }
}
