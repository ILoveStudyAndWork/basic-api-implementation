package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.valid.Validate;
import com.thoughtworks.rslist.valid.Validate1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
public class RsController {
  public static List<RsEvent> rsList;

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;

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
      return ResponseEntity.ok(rsList.subList(start-1,end));
    }
    return ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody  RsEventDto rsEventDto) throws Exception{
    //find if the user exist
    if (userRepository.findById(rsEventDto.getUserId()).isPresent()){
      rsEventRepository.save(rsEventDto);
      return ResponseEntity.created(null).build();
    }else
      return ResponseEntity.badRequest().build();

  }

//  @PostMapping("/rs/event")
//  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws Exception{
//    User user = rsEvent.getUser();
//    if (!UserController.userList.contains(user)){
//      UserController.userList.add(user);
//    }
//    rsList.add(rsEvent);
//    String indexToString = Integer.toString(rsList.size()-1);
//    return ResponseEntity.created(null).header("index",indexToString).build();
//  }


  @PostMapping("/rs/modify")
  public ResponseEntity modifyRsEvent(@RequestBody String json,@RequestParam Integer order) throws JsonProcessingException {
    RsEvent eventToBeModify = rsList.get(order-1);
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





}
