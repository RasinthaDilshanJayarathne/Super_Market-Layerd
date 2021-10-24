package dao.custom;

import entity.Customer;

import java.sql.SQLException;

public interface CustomerDAO extends CrudDAO<Customer,String>{
    boolean ifCustomerExist(String id) throws SQLException, ClassNotFoundException;
    String generateNewID() throws SQLException, ClassNotFoundException;
}
