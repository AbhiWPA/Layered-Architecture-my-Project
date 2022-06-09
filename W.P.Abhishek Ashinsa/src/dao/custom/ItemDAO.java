package dao.custom;

import dao.CrudDAO;
import model.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;


public interface ItemDAO extends CrudDAO<ItemDTO,String> {
    public ArrayList<ItemDTO> getItemFromCode(String code)throws ClassNotFoundException, SQLException;
    public ArrayList<ItemDTO> getDescriptionByCode(String code)throws ClassNotFoundException, SQLException;
    public ArrayList<ItemDTO> getQty(String code)throws ClassNotFoundException, SQLException;
}
