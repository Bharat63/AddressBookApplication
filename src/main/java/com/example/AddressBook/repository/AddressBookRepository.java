package com.example.AddressBook.repository;

import com.example.AddressBook.model.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
}
