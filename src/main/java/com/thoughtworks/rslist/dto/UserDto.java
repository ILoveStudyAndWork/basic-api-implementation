package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.beans.IDProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
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
