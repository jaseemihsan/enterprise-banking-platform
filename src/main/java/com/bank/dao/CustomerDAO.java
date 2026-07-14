package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Customer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CustomerDAO {
    
	public List<Customer> getAllCustomers() {

    List<Customer> customers = new ArrayList<>();

    String sql = "SELECT * FROM customers ORDER BY id";

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
    ) {

        while (rs.next()) {

            Customer customer = new Customer();

            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));

            customers.add(customer);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return customers;
} 

        public List<Customer> searchCustomers(String keyword) {

    List<Customer> customers = new ArrayList<>();

    String sql = """
            SELECT *
            FROM customers
            WHERE first_name LIKE ?
               OR last_name LIKE ?
               OR email LIKE ?
            ORDER BY id
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        String search = "%" + keyword + "%";

        statement.setString(1, search);
        statement.setString(2, search);
        statement.setString(3, search);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {

            Customer customer = new Customer();

            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));

            customers.add(customer);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return customers;
}


         public Customer getCustomerById(int id) {

    String sql = "SELECT * FROM customers WHERE id=?";

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
    ) {

        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {

            Customer customer = new Customer();

            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));

            return customer;

        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
    

          public boolean saveCustomer(Customer customer) {

        String sql = """
                INSERT INTO customers
                (first_name,last_name,email,phone)
                VALUES(?,?,?,?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

          public boolean updateCustomer(Customer customer) {

    String sql = """
        UPDATE customers
        SET first_name=?,
            last_name=?,
            email=?,
            phone=?
        WHERE id=?
        """;

    try (

        Connection connection = DBConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)

    ) {

        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setString(3, customer.getEmail());
        statement.setString(4, customer.getPhone());
        statement.setInt(5, customer.getId());

        return statement.executeUpdate() > 0;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return false;

   }

         public boolean deleteCustomer(int id) {

    String sql = "DELETE FROM customers WHERE id=?";

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql)
    ) {

        statement.setInt(1, id);

        return statement.executeUpdate() > 0;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return false;
  }
}
