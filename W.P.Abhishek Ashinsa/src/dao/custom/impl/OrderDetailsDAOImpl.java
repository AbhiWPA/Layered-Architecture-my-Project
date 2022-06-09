package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDetailsDAO;
import model.ItemDTO;
import model.OrderDetailDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public ArrayList<OrderDetailDTO> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM order_detail");
        ArrayList<OrderDetailDTO> allOrders = new ArrayList<>();
        while (rst.next()) {
            allOrders.add(new OrderDetailDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getBigDecimal(5)));
        }
        return allOrders;
    }

    @Override
    public boolean save(OrderDetailDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO order_detail (OrderId, CustId, ItemCode, OrderQty, total) VALUES (?,?,?,?,?)", dto.getOrderId(), dto.getCustId(), dto.getItemCode(), dto.getOrQty(), dto.getTotal());
    }

    @Override
    public boolean update(OrderDetailDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE order_detail SET CustId=?, ItemCode=?, OrderQty=?, Total=? WHERE OrderId=?", dto.getCustId(), dto.getItemCode(), dto.getOrQty(), dto.getTotal(), dto.getOrderId());
    }

    @Override
    public OrderDetailDTO search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM order_detail WHERE OrderId=?", id);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<String> getIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean updateQty(int qty, String code) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public BigDecimal getProfit() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT SUM(total) Profit FROM order_detail");
        if (rst.next()){
            BigDecimal profit = rst.getBigDecimal(1);
            return profit;
        }
        return null;
    }
}
