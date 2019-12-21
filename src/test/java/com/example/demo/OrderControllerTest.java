package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class OrderControllerTest {

private OrderController orderController;
    private UserRepository userRepository=mock(UserRepository.class);


    private OrderRepository orderRepository=mock(OrderRepository.class);
    @Before
    public void setup(){
        orderController=new OrderController();
        TestUtils.injectObject(orderController,"userRepository",userRepository);
        TestUtils.injectObject(orderController,"orderRepository",orderRepository);
    }
    @Test
    public void testSubmitOrder_happypath(){
        User user=TestUtils.getSampleUser();
        user.getCart().addItem(TestUtils.getSampleItem());
        user.getCart().addItem(TestUtils.getSampleItem());
        when(userRepository.findByUsername("Srini")).thenReturn(user);
        ResponseEntity<UserOrder> response=orderController.submit("Srini");
        assertEquals("Order should submit successfully", HttpStatus.OK,response.getStatusCode());
        assertEquals("validate if total value of order is 2$",new BigDecimal(2),response.getBody().getTotal());

    }
    @Test
    public void testSubmitOrder_errorscenario_invalidUser(){

        ResponseEntity<UserOrder> response=orderController.submit("Srini");
        assertEquals("Order should throw 404 for invalid user", HttpStatus.NOT_FOUND,response.getStatusCode());

    }
    @Test
    public void testRetrieveOrder(){
        User user=TestUtils.getSampleUser();
        when(userRepository.findByUsername("Srini")).thenReturn(user);
        UserOrder userOrder=new UserOrder();
        userOrder.setUser(user);
        userOrder.setTotal(new BigDecimal(2));
        List<UserOrder> userOrders=new ArrayList<UserOrder>();
        userOrders.add(userOrder);
        when(orderRepository.findByUser(TestUtils.getSampleUser())).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> entity=orderController.getOrdersForUser("Srini");
        assertEquals("Order should be retrived successfully", HttpStatus.OK,entity.getStatusCode());


    }
}
