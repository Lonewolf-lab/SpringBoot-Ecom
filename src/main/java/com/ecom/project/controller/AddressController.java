package com.ecom.project.controller;

import com.ecom.project.model.User;
import com.ecom.project.payload.AddressDTO;
import com.ecom.project.repositories.AddressRepository;
import com.ecom.project.service.AddressService;
import com.ecom.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressRepository addressRepository;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> addressDTOS = addressService.getAllAddresses();
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTOS = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOS = addressService.getAddressesByUser(user);
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@Valid @RequestBody AddressDTO addressDTO,
                                                    @PathVariable Long addressId){
        AddressDTO updatedAddress = addressService.updateAddress(addressDTO, addressId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String deletedstatus = addressService.deleteAddress(addressId);

        return new ResponseEntity<String>(deletedstatus, HttpStatus.OK);
    }
}
