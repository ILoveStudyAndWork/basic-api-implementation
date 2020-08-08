package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RsController {
  public static List<RsEventDto> rsList;

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  VoteRepository voteRepository;
  @Autowired
  RsEventService rsEventService;

  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEvent(@PathVariable int index){
    if (index < 1 || index > rsList.size()){
          throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEvent(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if (start != null && end != null){
        if (start < 1 || end > rsList.size()){
          throw new RequestNotValidException("invalid request param");
        }
      return ResponseEntity.ok(rsEventService.getRsList().subList(start-1,end));
    }
    return ResponseEntity.ok(rsEventService.getRsList());
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody  RsEventDto rsEventDto) throws Exception{
    //find if the user exist
    if (userRepository.findById(rsEventDto.getUser().getId()).isPresent()){
      rsEventRepository.save(rsEventDto);
      return ResponseEntity.created(null).build();
    }else
      return ResponseEntity.badRequest().build();

  }


  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity patchRsEvent(@PathVariable int rsEventId
                                    ,@RequestBody String jsonEvent) throws JsonProcessingException {

    Map<String, String> map = new ObjectMapper().readValue(jsonEvent, Map.class);
    String eventName = map.get("eventName");

    String keyword = map.get("keyword");
    String userId = map.get("userId");
    if (userId == null){
      return ResponseEntity.badRequest().build();
    }
    //read the userId by rsEvent
    Optional<RsEventDto> rsEventDtoToBePatch = rsEventRepository.findById(rsEventId);
    if (rsEventDtoToBePatch.isPresent()) {
      int userIdInRsEvent = rsEventDtoToBePatch.get().getUser().getId();
      String eventNameInRsEvent = rsEventDtoToBePatch.get().getEventName();
      String keyWordInRsEvent = rsEventDtoToBePatch.get().getKeyWord();
      if (eventName == null){
        eventName = eventNameInRsEvent;
      }
      if (keyword == null){
        keyword = keyWordInRsEvent;
      }
      if (userIdInRsEvent == Integer.valueOf(userId)) {
        //update
        RsEventDto rsEventDto = RsEventDto.builder()
                .eventName(eventName)
                .keyWord(keyword)
                .id(2)
                .user(rsEventDtoToBePatch
                        .get()
                        .getUser())
                .build();
        rsEventRepository.save(rsEventDto);
        return ResponseEntity.created(null).build();
      }
    }
      return ResponseEntity.created(null).build();

  }



  @PostMapping("/rs/modify")
  public ResponseEntity modifyRsEvent(@RequestBody String json,@RequestParam Integer order) throws JsonProcessingException {
    RsEventDto eventToBeModify = rsList.get(order-1);
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(json,RsEvent.class);
    if (rsEvent.getEventName() != null){
      eventToBeModify.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null){
      eventToBeModify.setKeyWord(rsEvent.getKeyWord());
    }

    String indexToString = Integer.toString(order-1);
    return ResponseEntity.created(null).header("index",indexToString).build();
  }

  @GetMapping("/rs/delete")
  public ResponseEntity addRsEvent(@RequestParam Integer order) throws Exception {
    rsList.remove(rsList.get(order-1));
    return ResponseEntity.ok().build();
  }



  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity voteWithEventId (@PathVariable int rsEventId, @RequestBody Vote vote) throws Exception {
    int remainVoteNum = userRepository.findById(vote.getUserId()).get().getVoteNum();
    if(remainVoteNum < vote.getVoteNum()){
      return ResponseEntity.badRequest().build();
    }
    rsEventService.vote(vote, rsEventId);
    return ResponseEntity.created(null).build();
  }
}
