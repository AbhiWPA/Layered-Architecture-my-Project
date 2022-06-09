package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.ItemDAO;
import model.ItemDTO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {


    @Override
    public ArrayList<ItemDTO> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM item");
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        while (rst.next()) {
            allItems.add(new ItemDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getBigDecimal(4), rst.getInt(5)));
        }
        return allItems;
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM item WHERE ItemCode=?", code);
    }

    @Override
    public boolean save(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO item (ItemCode, Description, PackSize, UnitPrice, QtyOnHand) VALUES (?,?,?,?,?)", dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand());
    }

    @Override
    public boolean update(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE item SET Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? WHERE ItemCode=?", dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand(), dto.getItemCode());
    }

    @Override
    public ItemDTO search(String code) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM item WHERE ItemCode=?", code);
        if (rst.next()) {
            return new ItemDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getBigDecimal(4), rst.getInt(5));
        }
        return null;
    }

    @Override
    public boolean exist(String code) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT ItemCode FROM item WHERE ItemCode=?", code).next();
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT ItemCode FROM item ORDER BY ItemCode DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("code");
            int newItemId = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newItemId);
        } else {
            return "I00-001";
        }
    }

    @Override
    public ArrayList<ItemDTO> getItemFromCode(String code) throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM item WHERE ItemCode=?", code);
        ArrayList<ItemDTO> item= new ArrayList<>();
        while (rst.next()) {
            item.add(new ItemDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getBigDecimal(4), rst.getInt(5)));
        }
        return item;
    }

    @Override
    public ArrayList<ItemDTO> getDescriptionByCode(String code) throws ClassNotFoundException, SQLException {
        ResultSet rst = SQLUtil.executeQuery("SELECT Description,UnitPrice FROM item WHERE ItemCode=?", code);
        ArrayList<ItemDTO> itemDescription= new ArrayList<>();
        while (rst.next()) {
            itemDescription.add(new ItemDTO(rst.getString(2), rst.getBigDecimal(4)));
        }
        return itemDescription;
    }

    @Override
    public ArrayList<ItemDTO> getQty(String code) throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getIds() throws SQLException, ClassNotFoundException {
        ResultSet result = SQLUtil.executeQuery("SELECT ItemCode FROM item");
        ArrayList<String> ids= new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    @Override
    public boolean updateQty(int qty, String code) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE item SET QtyOnHand=? WHERE ItemCode=?", qty, code);
    }

    @Override
    public BigDecimal getProfit() throws SQLException, ClassNotFoundException {
        return null;
    }
}
