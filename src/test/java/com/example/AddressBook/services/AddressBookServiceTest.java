package com.example.AddressBook.services;

import com.example.AddressBook.dto.AddressBookDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

    @Mock
    private AddressBookRepository addressBookRepository;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AddressBookService addressBookService;

    private AddressBook addressBook;
    private AddressBookDto addressBookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressBook = new AddressBook(1L, "Bhupesh Nauhwar", "bhupesh052000@gmail.com", "1234567890");
        addressBookDto = new AddressBookDto(2L, "Bhupesh Nauhwar", "bhupesh052000@gmail.com", "1234567890");
    }

    @Test
    void testAddAddressBook_Success() {
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        AddressBook saved = addressBookService.addAddressBook(addressBookDto);

        assertTrue(saved != null);
    }

    @Test
    void testAddAddressBook_Failure() {
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(null);

        AddressBook saved = addressBookService.addAddressBook(addressBookDto);

        assertFalse(saved != null);
    }

    @Test
    void testAddAddressBook_Exception() {
        when(addressBookRepository.save(any(AddressBook.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> addressBookService.addAddressBook(addressBookDto));
    }

    @Test
    void testGetAllAddressBook_Success() {
        when(addressBookRepository.findAll()).thenReturn(List.of(addressBook));

        List<AddressBook> list = addressBookService.getAllAddressBook();

        assertTrue(list.size() > 0);
    }

    @Test
    void testGetAllAddressBook_Failure() {
        when(addressBookRepository.findAll()).thenReturn(List.of());

        List<AddressBook> list = addressBookService.getAllAddressBook();

        assertFalse(list.size() > 0);
    }

    @Test
    void testGetAllAddressBook_Exception() {
        when(addressBookRepository.findAll()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> addressBookService.getAllAddressBook());
    }

    @Test
    void testGetAddressBookById_Success() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));

        AddressBook result = addressBookService.getAddressBookById(1L);

        assertTrue(result != null);
    }

    @Test
    void testGetAddressBookById_Failure() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

        AddressBook result = addressBookService.getAddressBookById(1L);

        assertFalse(result != null);
    }

    @Test
    void testGetAddressBookById_Exception() {
        when(addressBookRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> addressBookService.getAddressBookById(1L));
    }

    @Test
    void testUpdateAddressBookById_Success() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        AddressBook updated = addressBookService.updateAddressBookById(1L, addressBookDto);

        assertTrue(updated != null);
    }

    @Test
    void testUpdateAddressBookById_Failure() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

        AddressBook updated = addressBookService.updateAddressBookById(1L, addressBookDto);

        assertFalse(updated != null);
    }

    @Test
    void testUpdateAddressBookById_Exception() {
        when(addressBookRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> addressBookService.updateAddressBookById(1L, addressBookDto));
    }


    @Test
    void testDeleteAddressBookById_Success() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));

        addressBookService.deleteAddressBookById(1L);

        verify(addressBookRepository, times(1)).delete(addressBook);
        assertTrue(true);
    }

    @Test
    void testDeleteAddressBookById_Failure() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

        addressBookService.deleteAddressBookById(1L);

        verify(addressBookRepository, times(0)).delete(any());
        assertFalse(addressBookRepository.findById(1L).isPresent());
    }

    @Test
    void testDeleteAddressBookById_Exception() {
        when(addressBookRepository.findById(anyLong())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> addressBookService.deleteAddressBookById(1L));
    }
}
