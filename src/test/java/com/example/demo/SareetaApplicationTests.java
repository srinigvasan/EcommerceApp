package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.hamcrest.number.IsNaN;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.OutputStream;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SareetaApplicationTests {

	@LocalServerPort
	private  int port;
	@Autowired
	private  TestRestTemplate restTemplate;

	private static String token;

	private static boolean created=false;

	private static String userName="Srini";

	@Test
	public void contextLoads() {
	}




	@Before
	public synchronized void  getToken() throws Exception{

		if(!created)
		{

			CreateUserRequest userRequest=new CreateUserRequest();
			userRequest.setUsername(userName);
			userRequest.setConfirmPassword("Password");
			userRequest.setPassword("Password");
			ResponseEntity<User> userResponseEntity=restTemplate.postForEntity("http://localhost:"+port+"/api/user/create",userRequest,User.class);

            created=true;
		}

		JSONObject login=new JSONObject();
		login.put("username","Srini");
		login.put("password","Password");
		ResponseEntity<Object> loginresponse=restTemplate.postForEntity("http://localhost:"+port+"/login",login.toString(),Object.class);
		token=loginresponse.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

	}

	public HttpHeaders getHeaders(boolean authorization){
		HttpHeaders headers = new HttpHeaders();
		if(authorization)
		headers.add(HttpHeaders.AUTHORIZATION, token);
	   return headers;
	}

@Test
	public void testUserAuthentication_happypath(){
		HttpHeaders headers=getHeaders(true);
		ResponseEntity<User> findUserResponse=restTemplate.exchange("http://localhost:"+port+"/api/user/Srini", GET, new HttpEntity<>(headers), User.class);
		assertEquals("Status code should be 200", HttpStatus.OK,findUserResponse.getStatusCode());
		assertEquals("Valid User object should be there", "Srini",findUserResponse.getBody().getUsername());
	}

	@Test
	public void testUserAuthentication_error(){
		HttpHeaders headers=getHeaders(false);
		ResponseEntity<User> findUserResponse=restTemplate.exchange("http://localhost:"+port+"/api/user/Srini", GET, new HttpEntity<>(headers), User.class);
        assertEquals("User should not be authenticated without JWT Token",HttpStatus.FORBIDDEN,findUserResponse.getStatusCode());
	}

	/**
	 * Integration
	 * Add to Item
	 * Submit Order
	 */

	@Test
	public void testSubmitOrder(){
		ModifyCartRequest cartRequest=new ModifyCartRequest();
		cartRequest.setUsername("Srini");
		cartRequest.setQuantity(2);
		cartRequest.setItemId(1);
		ResponseEntity<Cart> cartResponseEntity=restTemplate.exchange("http://localhost:"+port+"/api/cart/addToCart",POST,new HttpEntity<ModifyCartRequest>(cartRequest,getHeaders(true)),Cart.class);
        assertEquals("Add to Cart should be successful",HttpStatus.OK,cartResponseEntity.getStatusCode());
		ResponseEntity<UserOrder> userOrderResponseEntity=restTemplate.exchange("http://localhost:"+port+"/api/order/submit/Srini",POST,new HttpEntity<>(getHeaders(true)), UserOrder.class);
		assertEquals("Order submission should be successful",HttpStatus.OK,userOrderResponseEntity.getStatusCode());
		assertTrue("Valid Order Id is generated",(userOrderResponseEntity.getBody().getId()>0));
	}




}
