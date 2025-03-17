package com.example.AddressBook.serviceInterfaces;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;

import java.util.List;

public interface AddressBookInterface {
    AddressBook addAddressBook(AddressBookDto addressBookDto);
    List<AddressBook> getAllAddressBook();
    AddressBook getAddressBookById(Long id);
    AddressBook updateAddressBookById(Long id, AddressBookDto addressBookDto);
    void deleteAddressBookById(Long id);
}
