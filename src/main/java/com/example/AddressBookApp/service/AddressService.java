package com.example.AddressBookApp.service;

import com.example.AddressBookApp.model.Address;
import com.example.AddressBookApp.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor  // Replaces @Autowired for cleaner dependency injection
public class AddressService {

    private final AddressRepository addressRepository;  // Lombok injects this automatically

    public List<Address> getAllAddresses() {  // Changed method name to plural
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));
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
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));  // Instead of returning null
    }

    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found with ID: " + id);
        }
        addressRepository.deleteById(id);
    }
}
