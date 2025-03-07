package com.example.AddressBookApp.service;

import com.example.AddressBookApp.model.Address;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor  // Eliminates the need for @Autowired
public class AddressService {

    private final List<Address> addressList = new ArrayList<>();  // In-memory list
    private final AtomicLong idGenerator = new AtomicLong(1);  // Unique ID generator

    public List<Address> getAllAddresses() {
        return new ArrayList<>(addressList);  // Returning a copy to avoid modification issues
    }

    public Address getAddressById(Long id) {
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id));
    }

    public Address addAddress(Address address) {
        address.setId(idGenerator.getAndIncrement());  // Assign unique ID
        addressList.add(address);
        return address;
    }

    public Address updateAddress(Long id, Address addressDetails) {
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .map(address -> {
                    address.setName(addressDetails.getName());
                    address.setEmail(addressDetails.getEmail());
                    address.setPhone(addressDetails.getPhone());
                    return address;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id));
    }

    public void deleteAddress(Long id) {
        boolean removed = addressList.removeIf(address -> address.getId().equals(id));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id);
        }
    }
}
