package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Table(name = "rs_event")
@Entity
@Data
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserDto user;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "rsEvent")
    private List<VoteDto> vote;
}
