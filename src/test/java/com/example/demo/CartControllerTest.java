package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private ItemRepository itemRepository=mock(ItemRepository.class);
    private UserRepository userRepository= mock(UserRepository.class);
    private CartRepository cartRepository= mock(CartRepository.class);
    @Before
    public void setup()
    {
        cartController=new CartController();
        TestUtils.injectObject(cartController,"itemRepository",itemRepository);
        TestUtils.injectObject(cartController,"userRepository",userRepository);
        TestUtils.injectObject(cartController,"cartRepository",cartRepository);

    }
    @Test
    public void testAddtoCart_HappyPath(){
        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("Srini");
        when(userRepository.findByUsername("Srini")).thenReturn(TestUtils.getSampleUser());
        when(itemRepository.findById(cartRequest.getItemId())).thenReturn(Optional.of(TestUtils.getSampleItem()));
        ResponseEntity<Cart> responseEntity=cartController.addTocart(cartRequest);
        assertEquals("Status code is not 200", HttpStatus.OK,responseEntity.getStatusCode());

    }

    @Test
    public void testAddtoCart_InvalidUser(){
        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("Srini");
       // when(userRepository.findByUsername("Srini")).thenReturn(TestUtils.getSampleUser());
        when(itemRepository.findById(cartRequest.getItemId())).thenReturn(Optional.of(TestUtils.getSampleItem()));
        ResponseEntity<Cart> responseEntity=cartController.addTocart(cartRequest);
        assertEquals("Should throw User not found for invalid user", HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
    @Test
    public void testAddtoCart_InvalidItem(){
        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        cartRequest.setUsername("Srini");
         when(userRepository.findByUsername("Srini")).thenReturn(TestUtils.getSampleUser());
        //when(itemRepository.findById(cartRequest.getItemId())).thenReturn(Optional.of(TestUtils.getSampleItem()));
        ResponseEntity<Cart> responseEntity=cartController.addTocart(cartRequest);
        assertEquals("Should throw Item not found for invalid Item", HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }
@Test
    public void testRemoveFromCart_HappyPath(){
        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);
        cartRequest.setUsername("Srini");
        //Add two items to cart
        User user=TestUtils.getSampleUser();
        user.getCart().addItem(TestUtils.getSampleItem());
        user.getCart().addItem(TestUtils.getSampleItem());

        when(userRepository.findByUsername("Srini")).thenReturn(user);
        when(itemRepository.findById(cartRequest.getItemId())).thenReturn(Optional.of(TestUtils.getSampleItem()));


        ResponseEntity<Cart> responseEntity=cartController.removeFromcart(cartRequest);

        assertEquals("Status code is not 200", HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals("Items count should be 1 after removing one item",1,responseEntity.getBody().getItems().size());
        }
}
