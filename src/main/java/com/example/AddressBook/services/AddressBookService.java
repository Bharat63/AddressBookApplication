package com.example.AddressBook.services;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@EnableCaching
public class AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private EmailService emailService;

    @CachePut(value = "addressBook", key = "#result.id")
    public AddressBook addAddressBook(AddressBookDto addressBookDto) {
        try {
            AddressBook addressBook = new AddressBook();
            addressBook.setName(addressBookDto.getName());
            addressBook.setEmail(addressBookDto.getEmail());
            addressBook.setPhone(addressBookDto.getPhone());
            AddressBook savedAddressBook = addressBookRepository.save(addressBook);
            log.info("Added new AddressBook entry: {}", savedAddressBook);
            messagePublisher.sendMessage("contact.add.queue", "New Contact Added: " + savedAddressBook.getEmail());
            emailService.sendNotificationEmail(savedAddressBook.getEmail(), "New contact added to your address book.");
            return savedAddressBook;
        } catch (Exception e) {
            log.error("Error adding AddressBook: {}", e.getMessage());
            throw new RuntimeException("Failed to add address book entry: " + e.getMessage());
        }
    }

    @Cacheable(value = "addressBookList")
    public List<AddressBook> getAllAddressBook() {
        try {
            log.info("Fetching all AddressBook entries from DB.");
            return addressBookRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all AddressBook entries: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch address book entries: " + e.getMessage());
        }
    }

    @Cacheable(value = "addressBook", key = "#id")
    public AddressBook getAddressBookById(Long id) {
        try {
            log.info("Fetching AddressBook with ID: {}", id);
            return addressBookRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Address book with ID " + id + " not found"));
        } catch (Exception e) {
            log.error("Error fetching AddressBook by ID: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch address book with ID: " + id + " due to: " + e.getMessage());
        }
    }

    @CachePut(value = "addressBook", key = "#id")
    public AddressBook updateAddressBookById(Long id, AddressBookDto addressBookDto) {
        try {
            log.info("Updating AddressBook with ID: {}", id);
            AddressBook found = addressBookRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("Address book with ID " + id + " not found"));

            found.setName(addressBookDto.getName());
            found.setPhone(addressBookDto.getPhone());
            found.setEmail(addressBookDto.getEmail());

            AddressBook updated = addressBookRepository.save(found);
            log.info("Updated AddressBook: {}", updated);
            return updated;
        } catch (Exception e) {
            log.error("Error updating AddressBook by ID: {}", e.getMessage());
            throw new RuntimeException("Failed to update address book with ID: " + id + " due to: " + e.getMessage());
        }
    }

    @CacheEvict(value = "addressBook", key = "#id")
    public void deleteAddressBookById(Long id) {
        Optional<AddressBook> addressBook = addressBookRepository.findById(id);

        if (addressBook.isPresent()) {
            addressBookRepository.deleteById(id);
            System.out.println("✅ Address Book deleted successfully!");
        } else {
            throw new RuntimeException("Address book with ID " + id + " not found");
        }
    }

}
