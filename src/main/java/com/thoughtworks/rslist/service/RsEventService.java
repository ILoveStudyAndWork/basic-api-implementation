package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service
public class RsEventService {
    public static List<RsEventDto> rsList;
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public RsEventService(RsEventRepository rsEventRepository
            , UserRepository userRepository
            , VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<UserDto> userDto = userRepository.findById(vote.getUserId());
        Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
        UserDto userToBeUpdate = userDto.get();
        RsEventDto eventToBeUpdate = rsEventDto.get();
        if (userDto.isPresent() && userToBeUpdate.getVoteNum() > vote.getVoteNum() && rsEventDto.isPresent()) {
            userToBeUpdate.setVoteNum(userToBeUpdate.getVoteNum() - vote.getVoteNum());
            eventToBeUpdate.setVoteNum(eventToBeUpdate.getVoteNum() + vote.getVoteNum());

            VoteDto voteDto = VoteDto.builder()
                    .rsEvent(rsEventDto.get())
                    .user(userDto.get())
                    .voteNum(vote.getVoteNum())
                    .voteTime(vote.getVoteTime())
                    .build();
            rsEventRepository.save(eventToBeUpdate);
            userRepository.save(userToBeUpdate);
            voteRepository.save(voteDto);
        } else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public List<RsEventDto> getRsList(){
         rsList = rsEventRepository.findAll();
         return rsList;
    }

    public void deleteByOrder(int order){
        RsEventDto rsEventDto = getRsList().get(order-1);
        int id = rsEventDto.getId();
        rsEventRepository.deleteById(id);
    }
}
