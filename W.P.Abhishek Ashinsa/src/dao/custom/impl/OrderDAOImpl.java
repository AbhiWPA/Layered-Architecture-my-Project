package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDAO;
import model.ItemDTO;
import model.OrderDTO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public ArrayList<OrderDTO> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO `orders` (ordId, orderDate, CustId, total) VALUES (?,?,?,?)", dto.getOrderId(), dto.getOrderDate(), dto.getCustomerId(), dto.getOrderTotal());
    }

    @Override
    public boolean update(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE `orders` SET CustId=?, total=? WHERE ordId=?", dto.getCustomerId(), dto.getOrderTotal(), dto.getOrderId());
    }

    @Override
    public OrderDTO search(String oID) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM `orders` WHERE ordId=?", oID);
        if (rst.next()) {
            return new OrderDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getBigDecimal(4));
        }
        return null;
    }

    @Override
    public boolean exist(String oid) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT ordId FROM `orders` WHERE ordId=?", oid).next();
    }

    @Override
    public boolean delete(String oId) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM `orders` WHERE ordId=?", oId);
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT ordId FROM `orders` ORDER BY ordId DESC LIMIT 1;");
        return rst.next() ? String.format("Or%03d", (Integer.parseInt(rst.getString("ordId").replace("Or", "")) + 1)) : "Or001";
    }

    @Override
    public ArrayList<String> getIds() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean updateQty(int qty, String code) throws SQLException, ClassNotFoundException {
       return SQLUtil.executeUpdate("UPDATE item SET QtyOnHand=? WHERE ItemCode=?",qty, code);
    }

    @Override
    public BigDecimal getProfit() throws SQLException, ClassNotFoundException {
        return null;
    }


}
