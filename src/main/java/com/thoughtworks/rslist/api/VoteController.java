package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId,@RequestParam int rsEventId){
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId,rsEventId).stream().map(
                        item-> Vote.builder().
                                voteNum(item.getVoteNum())
                                .rsEventId(item.getRsEvent().getId())
                                .userId(item.getUser().getId())
                                .voteTime(item.getVoteTime())
                                .build())
                        .collect(Collectors.toList()));
    }
}
