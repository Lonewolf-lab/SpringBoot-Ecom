package com.ecom.project.service;

import com.ecom.project.model.User;
import com.ecom.project.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressesByUser(User user);

    AddressDTO updateAddress(@Valid AddressDTO addressDTO, Long addressId);

    String deleteAddress(Long addressId);
}
