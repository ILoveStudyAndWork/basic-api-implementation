package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

public class RsEvent {
    String keyWord;
    String eventName;
    @Valid
    User user;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent(){}
    public RsEvent(String keyWord, String eventName,User user) {
        this.keyWord = keyWord;
        this.eventName = eventName;
        this.user = user;
    }

//    public RsEvent(String keyWord, String eventName) {
//        this.keyWord = keyWord;
//        this.eventName = eventName;
//    }
    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
