package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {
    @Id
    @GeneratedValue
    private int rsEventId;
    private String keyWord;

    private String eventName;

    //private int userId;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserDto userDto;
}
