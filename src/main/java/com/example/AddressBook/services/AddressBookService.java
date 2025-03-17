package com.example.AddressBook.services;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.serviceInterfaces.AddressBookInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressBookService implements AddressBookInterface {

    @Autowired
    AddressBookRepository addressBookRepository;
    @Override
    public AddressBook addAddressBook(AddressBookDto addressBookDto){
        log.info("Adding new AddressBook {}");
        AddressBook addressBook=new AddressBook();
        addressBook.setName(addressBookDto.getName());
        addressBook.setEmail(addressBookDto.getEmail());
        addressBook.setPhone(addressBookDto.getPhone());
        AddressBook savedAddressBook= addressBookRepository.save(addressBook);
        log.info("New AddressBook details saved in db {}");
        return savedAddressBook;
    }
    @Override
    public List<AddressBook> getAllAddressBook(){
        log.info("Fetching all the AddressBook {}");
        List<AddressBook> addressBooks=addressBookRepository.findAll();
        log.info("Fetching done ");
        return addressBooks;
    }

    @Override
    public  AddressBook getAddressBookById(Long id){
        log.info("find details of AddressBook with id ");
        return addressBookRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"+id));
    }

    @Override
    public AddressBook updateAddressBookById(Long id, AddressBookDto addressBookDto){
        log.info("find details of AddressBook with id ");
        AddressBook found=addressBookRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found"));
        found.setName(addressBookDto.getName());
        found.setPhone(addressBookDto.getPhone());
        found.setEmail(addressBookDto.getEmail());

        AddressBook uodate=addressBookRepository.save(found);
        log.info("Update done");
        return uodate;


    }

    @Override
    public void deleteAddressBookById(Long id){
        log.info("find details of AddressBook with id ");
        AddressBook found=addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found: " + id));

        addressBookRepository.delete(found);
        log.info(" AddressBook deleted successfully");
    }
}
