package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import com.example.demo.model.requests.CreateUserRequest;
import org.hamcrest.number.IsNaN;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository= mock(UserRepository.class);
    private CartRepository cartRepository= mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

@Before
    public void setUp(){
        userController=new UserController();
        TestUtils.injectObject(userController,"userRepository",userRepository);
        TestUtils.injectObject(userController,"cartRepository",cartRepository);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
    }
    @Test
    public void testHappyPath(){
       // when(bCryptPasswordEncoder.encode("Password")).thenReturn("ThisisHashed");
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setConfirmPassword("Password");
        createUserRequest.setPassword("Password");
        ResponseEntity<User> responseEntity=userController.createUser(createUserRequest);
        assertEquals("Status code is not 200", HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("userName matched","testUser",responseEntity.getBody().getUsername());

        assertEquals(0,responseEntity.getBody().getId());
    }
@Test
    public void testInvalidPassword(){
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setConfirmPassword("Password");
        createUserRequest.setPassword("Password1");
        ResponseEntity<User> responseEntity=userController.createUser(createUserRequest);
        assertEquals("statusCode should be badrequest ",HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());

    }

    @Test
    public void findUser(){

        when(userRepository.findById(1L)).thenReturn(Optional.of(TestUtils.getSampleUser()));
        ResponseEntity<User> responseEntity=userController.findById(1L);

        assertEquals("Status code is not 200", HttpStatus.OK,responseEntity.getStatusCode());

        assertEquals("User name must be Srini",TestUtils.getSampleUser().getUsername(),responseEntity.getBody().getUsername());

        when(userRepository.findByUsername("Srini")).thenReturn(TestUtils.getSampleUser());
         responseEntity=userController.findByUserName("Srini");
        assertEquals("Status code is not 200", HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("User name must be Srini",TestUtils.getSampleUser().getId(),responseEntity.getBody().getId());

    }
}
