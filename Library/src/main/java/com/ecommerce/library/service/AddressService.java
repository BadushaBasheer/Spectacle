package com.ecommerce.library.service;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.Address;

public interface AddressService {

    Address save(AddressDto addressDto, String username);

    Address update(AddressDto addressDto, long id);

    Address update(AddressDto addressDto);

    AddressDto findById(long id);

    void deleteAddress(Long id);

    void enable(long id);

    void disable(long id);

    Address findDefaultAddress(long customer_id);

    Address findByIdOrder(long id);
}
