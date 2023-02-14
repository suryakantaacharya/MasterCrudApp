package com.surya.service;

import java.util.List;

import com.surya.dto.CustomerDTO;
import com.surya.exception.CustomerException;

public interface CustomerService {
	public CustomerDTO getCustomer(Integer customerId) throws CustomerException;
	public List<CustomerDTO> getAllCustomers() throws CustomerException;
	public Integer addCustomer(CustomerDTO customer) throws CustomerException;
	public void updateCustomer(Integer customerId, String emailId) throws CustomerException;
	public void deleteCustomer(Integer customerId) throws CustomerException;
}
