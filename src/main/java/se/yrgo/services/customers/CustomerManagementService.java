package se.yrgo.services.customers;

import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;


public interface CustomerManagementService {

	public void newCustomer(Customer newCustomer);


    void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException;

    void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException;


	public Customer findCustomerById(String customerId) throws CustomerNotFoundException;

	public List<Customer> findCustomersByName (String name);

	public List<Customer> getAllCustomers();


	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException;

	
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException;
}
