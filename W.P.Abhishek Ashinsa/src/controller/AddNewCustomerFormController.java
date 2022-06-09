package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.ValidationUtil;
import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CustomerDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddNewCustomerFormController implements Initializable {
    public JFXButton btnAdd;
    public AnchorPane context;
    public JFXTextField txtCustId;
    public JFXTextField txtCustName;
    public JFXTextField txtCustAddress;
    public JFXTextField txtCity;
    public JFXComboBox cmbProvince;
    public JFXTextField txtPostalCode;
    private final NotificationController nfc = new NotificationController();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void addBtnOnAction(ActionEvent actionEvent) {
        String id = txtCustId.getText();
        String name = txtCustName.getText();
        String address = txtCustAddress.getText();
        String city = txtCity.getText();
        String province = (String) cmbProvince.getValue();
        String postalCode = txtPostalCode.getText();

        try {
            if (existCustomer(id)){
                nfc.upperErrorMessage("Failed to save..!","Entered customer is already exists...");
            }
            boolean save = customerDAO.save(new CustomerDTO(id, name, address, city, province, postalCode));
            if (save){
                nfc.confirmMassage("Saved...!", "Customer details saved successfully");
                txtCustId.clear();
                txtCustName.clear();
                txtCustAddress.clear();
                txtCity.clear();
                cmbProvince.setValue("");
                txtPostalCode.clear();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            nfc.errorMassage("Failed to save...!", "Enter customer details again...");
        }
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] provinces = {"Western", "Central", "Southern", "Uva", "Sabaragamuwa", "North Western", "north Central", "Nothern", "Eastern"};
        cmbProvince.getItems().addAll(provinces);

        Pattern idPattern = Pattern.compile("^(C00)[1-9]{1,3}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
        Pattern postalCodePattern = Pattern.compile("^[1-9]{6}$");

        map.put(txtCustId,idPattern);
        map.put(txtCustName,namePattern);
        map.put(txtCustAddress,addressPattern);
        map.put(txtCity,cityPattern);
        map.put(txtPostalCode,postalCodePattern);
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }
}
