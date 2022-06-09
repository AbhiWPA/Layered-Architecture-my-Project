package dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T, ID> {
    ArrayList<T> getAll() throws SQLException, ClassNotFoundException;

    boolean save(T dto) throws SQLException, ClassNotFoundException;

    boolean update(T dto) throws SQLException, ClassNotFoundException;

    T search(ID id)throws SQLException,ClassNotFoundException;

    boolean exist(ID id) throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    String generateNewID() throws SQLException, ClassNotFoundException;

    ArrayList<String> getIds() throws SQLException, ClassNotFoundException;

    boolean updateQty(int qty, String code) throws SQLException, ClassNotFoundException;

    BigDecimal getProfit() throws SQLException, ClassNotFoundException;
}
