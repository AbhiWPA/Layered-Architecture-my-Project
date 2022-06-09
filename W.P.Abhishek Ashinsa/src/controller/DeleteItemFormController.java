package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.ItemDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeleteItemFormController implements Initializable {
    public AnchorPane context;
    public JFXComboBox cmbItemCode;
    public Label lblDescription;
    public Label lblPackSize;
    public Label lblUnitPrice;
    public Label lblQtyOnHand;
    public JFXButton btnDelete;
    NotificationController nfc = new NotificationController();
    private final ItemDAO itemDAO = new ItemDAOImpl();

    public void DeleteBtnOnAction(ActionEvent actionEvent) {
        String code = (String) cmbItemCode.getValue();

        try {
            if (!existItem(code)) {
                nfc.upperErrorMessage("Can't Delete...!", "There is no such item associated with the id " + code);
            }

            boolean delete = itemDAO.delete(code);
            if (delete){
                nfc.confirmMassage("Deleted", "Item details deleted successfully...");
                cmbItemCode.setValue("");
                lblDescription.setText("");
                lblPackSize.setText("");
                lblUnitPrice.setText("");
                lblQtyOnHand.setText("");
            }

        } catch (SQLException e) {
            nfc.errorMassage("Can't Delete...!", "Failed to delete the item " + code);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setItemcodes();
    }

    private void setItemcodes() {
        try {

            ArrayList<String> ids = itemDAO.getIds();

            cmbItemCode.getItems().addAll(ids);


            cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String code= (String) newValue;

                ArrayList<ItemDTO> items = null;

                try {
                    items = itemDAO.getItemFromCode(code);
                    for (ItemDTO item : items) {
                        lblDescription.setText(item.getDescription());
                        lblPackSize.setText(item.getPackSize());
                        lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                        lblQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }
}
