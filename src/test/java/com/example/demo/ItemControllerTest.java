package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemRepository itemRepository=mock(ItemRepository.class);
    private ItemController itemController;
    @Before
    public void setup(){
        itemController=new ItemController();
        TestUtils.injectObject(itemController,"itemRepository",itemRepository);
    }
    @Test
    public void testgetByItemName(){
        List<Item> itemList=new ArrayList<Item>();
        itemList.add(TestUtils.getSampleItem());
        when(itemRepository.findByName("item1")).thenReturn(itemList);
        ResponseEntity<List<Item>> entity=itemController.getItemsByName("item1");
        assertEquals("item name is matching with search query","item1",entity.getBody().get(0).getName());
    }

    @Test
    public void testgetByItemName_errorscenario(){
      //  when(itemRepository.findByName("item1")).thenReturn(itemList);
        ResponseEntity<List<Item>> entity=itemController.getItemsByName("item1");
         assertEquals("404 for invalid item name",HttpStatus.NOT_FOUND,entity.getStatusCode());
    }
@Test
    public void testgetByItemId(){

        when(itemRepository.findById(1L)).thenReturn(Optional.of(TestUtils.getSampleItem()));
        ResponseEntity<Item> entity=itemController.getItemById(1L);
        assertEquals("item name is matching with search query","item1",entity.getBody().getName());
    }

    @Test
    public void testgetItems(){
        List<Item> itemList=new ArrayList<Item>();
        itemList.add(TestUtils.getSampleItem());
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> entity=itemController.getItems();
        assertEquals("size of item list must be 1",1,entity.getBody().size());

    }
}
