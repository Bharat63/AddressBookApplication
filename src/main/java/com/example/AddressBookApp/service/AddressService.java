package com.example.AddressBookApp.service;

import com.example.AddressBookApp.model.Address;
import com.example.AddressBookApp.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor  // Eliminates the need for @Autowired
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id));
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address addressDetails) {
        return addressRepository.findById(id)
                .map(address -> {
                    address.setName(addressDetails.getName());
                    address.setEmail(addressDetails.getEmail());
                    address.setPhone(addressDetails.getPhone());
                    return addressRepository.save(address);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id));
    }

    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found with ID: " + id);
        }
        addressRepository.deleteById(id);
    }
}
