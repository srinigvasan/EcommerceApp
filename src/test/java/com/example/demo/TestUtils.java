package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {

    public static void injectObject(Object target,String fieldName,Object toInject){

        try {
            boolean isprivate=false;
            Field field=target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible())
            {
                field.setAccessible(true);
                isprivate=true;
            }
            field.set(target,toInject);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User getSampleUser(){
        User user=new User();
        user.setId(1);
        user.setUsername("Srini");

        user.setCart(new Cart());
        return user;
    }

    public static Item getSampleItem(){
        Item item=new Item();
        item.setPrice(new BigDecimal(1));
        item.setName("item1");
        item.setId(1L);
        item.setDescription("item");
        return item;
    }
}
