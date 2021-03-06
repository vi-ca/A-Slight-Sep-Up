package com.rmit.sept.bk_loginservices.services;




import com.rmit.sept.bk_loginservices.Repositories.UserRepository;
import com.rmit.sept.bk_loginservices.exceptions.UsernameAlreadyExistsException;
import com.rmit.sept.bk_loginservices.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){

      /*  newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        //Username has to be unique (exception)
        // Make sure that password and confirmPassword match
        // We don't persist or show the confirmPassword
        return userRepository.save(newUser);
       */
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            newUser.setUserStatus(newUser.getUserStatus());
            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }

    }

    //save input into repository and then sql
    public User saveorUpdateUser (User user){
        return userRepository.save(user);
    }

    //set user to "want to be publisher"
    public void updatePubUser(User user){
        user.setpublisherrequest(true);
        userRepository.save(user);
    }

    //grabs user by id
    public User findByID(Long id){
        return userRepository.getById(id);

    }

    //grabs users based on whether they "want" to be a publisher
    public Iterable<User> findByPublisherRequest(boolean value){
        return userRepository.getBypublisherrequest(true);

    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    public void enable(User user) {
        user.setAccountEnabled(true);
        userRepository.save(user);
    }

    // calls CRUDREPO built in findall megthod
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    // public void sendpublisherrequests(){
    //     userRepository.setThepublisherrequest(true);
    // }

    public void deleteAllUsers() {
        if(userRepository.count() > 0) {
            userRepository.deleteAll();
        }
    }

}
