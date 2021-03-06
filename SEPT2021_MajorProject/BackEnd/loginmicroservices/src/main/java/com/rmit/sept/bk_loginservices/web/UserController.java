package com.rmit.sept.bk_loginservices.web;


import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rmit.sept.bk_loginservices.Repositories.UserRepository;
import com.rmit.sept.bk_loginservices.model.User;
import com.rmit.sept.bk_loginservices.payload.JWTLoginSucessReponse;
import com.rmit.sept.bk_loginservices.payload.LoginRequest;
import com.rmit.sept.bk_loginservices.security.JwtTokenProvider;
import com.rmit.sept.bk_loginservices.services.MapValidationErrorService;
import com.rmit.sept.bk_loginservices.services.UserService;
import com.rmit.sept.bk_loginservices.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;

import static com.rmit.sept.bk_loginservices.security.SecurityConstant.TOKEN_PREFIX;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController{

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @PostMapping("/register")
    //POST method that handles registering a user
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        // Validate passwords match
        userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        User newUser = userService.saveUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }


    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    //matches login input and authenticates valid user
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }
    
    //returns all users as an iterable
    @GetMapping("/all")
    public Iterable<User> getAllUsers(Principal principal) {
        return userService.findAllUsers();
    }

    //test method for users who want to be publisher
    @GetMapping("/usertopublisher")
    public Iterable<User> getPubUsers(Principal principal) {
        System.out.println("test if works");
        return userService.findByPublisherRequest(true);
    }

    // @CrossOrigin(origins = "http://localhost:3000")
    // @GetMapping("/requestlist")
    // public Iterable<User>  getbypublisherrequestlist(){
    //     return userService.findAllpublisherrequests();
    // }

    //sets users who request to be publishers to true
    @PutMapping("/updaterequest")
    public ResponseEntity<?> updateUser(@Valid @RequestBody String id){
        System.out.println(id);
        String usable = extractInt(id);
        System.out.println(usable);
        long userId = Long.parseLong(usable);
        System.out.println(userId);

        //actual put request
        User user = userService.findByID(userId);
        user.setpublisherrequest(true);
        userService.saveUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    
    }

    //helper method convert string to int
    static String extractInt(String str)
    {
        str = str.replaceAll("[^\\d]", " ");
        str = str.trim();
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
  
        return str;
    }
}
