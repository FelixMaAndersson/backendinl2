package se.yrgo.client;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.domain.Customer;
import se.yrgo.services.customers.CustomerManagementService;

public class SimpleClient {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        CustomerManagementService service =
                container.getBean("customerManagement", CustomerManagementService.class);

        List<Customer> customers = service.getAllCustomers();

        for (Customer customer : customers) {
            System.out.println(customer);
        }

        container.close();

    }
}