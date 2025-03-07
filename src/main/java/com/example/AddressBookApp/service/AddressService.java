package com.example.AddressBookApp.service;

import com.example.AddressBookApp.model.Address;
import com.example.AddressBookApp.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address addressDetails) {
        return addressRepository.findById(id).map(address -> {
            address.setName(addressDetails.getName());
            address.setEmail(addressDetails.getEmail());
            address.setPhone(addressDetails.getPhone());
            return addressRepository.save(address);
        }).orElse(null);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
