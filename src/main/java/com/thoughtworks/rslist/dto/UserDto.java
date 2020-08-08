package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.beans.IDProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String userName;

    private String gender;

    private int age;

    private String email;

    private String phone;

    private int voteNum = 10;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<RsEventDto> rsEvent;

    @JsonIgnore
    @OneToMany//(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<VoteDto> vote;



}
