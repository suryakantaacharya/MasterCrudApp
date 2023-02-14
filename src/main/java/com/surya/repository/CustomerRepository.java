package com.surya.repository;


import org.springframework.data.repository.CrudRepository;

import com.surya.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
