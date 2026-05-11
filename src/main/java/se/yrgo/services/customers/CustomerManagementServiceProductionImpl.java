package se.yrgo.services.customers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.yrgo.dataaccess.CustomerDao;
import se.yrgo.dataaccess.RecordNotFoundException;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

@Service
public class CustomerManagementServiceProductionImpl implements CustomerManagementService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional
    public void newCustomer(Customer newCustomer) {
        customerDao.create(newCustomer);
    }

    @Override
    @Transactional
    public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException {
        try {
            customerDao.update(changedCustomer);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException {
        try {
            customerDao.delete(oldCustomer);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        try {
            return customerDao.getById(customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findCustomersByName(String name) {
        return customerDao.getByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        try {
            return customerDao.getFullCustomerDetail(customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    @Transactional
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try {
            customerDao.addCall(callDetails, customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }
}
