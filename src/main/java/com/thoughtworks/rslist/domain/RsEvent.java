package com.thoughtworks.rslist.domain;

public class RsEvent {
    String keyWord;
    String eventName;

    public RsEvent(){}
    public RsEvent(String keyWord, String eventName) {
        this.keyWord = keyWord;
        this.eventName = eventName;
    }

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
