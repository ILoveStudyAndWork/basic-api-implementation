package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class RsControllerTest {

    @Autowired
    MockMvc mockMvc ;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;


    @BeforeEach
    void setUp(){
        userRepository.deleteAll();


//
//        RsController.rsList = new ArrayList<>();
//
//        User user = new User("abc","male",20,"abc@abc.com","18978654567",10);
//        RsController.rsList.add(new RsEvent("无标签","第一条事件",user));
//        RsController.rsList.add(new RsEvent("无标签","第二条事件",user));
//        RsController.rsList.add(new RsEvent("无标签","第三条事件",user));
//        UserController.userList = new ArrayList<>();
//        UserController.userList.add(user);

    }

    @Test
    void should_return_bad_request_if_user_not_exist_when_add_rs_event() throws Exception {

        UserDto userDto = UserDto.builder()
                .userName("xiaoming")
                .age(20)
                .email("a@b.com")
                .gender("male")
                .phone("19876545676")
                .voteNum(10)
                .build();
        userRepository.save(userDto);

        RsEventDto rsEventDto = RsEventDto.builder().eventName("龙卷风").keyWord("天气").userId(1).build();
        String jsonValue = new ObjectMapper().writeValueAsString(rsEventDto);
        mockMvc.perform(post("/rs/event").content(jsonValue).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserDto> userDtoList = userRepository.findAll();
        assertEquals(1,userDtoList.size());
        assertEquals("xiaoming",userDtoList.get(0).getUserName());
        assertEquals("a@b.com",userDtoList.get(0).getEmail());
        assertEquals("male",userDtoList.get(0).getGender());
        assertEquals(10,userDtoList.get(0).getVoteNum());
        assertEquals("19876545676",userDtoList.get(0).getPhone());
        assertEquals(1,userDtoList.get(0).getUserId());

        RsEventDto rsEventDtoUserNotExist = RsEventDto.builder().eventName("龙卷风").keyWord("天气").userId(2).build();
        String jsonUserNotExist = new ObjectMapper().writeValueAsString(rsEventDtoUserNotExist);
        mockMvc.perform(post("/rs/event").content(jsonUserNotExist).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldAddUserIfUserNotExist(){
//
//        mockMvc.perform(post("rs/list").content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$"),hasSize(1));
    }
    @Test
    public void get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2]",not(hasKey("user"))));
    }
    @Test
    public void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));

        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第三条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
                .andExpect(jsonPath("$",not(hasKey("user"))));
    }

    @Test
    public void get_sublist_of_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")));

        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")));
    }

    @Test
    public void add_rs_event_to_list() throws Exception {
        String eventJson =  "{\"eventName\":\"牛肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"newUser\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","3"));

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")))
                .andExpect(jsonPath("$[3].eventName",is("牛肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord",is("经济")));
    }

    @Test
    void modify_rs_event() throws Exception {
        User user = new User("modify","male",20,"abc@abc.com","18978654567",10);
        RsEvent rsEvent = new RsEvent("天文","月全食出现",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String addEvent = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/modify?order=1").content(addEvent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","0"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("月全食出现")))
                .andExpect(jsonPath("$[0].keyWord",is("天文")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")));


        RsEvent rsEventEnt = new RsEvent(null,"乘风破浪的姐姐开播",user);
        String addEventEnt = objectMapper.writeValueAsString(rsEventEnt);
        mockMvc.perform(post("/rs/modify?order=2").content(addEventEnt).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","1"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("月全食出现")))
                .andExpect(jsonPath("$[0].keyWord",is("天文")))
                .andExpect(jsonPath("$[1].eventName",is("乘风破浪的姐姐开播")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")));

        RsEvent rsEventTech = new RsEvent("科技",null,user);
        String addEventTech = objectMapper.writeValueAsString(rsEventTech);
        mockMvc.perform(post("/rs/modify?order=3").content(addEventTech).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","2"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("月全食出现")))
                .andExpect(jsonPath("$[0].keyWord",is("天文")))
                .andExpect(jsonPath("$[1].eventName",is("乘风破浪的姐姐开播")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("科技")));

    }

    @Test
    public void delete_rs_event() throws Exception {
        mockMvc.perform(get("/rs/delete?order=3"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")));

    }

    @Test
    void should_throw_error_when_index_invalid() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    void should_throw_exception_when_user_not_valid() throws Exception {
        //why can't de-serialization success?
        String eventJson =  "{\"eventName\":\"什么肉都涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"xyxiainvalid\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void should_add_user_to_user_list_if_user_not_exist_yet() throws Exception {
        String eventJson =  "{\"eventName\":\"牛肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"newUser\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    void should_add_not_add_user_to_usr_list_if_user_exist() throws Exception {
        String eventJson =  "{\"eventName\":\"口罩降价了\",\"keyWord\":\"经济\",\"user\": {\"userName\":\"abc\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    void should_throw_error_when_rs_name_is_empty() throws Exception {
        //difference between null and ""
        String eventJson =  "{\"keyWord\":\"天气\",\"user\": {\"userName\":\"reporter\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));
    }
    @Test
    void should_throw_error_when_rs_keyword_is_empty() throws Exception {
        //difference between null and ""
        String eventJson =  "{\"eventName\":\"广东台风\",\"user\": {\"userName\":\"reporter\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    void should_throw_error_when_user_is_empty() throws Exception {
        //difference between null and ""
        String eventJson =  "{\"eventName\":\"广东台风\",\"keyWord\":\"天气\"}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    void should_throw_error_when_user_is_not_null_but_invalid() throws Exception {
        //difference between null and ""
        String eventJson =  "{\"eventName\":\"广东台风\",\"keyWord\":\"天气\",\"user\": {\"userName\":\"reporte444r\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\",\"voteNum\":\"10\"}}";
        mockMvc.perform(post("/rs/event").content(eventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));

    }

    @Test
    void should_throw_error_when_given_invalid_request_param() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }


}
