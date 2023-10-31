package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    Admin findByUsername(String username);

    Admin save(AdminDto admin);


}
