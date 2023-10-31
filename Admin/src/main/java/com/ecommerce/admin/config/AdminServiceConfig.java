package com.ecommerce.admin.config;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Repository
public class AdminServiceConfig implements UserDetailsService {

    public AdminRepository adminRepository;

    public AdminServiceConfig(AdminRepository adminRepository) {
        this.adminRepository=adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin=adminRepository.findByEmail(username);
        System.out.println(username);
        if (admin==null){
            throw  new UsernameNotFoundException("The admin profile could not be located.");
        }
        return new User(admin.getEmail(), admin.getPassword(), new ArrayList<>()
        );
    }
}
