package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.ValidationUtil;
import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ItemDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddNewItemFormController implements Initializable {
    public AnchorPane context;
    public JFXTextField txtItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnAdd;
    private final NotificationController nfc = new NotificationController();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void enterKeyPressed(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAdd);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Work");
            }
        }
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
        String code = txtItemCode.getText();
        String desc = txtDescription.getText();
        String size = txtPackSize.getText();
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(txtUnitPrice.getText()));
        int qty = Integer.parseInt(txtQtyOnHand.getText());

        try {
            if (existItem(code)){
                nfc.upperErrorMessage("Failed to save Item...!","Item is already exists");
            }
            boolean save = itemDAO.save(new ItemDTO(code, desc, size, price, qty));
            if (save) {
                nfc.confirmMassage("Saved...!", "Item details saved successfully...");
                txtItemCode.clear();
                txtDescription.clear();
                txtPackSize.clear();
                txtUnitPrice.clear();
                txtQtyOnHand.clear();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            nfc.errorMassage("Failed to save...!", "Enter item details again...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            nfc.errorMassage("Failed to save...!", "Enter item details again...");
        }

    }

    boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(code);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Pattern codePattern = Pattern.compile("^(I00|I0)[0-9]{1,3}$");
        Pattern descPattern = Pattern.compile("^[A-z0-9 (),/]{3,30}$");
        Pattern sizePattern = Pattern.compile("^[0-9 ]{1,4}(kg|l|cm|m|pieces)$");
        Pattern pricePattern = Pattern.compile("^[0-9]{2,}.[0-9]{2}");
        Pattern qtyPattern = Pattern.compile("^[0-9]{1,2000}$");

        map.put(txtItemCode,codePattern);
        map.put(txtDescription,descPattern);
        map.put(txtPackSize,sizePattern);
        map.put(txtUnitPrice,pricePattern);
        map.put(txtQtyOnHand,qtyPattern);

    }
}
