package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> userList;


    @RequestMapping("/user")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity registerUser(@RequestBody @Valid User user){
        userList.add(user);
        return ResponseEntity.created(null).build();
    }
    @GetMapping("/userList")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }


}
