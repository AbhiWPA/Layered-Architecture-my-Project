package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.ValidationUtil;
import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CustomerDTO;
import model.ItemDTO;
import view.tdm.CustomerTM;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.MarshalledObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateItemFormController implements Initializable {
    public AnchorPane context;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnUpdate;
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final NotificationController nfc = new NotificationController();
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void UpdateBtnOnAction(ActionEvent actionEvent) {
        String code = (String) cmbItemCode.getValue();
        String description = txtDescription.getText();
        String size = txtPackSize.getText();
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(txtUnitPrice.getText()));
        int qty = Integer.parseInt(txtQtyOnHand.getText());
        
        try {
            if (!existCustomer(code)) {
                nfc.upperErrorMessage("Can't update...!", "Item code "+code+ "is not available...!");
            }

            boolean update = itemDAO.update(new ItemDTO(code, description, size, price, qty));
            if (update){
                nfc.confirmMassage("Saved...!", "Item details saved successfully...");
                cmbItemCode.setValue("");
                txtDescription.setText("");
                txtPackSize.clear();
                txtUnitPrice.clear();
                txtQtyOnHand.clear();
            }

        } catch (SQLException e) {
            nfc.errorMassage("Can't update...!", "Please enter values again...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }

    public void enterKeyPressed(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnUpdate);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnUpdate);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Work");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setItemCodes();

        Pattern descPattern = Pattern.compile("^[A-z0-9 (),/]{3,30}$");
        Pattern sizePattern = Pattern.compile("^[1-9 ]{1,4}(kg|l|cm|m|pieces)$");
        Pattern pricePattern = Pattern.compile("^[0-9]{2,}.[0-9]{2}");
        Pattern qtyPattern = Pattern.compile("^[0-9]{1,2000}$");

        map.put(txtDescription,descPattern);
        map.put(txtPackSize,sizePattern);
        map.put(txtUnitPrice,pricePattern);
        map.put(txtQtyOnHand,qtyPattern);
    }

    private void setItemCodes() {
        try {

            ArrayList<String> ids = itemDAO.getIds();

            cmbItemCode.getItems().addAll(ids);


            cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String code= (String) newValue;

                ArrayList<ItemDTO> items = null;

                try {
                    items = itemDAO.getItemFromCode(code);
                    for (ItemDTO item : items) {
                        txtDescription.setText(item.getDescription());
                        txtPackSize.setText(item.getPackSize());
                        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                        txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
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
}
