package com.example.demo;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void injectedComponentsAreNotNull(){
        assertThat(dataSource,is(notNullValue()));
        assertThat(jdbcTemplate,is(notNullValue()));
        assertThat(jdbcTemplate,is(notNullValue()));
        assertThat(entityManager,is(notNullValue()));
        assertThat(testEntityManager,is(notNullValue()));
        assertThat(userRepository,is(notNullValue()));

    }
    @Before
    public void setup(){

    }


    @Test
    public void testgetUserByName(){
        User user=TestUtils.getSampleUser();
        user.setPassword("Password");

        userRepository.save(user);
        User user1=userRepository.findByUsername("Srini");
       assertEquals("User Id should match",user.getId(),user1.getId());
    }

}
