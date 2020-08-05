package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
public class RsController {
  public static List<RsEvent> rsList;

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
      return ResponseEntity.ok(rsList.subList(start-1,end));
    }
    return ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws Exception {

    rsList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/modify")
  public void modifyRsEvent(@RequestBody String json,@RequestParam Integer order) throws JsonProcessingException {
    RsEvent eventToBeModify = rsList.get(order-1);
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(json,RsEvent.class);
    if (rsEvent.getEventName() != null){
      eventToBeModify.setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null){
      eventToBeModify.setKeyWord(rsEvent.getKeyWord());
    }
  }

  @GetMapping("/rs/delete")
  public void addRsEvent(@RequestParam Integer order) throws Exception {
    rsList.remove(rsList.get(order-1));
  }




}
