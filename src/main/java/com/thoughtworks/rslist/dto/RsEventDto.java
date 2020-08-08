package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;
@Table(name = "rs_event")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String keyWord;
    private String eventName;
    private int voteNum;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserDto user;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "rsEvent")
    private List<VoteDto> vote;

    @JsonProperty
    public UserDto getUser() {
        return user;
    }

    @JsonIgnore
    public void setUser(UserDto user) {
        this.user = user;
    }


}
