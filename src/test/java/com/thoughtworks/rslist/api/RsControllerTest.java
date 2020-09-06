package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.fonteddata.RsEventForModify;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.databind.MapperFeature.USE_ANNOTATIONS;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RsControllerTest {

    @Autowired
    MockMvc mockMvc ;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    UserDto userDto;
    UserDto userDtoNew;
    RsEventDto rsEventDto;
    RsEventDto rsEventDto1;
    RsEventDto rsEventDto2;
    RsEventDto rsEventDto3;

    @BeforeEach
    void setUp(){
        userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();
        userDtoNew = UserDto.builder()
                .userName("wanwan")
                .age(30)
                .email("wanwan@b.com")
                .gender("female")
                .phone("19876545676")
                .voteNum(10)
                .build();
        rsEventDto1 = RsEventDto.builder()
                .keyWord("no type")
                .eventName("The first rs event")
                .user(userDto)
                .voteNum(0).build();
        rsEventDto2 = RsEventDto.builder()
                .keyWord("no type")
                .eventName("The second rs event")
                .user(userDto)
                .voteNum(0).build();
        rsEventDto3 = RsEventDto.builder()
                .keyWord("no type")
                .eventName("The third rs event")
                .user(userDto)
                .voteNum(0).build();
        rsEventDto = RsEventDto.builder()
                .keyWord("体育")
                .eventName("北京广东半决赛")
                .user(userDto)
                .voteNum(100).build();
        userRepository.save(userDto);
        rsEventRepository.save(rsEventDto1);
        rsEventRepository.save(rsEventDto2);
        rsEventRepository.save(rsEventDto3);
    }

    @AfterEach
    void clean_work(){
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();
    }
    //
    @Test
    void should_add_rs_event_when_user_id_exist() throws Exception {
        RsEventDto rsEventDto = RsEventDto.builder().eventName("龙卷风").keyWord("天气").user(userDto).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonValue = objectMapper.writeValueAsString(rsEventDto);
        mockMvc.perform(post("/rs/event").content(jsonValue).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertEquals(4, rsEventDtoList.size());
        assertEquals("龙卷风", rsEventDtoList.get(3).getEventName());
        assertEquals("天气", rsEventDtoList.get(3).getKeyWord());


    }

    @Test
    void should_return_bad_request_if_user_not_exist_when_add_rs_event() throws Exception {
        RsEventDto rsEventDtoUserNotExist = RsEventDto.builder().eventName("龙卷风").keyWord("天气").user(userDtoNew).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonUserNotExist = objectMapper.writeValueAsString(rsEventDtoUserNotExist);


        mockMvc.perform(post("/rs/event").content(jsonUserNotExist).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
            List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
            assertEquals(3, rsEventDtoList.size());

    }

    @Test
    void should_update_rs_event() throws Exception {
        rsEventRepository.save(rsEventDto);
        RsEventForModify messageObject = RsEventForModify.builder().eventName("乘风破浪的姐姐更新").keyWord("娱乐").userId(userDto.getId()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String eventMessage = objectMapper.writeValueAsString(messageObject);

        mockMvc.perform(patch("/rs/{rsEventId}",rsEventDto.getId()).content(eventMessage).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertEquals(4,rsEventDtoList.size());
        assertEquals("娱乐", rsEventDtoList.get(3).getKeyWord());
        assertEquals("乘风破浪的姐姐更新", rsEventDtoList.get(3).getEventName());
    }

    @Test
    void should_return_bad_request_when_send_no_event_id_to_update_rs_event() throws Exception {
        rsEventRepository.save(rsEventDto);
        RsEventForModify messageObject = RsEventForModify.builder().eventName("乘风破浪的姐姐更新").keyWord("娱乐").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String eventMessage = objectMapper.writeValueAsString(messageObject);
        mockMvc.perform(patch("/rs/{rsEventId}",1).content(eventMessage).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_only_update_keyword_when_update_event_giving_event_name_empty() throws Exception {
        rsEventRepository.save(rsEventDto);
        RsEventForModify messageNoEventName = RsEventForModify.builder().keyWord("音乐").userId(userDto.getId()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String eventMessageNoEventName = objectMapper.writeValueAsString(messageNoEventName);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventDto.getId()).content(eventMessageNoEventName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertEquals(4,rsEventDtoList.size());
        assertEquals("音乐", rsEventDtoList.get(3).getKeyWord());
        assertEquals("北京广东半决赛", rsEventDtoList.get(3).getEventName());


    }

    @Test
    void should_only_update_event_name_when_update_event_giving_keyword_empty() throws Exception {
        rsEventRepository.save(rsEventDto);
        RsEventForModify messageNoEventName = RsEventForModify.builder().eventName("新裤子乐队").userId(userDto.getId()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String eventMessageNoEventName = objectMapper.writeValueAsString(messageNoEventName);
        mockMvc.perform(patch("/rs/{rsEventId}",rsEventDto.getId()).content(eventMessageNoEventName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertEquals(4,rsEventDtoList.size());
        assertEquals("体育", rsEventDtoList.get(3).getKeyWord());
        assertEquals("新裤子乐队", rsEventDtoList.get(3).getEventName());
    }

    @Test
    public void get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("The first rs event")))
                .andExpect(jsonPath("$[0].keyWord",is("no type")))
                .andExpect(jsonPath("$[0].id",is(rsEventDto1.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(0)))
                .andExpect(jsonPath("$[1].eventName",is("The second rs event")))
                .andExpect(jsonPath("$[1].keyWord",is("no type")))
                .andExpect(jsonPath("$[1].id",is(rsEventDto2.getId())))
                .andExpect(jsonPath("$[1].voteNum",is(0)))
                .andExpect(jsonPath("$[2].eventName",is("The third rs event")))
                .andExpect(jsonPath("$[2].keyWord",is("no type")))
                .andExpect(jsonPath("$[2].id",is(rsEventDto3.getId())))
                .andExpect(jsonPath("$[2].voteNum",is(0)))
                .andExpect(jsonPath("$[2]",not(hasKey("user"))));
    }

    @Test
    public void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("The first rs event")))
                .andExpect(jsonPath("$.keyWord",is("no type")))
                .andExpect(jsonPath("$",not(hasKey("user"))));

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("The second rs event")))
                .andExpect(jsonPath("$.keyWord",is("no type")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("The third rs event")))
                .andExpect(jsonPath("$.keyWord",is("no type")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
    }

    @Test
    public void get_sublist_of_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("The first rs event")))
                .andExpect(jsonPath("$[0].keyWord",is("no type")))
                .andExpect(jsonPath("$[1].eventName",is("The second rs event")))
                .andExpect(jsonPath("$[1].keyWord",is("no type")));

        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("The second rs event")))
                .andExpect(jsonPath("$[0].keyWord",is("no type")))
                .andExpect(jsonPath("$[1].eventName",is("The third rs event")))
                .andExpect(jsonPath("$[1].keyWord",is("no type")));
    }

    @Test
    public void delete_rs_event() throws Exception {
        mockMvc.perform(get("/rs/delete?order=3"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("The first rs event")))
                .andExpect(jsonPath("$[0].keyWord",is("no type")))
                .andExpect(jsonPath("$[0].id",is(rsEventDto1.getId())))
                .andExpect(jsonPath("$[0].voteNum",is(0)))
                .andExpect(jsonPath("$[1].eventName",is("The second rs event")))
                .andExpect(jsonPath("$[1].keyWord",is("no type")))
                .andExpect(jsonPath("$[1].id",is(rsEventDto2.getId())))
                .andExpect(jsonPath("$[1].voteNum",is(0)));

    }

    @Test
    void should_throw_error_when_get_event_given_index_invalid() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    void should_throw_error_when_add_event_given_rs_name_is_empty() throws Exception {
        RsEventDto rsEventDtoWithNameEmpty = RsEventDto.builder().eventName(null).keyWord("天气").user(userDto).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonValue = objectMapper.writeValueAsString(rsEventDtoWithNameEmpty);
        mockMvc.perform(post("/rs/event").content(jsonValue).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));
    }

    @Test
    void should_throw_error_when_add_event_given_rs_keyword_is_empty() throws Exception {
        userRepository.save(userDtoNew);
        RsEventDto rsEventDtoWithNameEmpty = RsEventDto.builder().eventName("北京广东半决赛").keyWord(null).user(userDtoNew).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(USE_ANNOTATIONS, false);
        String jsonValue = objectMapper.writeValueAsString(rsEventDtoWithNameEmpty);

        mockMvc.perform(post("/rs/event").content(jsonValue).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    void should_throw_error_when_add_event_given_user_is_empty() throws Exception {
        String eventJson =  "{\"eventName\":\"广东台风\",\"keyWord\":\"天气\"}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    void should_throw_error_when_add_event_given_user_is_not_null_but_invalid() throws Exception {
        String eventJson =  "{\"eventName\":\"广东台风\",\"keyWord\":\"天气\",\"user\": {\"userName\":\"reporte444r\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));

    }

    @Test
    void should_throw_error_when_get_sublist_given_invalid_request_param() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }


    @Test
    void should_add_vote_record() throws Exception {
        rsEventRepository.save(rsEventDto);
        VoteDto voteDto = VoteDto.builder().voteNum(3).voteTime(LocalDateTime.now()).user(userDto).rsEvent(rsEventDto).build();
        String jsonVote = (new ObjectMapper()).writeValueAsString(Vote.builder()
                .voteTime(voteDto.getVoteTime())
                .userId(voteDto.getUser().getId())
                .voteNum(voteDto.getVoteNum())
                .build());

        mockMvc.perform(post("/rs/vote/{rsEventId}",rsEventDto.getId())
                .content(jsonVote)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //check vote num for rs ,user ,voteD
        List<RsEventDto> rsEventDtoList = rsEventRepository.findAll();
        assertEquals(103,rsEventDtoList.get(3).getVoteNum());
        List<UserDto> userDtoList = userRepository.findAll();
        assertEquals(7,userDtoList.get(0).getVoteNum());
        List<VoteDto> voteDtoList = voteRepository.findAll();
        assertEquals(3,voteDtoList.get(0).getVoteNum());

    }
}


