package se.yrgo.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {

    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_BY_ID_SQL =
            "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE CUSTOMER_ID=?";

    private static final String GET_BY_NAME_SQL =
            "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER WHERE COMPANY_NAME=?";

    private static final String UPDATE_CUSTOMER_SQL =
            "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID=?";

    private static final String DELETE_CUSTOMER_SQL =
            "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";

    private static final String GET_ALL_CUSTOMERS_SQL =
            "SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER";

    private static final String INSERT_CALL_SQL =
            "INSERT INTO CUSTOMER_CALL (TIME_AND_DATE, NOTES, CUSTOMER_ID) VALUES (?, ?, ?)";

    private static final String GET_CALLS_FOR_CUSTOMER_SQL =
            "SELECT TIME_AND_DATE, NOTES FROM CUSTOMER_CALL WHERE CUSTOMER_ID=?";

    private JdbcTemplate template;

    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template){
    	this.template = template;
    }

    @Override
    public void create(Customer customer) {
        template.update(
                INSERT_CUSTOMER_SQL,
                customer.getCustomerId(),
                customer.getCompanyName(),
                customer.getEmail(),
                customer.getTelephone(),
                customer.getNotes()
        );
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            return template.queryForObject(GET_BY_ID_SQL, new CustomerRowMapper(), customerId);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return template.query(GET_BY_NAME_SQL, new CustomerRowMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        int rowsUpdated = template.update(
                UPDATE_CUSTOMER_SQL,
                customerToUpdate.getCompanyName(),
                customerToUpdate.getEmail(),
                customerToUpdate.getTelephone(),
                customerToUpdate.getNotes(),
                customerToUpdate.getCustomerId()
        );

        if (rowsUpdated == 0) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        int rowsDeleted = template.update(
                DELETE_CUSTOMER_SQL,
                oldCustomer.getCustomerId()
        );

        if (rowsDeleted == 0) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return template.query(GET_ALL_CUSTOMERS_SQL, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = getById(customerId);

        List<Call> calls = template.query(
                GET_CALLS_FOR_CUSTOMER_SQL,
                new CallRowMapper(),
                customerId
        );

        customer.setCalls(calls);
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        getById(customerId);

        template.update(
                INSERT_CALL_SQL,
                new Timestamp(newCall.getTimeAndDate().getTime()),
                newCall.getNotes(),
                customerId
        );
    }
    private static class CallRowMapper implements RowMapper<Call> {
        @Override
        public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Call(
                    rs.getString("NOTES"),
                    rs.getTimestamp("TIME_AND_DATE")
            );
        }
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {

        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
                    rs.getString("CUSTOMER_ID"),
                    rs.getString("COMPANY_NAME"),
                    rs.getString("EMAIL"),
                    rs.getString("TELEPHONE"),
                    rs.getString("NOTES")
            );
        }
    }

    public void createTables() {
        try {
            template.update("""
            CREATE TABLE CUSTOMER (
                CUSTOMER_ID VARCHAR(20) PRIMARY KEY,
                COMPANY_NAME VARCHAR(100),
                EMAIL VARCHAR(100),
                TELEPHONE VARCHAR(30),
                NOTES VARCHAR(255)
            )
        """);

            template.update("""
            CREATE TABLE CUSTOMER_CALL (
                TIME_AND_DATE TIMESTAMP,
                NOTES VARCHAR(255),
                CUSTOMER_ID VARCHAR(20)
            )
        """);

        } catch (Exception e) {
            System.out.println("Assuming the tables already exist");
        }
    }
}
