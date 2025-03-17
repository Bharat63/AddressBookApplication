package com.example.AddressBook.services;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.serviceInterfaces.AddressBookInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService implements AddressBookInterface {

    @Autowired
    AddressBookRepository addressBookRepository;
    @Override
    public AddressBook addAddressBook(AddressBookDto addressBookDto){
        AddressBook addressBook=new AddressBook();
        addressBook.setName(addressBookDto.getName());
        addressBook.setEmail(addressBookDto.getEmail());
        addressBook.setPhone(addressBookDto.getPhone());
        return addressBookRepository.save(addressBook);
    }
    @Override
    public List<AddressBook> getAllAddressBook(){
        return addressBookRepository.findAll();
    }

    @Override
    public  AddressBook getAddressBookById(Long id){
        return addressBookRepository.findById(id).orElseThrow(()-> new RuntimeException("Id not found"+id));
    }

    @Override
    public AddressBook updateAddressBookById(Long id, AddressBookDto addressBookDto){
        AddressBook found=addressBookRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found"));
        found.setName(addressBookDto.getName());
        found.setPhone(addressBookDto.getPhone());
        found.setEmail(addressBookDto.getEmail());

        return addressBookRepository.save(found);



    }

    @Override
    public void deleteAddressBookById(Long id){
        AddressBook found=addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found: " + id));

        addressBookRepository.delete(found);
    }
}
