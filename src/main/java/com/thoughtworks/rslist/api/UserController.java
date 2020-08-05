package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();

    @RequestMapping("/user")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity registerUser(@RequestBody @Valid User user){
        userList.add(user);
        return ResponseEntity.created(null).build();
    }


}
