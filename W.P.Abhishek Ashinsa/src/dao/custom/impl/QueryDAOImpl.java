package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.QueryDAO;
import model.CustomDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ArrayList<CustomDTO> searchOrderByOrderID(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("select Orders.oid,Orders.date,Orders.customerID,OrderDetails.itemCode,OrderDetails.qty,OrderDetails.unitPrice from Orders inner join OrderDetails on Orders.oid=OrderDetails.oid where Orders.oid=?;", id);
        ArrayList<CustomDTO> orderRecords = new ArrayList();
        while (rst.next()) {
            orderRecords.add(new CustomDTO(rst.getString(1), LocalDate.parse(rst.getString(2)), rst.getString(3), rst.getString(4), rst.getInt(5), rst.getBigDecimal(6)));
        }
        return orderRecords;
    }

    @Override
    public String getMostMovableItem() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT ItemCode, COUNT(ItemCode) AS most_movable FROM order_detail GROUP BY ItemCode ORDER BY most_movable DESC LIMIT 1");
        if (rst.next()){
            String itemCode = rst.getString(1);
            return itemCode;
        }
        return null;
    }

    @Override
    public String getLeastMovableItem() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT ItemCode, COUNT(ItemCode) AS least_movable FROM order_detail GROUP BY ItemCode ORDER BY least_movable ASC LIMIT 1");
        if (rst.next()){
            String itemCode = rst.getString(1);
            return itemCode;
        }
        return null;
    }
}
