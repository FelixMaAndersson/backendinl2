package se.yrgo.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;
import se.yrgo.services.calls.CallHandlingService;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;

public class SimpleClient {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext container =
                     new ClassPathXmlApplicationContext("application.xml")) {

            CustomerManagementService customerService =
                    container.getBean(CustomerManagementService.class);
            CallHandlingService callService =
                    container.getBean(CallHandlingService.class);
            DiaryManagementService diaryService =
                    container.getBean(DiaryManagementService.class);

            String customerId = "CS03939";

            System.out.println("=== ENSURE CUSTOMER EXISTS ===");
            try {
                Customer existingCustomer = customerService.findCustomerById(customerId);
                System.out.println("Customer already exists: " + existingCustomer);
            } catch (CustomerNotFoundException e) {
                Customer newCustomer =
                        new Customer(customerId, "Acme", "acme@mail.com", "070-123456", "Good customer");
                customerService.newCustomer(newCustomer);
                System.out.println("Customer created: " + newCustomer);
            }

            System.out.println("\n=== GET ALL CUSTOMERS ===");
            List<Customer> allCustomers = customerService.getAllCustomers();
            for (Customer customer : allCustomers) {
                System.out.println(customer);
            }

            System.out.println("\n=== FIND CUSTOMER BY ID ===");
            try {
                Customer foundCustomer = customerService.findCustomerById(customerId);
                System.out.println("Found: " + foundCustomer);
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer " + customerId + " was not found");
            }

            System.out.println("\n=== RECORD CALL THROUGH CALLHANDLINGSERVICE ===");
            Call newCall = new Call("Larry Wall called from Acme Corp");

            Action action1 = new Action(
                    "Call back Larry to ask how things are going",
                    new GregorianCalendar(2016, 0, 1),
                    "rac"
            );

            Action action2 = new Action(
                    "Check our sales dept to make sure Larry is being tracked",
                    new GregorianCalendar(2016, 0, 1),
                    "rac"
            );

            List<Action> actions = new ArrayList<>();
            actions.add(action1);
            actions.add(action2);

            try {
                callService.recordCall(customerId, newCall, actions);
                System.out.println("Call recorded");
            } catch (CustomerNotFoundException e) {
                System.out.println("That customer doesn't exist");
            }

            System.out.println("\n=== GET FULL CUSTOMER DETAIL ===");
            try {
                Customer detailedCustomer = customerService.getFullCustomerDetail(customerId);
                System.out.println("Customer: " + detailedCustomer);

                for (Call call : detailedCustomer.getCalls()) {
                    System.out.println("Call: " + call);
                }
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer " + customerId + " was not found");
            }

            System.out.println("\n=== GET INCOMPLETE ACTIONS FROM DIARY ===");
            Collection<Action> incompleteActions = diaryService.getAllIncompleteActions("rac");
            for (Action next : incompleteActions) {
                System.out.println(next);
            }

            System.out.println("\n=== TEST NOT FOUND ===");
            try {
                customerService.findCustomerById("DOES_NOT_EXIST");
                System.out.println("Unexpected: customer was found");
            } catch (CustomerNotFoundException e) {
                System.out.println("Correct: missing customer threw CustomerNotFoundException");
            }
        }
    }
}