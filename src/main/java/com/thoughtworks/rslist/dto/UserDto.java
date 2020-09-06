package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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


    @NotNull
    @Size(max = 8)
    @Column(name = "name")
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

    private int voteNum = 10;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<RsEventDto> rsEvent;

    @JsonIgnore
    @OneToMany//(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<VoteDto> vote;



}
