package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    public static List<User> userList;

    @Autowired
    UserRepository userRepository;


    @RequestMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user){
        UserDto userDto = UserDto.builder()
                .userName(user.getUserName())
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum())
                .build();
        userRepository.save(userDto);


//        userList.add(user);
        //String indexToString = Integer.toString(userList.size()-1);
        //return ResponseEntity.created(null).header("index",indexToString).build();
        return ResponseEntity.created(null).build();
    }
    @GetMapping("/users")
    public ResponseEntity getUserList(@RequestParam String userId){
        Optional<UserDto> userDto = userRepository.findById(Integer.valueOf(userId));
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user/delete")
    public ResponseEntity deleteUserById(@RequestParam String userId){
        userRepository.deleteById(Integer.valueOf(userId));

        return ResponseEntity.created(null).build();
    }

}
