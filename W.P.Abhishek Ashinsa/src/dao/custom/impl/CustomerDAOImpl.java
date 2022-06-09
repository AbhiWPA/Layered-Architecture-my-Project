package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.CustomerDAO;
import model.CustomerDTO;
import model.ItemDTO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<CustomerDTO> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Customer");
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        while (rst.next()) {
            allCustomers.add(new CustomerDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6)));
        }
        return allCustomers;
    }

    @Override
    public boolean save(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO Customer (CustId, CustName, CustAddress, City, Province, PostalCode) VALUES (?,?,?,?,?,?)", dto.getCustId(), dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(), dto.getPostalCode());
    }


    @Override
    public boolean update(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE Customer SET CustName=?, CustAddress=?, City=?, Province=?, PostalCode=? WHERE CustId=?", dto.getName(), dto.getAddress(), dto.getCity(), dto.getProvince(), dto.getPostalCode(), dto.getCustId());
    }

    @Override
    public CustomerDTO search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Customer WHERE CustId=?", id);
        if (rst.next()) {
            return new CustomerDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),rst.getString(5), rst.getString(6));
        }
        return null;
    }


    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT CustId FROM Customer WHERE CustId=?", id).next();
    }


    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM Customer WHERE CustId=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT CustId FROM Customer ORDER BY id DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("id");
            int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
            return String.format("C00-%03d", newCustomerId);
        } else {
            return "C00-001";
        }
    }

    @Override
    public ArrayList<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT CustId FROM customer");
        ArrayList<String> ids= new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    @Override
    public boolean updateQty(int qty, String code) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public BigDecimal getProfit() throws SQLException, ClassNotFoundException {
        return null;
    }


    @Override
    public ArrayList<CustomerDTO> getAllCustomersById(String Id) throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM customer WHERE CustId=?", Id);
        ArrayList<CustomerDTO> item= new ArrayList<>();
        while (rst.next()) {
            item.add(new CustomerDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6)));
        }
        return item;
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomerNamesById(String Id) throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.executeQuery("SELECT CustName FROM customer WHERE CustId=?",Id);
        ArrayList<CustomerDTO> customerNames = new ArrayList<>();
        while (rst.next()){
            customerNames.add(new CustomerDTO(rst.getString(2)));
        }
        return customerNames;
    }
}
