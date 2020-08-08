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
    public ResponseEntity registerUser(@RequestBody  UserDto userDto){
        Optional<UserDto> userDtoOptional = userRepository.findById(userDto.getId());
        if (!userDtoOptional.isPresent()){
            userRepository.save(userDto);
            return ResponseEntity.created(null).build();
        }else {
            return ResponseEntity.badRequest().build();
        }


    }
    @GetMapping("/users")
    public ResponseEntity getUserList(@RequestParam String userId){
        Optional<UserDto> userDto = userRepository.findById(Integer.valueOf(userId));
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable int id){
        Optional<UserDto> userDtoOptional = userRepository.findById(id);
        if (userDtoOptional.isPresent()){
            userRepository.deleteById(id);
            return ResponseEntity.created(null).build();
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

}
