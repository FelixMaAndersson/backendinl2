package se.yrgo.services.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

public class CustomerManagementMockImpl implements CustomerManagementService {
	private HashMap<String,Customer> customerMap;

	public CustomerManagementMockImpl() {
		customerMap = new HashMap<String,Customer>();
		customerMap.put("OB74", new Customer("OB74" ,"Fargo Ltd", "some notes"));
		customerMap.put("NV10", new Customer("NV10" ,"North Ltd", "some other notes"));
		customerMap.put("RM210", new Customer("RM210" ,"River Ltd", "some more notes"));
	}


	@Override
	public void newCustomer(Customer newCustomer) {
        customerMap.put(newCustomer.getCustomerId(), newCustomer);
	}

	@Override
	public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException {
        String customerId = changedCustomer.getCustomerId();

        if (!customerMap.containsKey(customerId)) {
            throw new CustomerNotFoundException();
        }

        customerMap.put(customerId, changedCustomer);
	}

	@Override
    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException {
        String customerId = oldCustomer.getCustomerId();

        if (!customerMap.containsKey(customerId)) {
            throw new CustomerNotFoundException();
        }

        customerMap.remove(customerId);
    }

	@Override
	public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
		Customer foundCustomer = customerMap.get(customerId);

        if (foundCustomer == null) {
            throw new CustomerNotFoundException();
        }

		return foundCustomer;
	}

	@Override
	public List<Customer> findCustomersByName(String name) {
        List<Customer> matchingCustomers = new ArrayList<Customer>();

        for (Customer customer : customerMap.values()) {
            if (customer.getCompanyName().equals(name)) {
                matchingCustomers.add(customer);
            }
        }

        return matchingCustomers;
	}

	@Override
	public List<Customer> getAllCustomers() {
        return new ArrayList<Customer>(customerMap.values());
	}

	@Override
	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        return findCustomerById(customerId);
	}

	@Override
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        Customer foundCustomer = findCustomerById(customerId);
        foundCustomer.addCall(callDetails);
	}

}
