package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEvent();

  public List<RsEvent> initRsEvent(){
    List<RsEvent> rsEvent = new ArrayList<>();
    rsEvent.add(new RsEvent("无标签","第一条事件"));
    rsEvent.add(new RsEvent("无标签","第二条事件"));
    rsEvent.add(new RsEvent("无标签","第三条事件"));
    return rsEvent;
  }



  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEvent(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if (start != null && end != null){
      return rsList.subList(start-1,end);
    }
    return rsList;
  }

  @PostMapping("/rs/event")
  public void addRsEvent(@RequestBody String json) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(json,RsEvent.class);
    rsList.add(rsEvent);
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
