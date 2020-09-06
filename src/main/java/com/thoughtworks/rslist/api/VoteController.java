package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.DateNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @GetMapping("/voteRecordByTime")
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@RequestParam String startTime,
                                                          @RequestParam String endTime) throws ParseException, DateNotValidException {
        if (!theStringIsValidDate(startTime) || !theStringIsValidDate(endTime) ){
            throw new DateNotValidException("input time invalid:the format is not a date format");
        }
        //transfer string to date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = dateFormat.parse(startTime);
        Date endDate = dateFormat.parse(endTime);
        if (!theEndTimeLater(startDate, endDate)){
            throw new DateNotValidException("input time invalid:the start time is later than end time");
        }
        //compare endDate and now
        Date now = new Date();
        if (now.compareTo(endDate) < 0 ){
            throw new DateNotValidException("input time invalid:please input endDate before now!");
        }

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime;
        VoteDto currentVoteDto;
        //get voteTime to Date format then compare
        List<VoteDto> voteDtoList = voteRepository.findAll();
        List<Vote> voteList = new ArrayList<>();
        for (int i = 0; i < voteDtoList.size(); i++){
            currentVoteDto = voteDtoList.get(i);
            localDateTime = currentVoteDto.getVoteTime();
            ZonedDateTime zdt = localDateTime.atZone(zoneId);
            Date voteDate = Date.from(zdt.toInstant());
            if (voteDateIsBetween(startDate,voteDate,endDate)){
                voteList.add(Vote.builder()
                        .voteTime(currentVoteDto.getVoteTime())
                        .userId(currentVoteDto.getUser().getId())
                        .rsEventId(currentVoteDto.getRsEvent().getId())
                        .voteNum(currentVoteDto.getVoteNum())
                        .build());
            }
        }
        return ResponseEntity.ok(voteList);

    }

    boolean theStringIsValidDate(String dateToBeTest){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateToBeTest.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    boolean theEndTimeLater(Date startDate, Date endDate) throws ParseException {
        return endDate.compareTo(startDate) > 0;
    }

    boolean voteDateIsBetween(Date startDate,Date voteDate, Date endDate) throws ParseException {
        return voteDate.compareTo(startDate) >0 && voteDate.compareTo(endDate)<0;
    }
}
