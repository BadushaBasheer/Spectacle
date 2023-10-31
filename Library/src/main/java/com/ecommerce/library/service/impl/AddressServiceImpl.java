package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final CustomerService customerService;
    private final AddressRepository addressRepository;


    public AddressServiceImpl(CustomerService customerService, AddressRepository addressRepository) {
        this.customerService = customerService;
        this.addressRepository = addressRepository;
    }

//==================================ADDRESS SAVE AND UPDATE=====================================================

    @Override
    public Address save(AddressDto addressDto, String username) {
        Customer customer = customerService.findByUsername(username);
        Address address = new Address();
        return reusable(addressDto, customer, address);
    }

    public Address update(AddressDto addressDto, long id) {
        Address address = addressRepository.findById(id);
        Customer customer = address.getCustomer();
        return reusable(addressDto, customer, address);
    }

    @NotNull
    private Address reusable(AddressDto addressDto, Customer customer, Address address) {
        address.setAddressLine1(addressDto.getAddress_line_1());
        address.setAddressLine2(addressDto.getAddress_line_2());
        address.setCity(addressDto.getCity());
        address.setPincode(addressDto.getPincode());
        address.setDistrict(addressDto.getDistrict());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setCustomer(customer);
        address.set_default(false);
        return addressRepository.save(address);
    }
//========================================================================================================================
    @Override
    public Address update(AddressDto addressDto) {
        long id=addressDto.getId();
        Address address=addressRepository.findById(id);
        address.setAddressLine1(addressDto.getAddress_line_1());
        address.setAddressLine2(address.getAddressLine2());
        address.setCity(address.getCity());
        address.setPincode(address.getPincode());
        address.setDistrict(address.getDistrict());
        address.setState(address.getState());
        address.setCountry(address.getCountry());
        return addressRepository.save(address);
    }

    @Override
    public AddressDto findById(long id) {
        Address address = addressRepository.findById(id);
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setAddress_line_1(address.getAddressLine1());
        addressDto.setAddress_line_2(address.getAddressLine2());
        addressDto.setCity(address.getCity());
        addressDto.setPincode(address.getPincode());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setCustomer(address.getCustomer());
        return addressDto;
    }

    @Override
    public void deleteAddress(long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void enable(long id) {
        Address address = addressRepository.findById(id);
        address.set_default(true);
        addressRepository.save(address);

    }

    @Override
    public void disable(long id) {

        Address address = addressRepository.findById(id);
        address.set_default(false);
        addressRepository.save(address);
    }

    @Override
    public Address findDefaultAddress(long customer_id) {
        return addressRepository.findByActivatedTrue(customer_id);
    }

    @Override
    public Address findByIdOrder(long id) {
        return addressRepository.findById(id);
    }


}
