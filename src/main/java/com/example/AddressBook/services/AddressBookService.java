package com.example.AddressBook.services;

import com.example.AddressBook.Exception.AddressBookNotFoundException;
import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.serviceInterfaces.AddressBookInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@EnableCaching
public class AddressBookService implements AddressBookInterface {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @CachePut(value = "addressBook", key = "#result.id")
    public AddressBook addAddressBook(AddressBookDto addressBookDto) {
        AddressBook addressBook = new AddressBook();
        addressBook.setName(addressBookDto.getName());
        addressBook.setEmail(addressBookDto.getEmail());
        addressBook.setPhone(addressBookDto.getPhone());
        AddressBook savedAddressBook = addressBookRepository.save(addressBook);
        log.info("Added new AddressBook entry: {}", savedAddressBook);
        return savedAddressBook;
    }

    @Cacheable(value = "addressBookList")
    public List<AddressBook> getAllAddressBook() {
        log.info("Fetching all AddressBook entries from DB.");
        return addressBookRepository.findAll();
    }

    @Cacheable(value = "addressBook", key = "#id")
    public AddressBook getAddressBookById(Long id) {
        log.info("Fetching AddressBook with ID: {}", id);
        return addressBookRepository.findById(id)
                .orElseThrow(() -> new AddressBookNotFoundException("Id not found: " + id));
    }

    @CachePut(value = "addressBook", key = "#id")
    public AddressBook updateAddressBookById(Long id, AddressBookDto addressBookDto) {
        log.info("Updating AddressBook with ID: {}", id);
        AddressBook found = addressBookRepository.findById(id)
                .orElseThrow(() -> new AddressBookNotFoundException("Id not found: " + id));

        found.setName(addressBookDto.getName());
        found.setPhone(addressBookDto.getPhone());
        found.setEmail(addressBookDto.getEmail());

        AddressBook updated = addressBookRepository.save(found);
        log.info("Updated AddressBook: {}", updated);
        return updated;
    }

    @CacheEvict(value = "addressBook", key = "#id")
    public void deleteAddressBookById(Long id) {
        log.info("Deleting AddressBook with ID: {}", id);
        AddressBook found = addressBookRepository.findById(id)
                .orElseThrow(() -> new AddressBookNotFoundException("Id not found: " + id));

        addressBookRepository.delete(found);
        log.info("Deleted AddressBook with ID: {}", id);
    }

    @CacheEvict(value = "addressBookList", allEntries = true)
    public void clearAddressBookCache() {
        log.info("Clearing all cached AddressBook entries.");
    }
}
