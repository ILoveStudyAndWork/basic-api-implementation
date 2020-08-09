package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VoteDto,Integer> {
    @Override
    List<VoteDto> findAll();

    List<VoteDto> findAllByUserIdAndRsEventId(int userId,int rsEventId);
}
