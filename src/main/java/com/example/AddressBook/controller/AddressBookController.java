package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.services.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressbook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    @PostMapping("/add")
    public AddressBook addAddressBook(@RequestBody AddressBookDto addressBookDto){
        log.info("Request for adding new Address Book");
        return  addressBookService.addAddressBook(addressBookDto);
    }

    @GetMapping("/get")
    public List<AddressBook> getAllAddressBook(){
        log.info("Request for getting all the Address Book");
        return addressBookService.getAllAddressBook();
    }

    @GetMapping("/get/{id}")

    public AddressBook getAddressBookById(@PathVariable Long id){
        log.info("Request for finding Address Book with id:{}",id);
        return addressBookService.getAddressBookById(id);
    }
    @PutMapping("/Update/{id}")
    public AddressBook updateAddressBook(@PathVariable Long id, @RequestBody AddressBookDto addressBookDto){
        log.info("Request for updating Address Book with id:{}",id);
        return addressBookService.updateAddressBookById(id,addressBookDto);
    }

    @DeleteMapping("/delete/{id}")

    public String deleteAddressBook(@PathVariable Long id) {
        log.info("Request for deleting Address Book with id:{}",id);
        addressBookService.deleteAddressBookById(id);
        return "Address book entry deleted successfully.";
    }
}
