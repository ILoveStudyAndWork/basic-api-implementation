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

import javax.validation.Valid;
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
  public ResponseEntity getRsEventByIndex(@PathVariable int index){
    if (index < 1 || index > rsEventService.getRsList().size()){
          throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsEventService.getRsList().get(index-1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEvent(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if (start != null && end != null){
        if (start < 1 || end > rsEventService.getRsList().size()){
          throw new RequestNotValidException("invalid request param");
        }
      return ResponseEntity.ok(rsEventService.getRsList().subList(start-1,end));
    }
    return ResponseEntity.ok(rsEventService.getRsList());
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEventDto rsEventDto) throws Exception{
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
    RsEventDto rsEventDtoToBePatch = rsEventRepository.findById(rsEventId).get();
    if (rsEventDtoToBePatch !=null && rsEventDtoToBePatch.getUser().getId()==Integer.valueOf(userId)) {
      if (eventName != null){
        rsEventDtoToBePatch.setEventName(eventName);
      }
      if (keyword != null){
        rsEventDtoToBePatch.setKeyWord(keyword);
      }
      rsEventRepository.save(rsEventDtoToBePatch);
      return ResponseEntity.created(null).build();
   }
      return ResponseEntity.created(null).build();
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
