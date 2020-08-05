package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    private String userName;
    @NotNull
    private String gender;
    @NotNull
    @Max(100)
    @Min(18)
    private int age;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^1\\d{10}")
    private String phone;
    private int voteNum;

    public User(String userName, String gender, int age, String email, String phone, int voteNum) {
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.voteNum = voteNum;
    }

    //@JsonIgnore
    public String getUserName() {
        return userName;
    }

    //@JsonProperty
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
