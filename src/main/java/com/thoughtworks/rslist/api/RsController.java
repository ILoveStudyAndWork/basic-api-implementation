package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.fonteddata.RsEventForModify;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rs")
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
  @GetMapping("/{index}")
  public ResponseEntity query(@PathVariable int index){
    if (index < 1 || index > rsEventService.getRsList().size()){
          throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsEventService.getRsList().get(index-1));
  }

  @GetMapping("/list")
  public ResponseEntity list(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if (start != null && end != null){
        if (start < 1 || end > rsEventService.getRsList().size()){
          throw new RequestNotValidException("invalid request param");
        }
      return ResponseEntity.ok(rsEventService.getRsList().subList(start-1,end));
    }
    return ResponseEntity.ok(rsEventService.getRsList());
  }

  @PostMapping("/event")
  public ResponseEntity create(@RequestBody @Valid RsEventDto rsEventDto) throws Exception{
    if (userRepository.findById(rsEventDto.getUser().getId()).isPresent()){
      rsEventRepository.save(rsEventDto);
      return ResponseEntity.created(null).build();
    }else
      return ResponseEntity.badRequest().build();

  }


  @PutMapping("/{rsEventId}")
  public ResponseEntity update(@PathVariable int rsEventId,
                               @RequestBody RsEventForModify rsEventForModify) {
    int userId = rsEventForModify.getUserId();
    String eventName = rsEventForModify.getEventName();
    String keyword = rsEventForModify.getKeyWord();
    if (Integer.valueOf(userId) == null){
      return ResponseEntity.badRequest().build();
    }

    Optional<RsEventDto> rsEventDtoOptional = rsEventRepository.findById(rsEventId);
    if (rsEventDtoOptional.isPresent() && rsEventDtoOptional.get().getUser().getId() == userId) {
      RsEventDto rsEventDtoToBePatch = rsEventRepository.findById(rsEventId).get();
      if (eventName != null){
        rsEventDtoToBePatch.setEventName(eventName);
      }
      if (keyword != null){
        rsEventDtoToBePatch.setKeyWord(keyword);
      }
      rsEventRepository.save(rsEventDtoToBePatch);
      return ResponseEntity.created(null).build();
   }
      return ResponseEntity.badRequest().build();

  }



  @PutMapping("/vote/{rsEventId}")
  public ResponseEntity updateVote (@PathVariable int rsEventId, @RequestBody Vote vote) throws Exception {
    int remainVoteNum = userRepository.findById(vote.getUserId()).get().getVoteNum();
    if(remainVoteNum < vote.getVoteNum()){
      return ResponseEntity.badRequest().build();
    }
    rsEventService.vote(vote, rsEventId);
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity delete(@RequestParam int order){
    if (order > rsEventService.getRsList().size() || order < 0){
      throw new RequestNotValidException("invalid request param");
    }
    rsEventService.deleteByOrder(order);
    return ResponseEntity.ok(null);
  }

}


