package com.ecommerce.library.repository;

import com.ecommerce.library.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    Address findById(long id);


    @Query(value = "SELECT * FROM addresses WHERE is_default=true AND customer_id=:id",nativeQuery = true)
    Address findByActivatedTrue(@Param("id") long id);
}