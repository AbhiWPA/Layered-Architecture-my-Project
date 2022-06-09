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
import model.ItemDTO;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UpdateCustomerFormController implements Initializable {
    public AnchorPane context;
    public JFXComboBox cmbCustomerId;
    public JFXTextField txtCustName;
    public JFXTextField txtCustAddress;
    public JFXTextField txtCity;
    public JFXComboBox cmbProvince;
    public JFXTextField txtPostalCode;
    public JFXButton btnUpdate;
    NotificationController nfc = new NotificationController();
    CustomerDAO customerDAO = new CustomerDAOImpl();
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void UpdateBtnOnAction(ActionEvent actionEvent) {
        String id = (String) cmbCustomerId.getValue();
        String name = txtCustName.getText();
        String address = txtCustAddress.getText();
        String city = txtCity.getText();
        String province = (String) cmbProvince.getValue();
        String postalCode = txtPostalCode.getText();

        try {
            if (!existCustomer(id)) {
                nfc.upperErrorMessage("Can't update...!", "Customer ID "+id+ "is not available...!");
            }

            boolean update = customerDAO.update(new CustomerDTO(id, name, address, city, province, postalCode));
            if (update){
                nfc.confirmMassage("Saved...!", "Customer details saved successfully...");
                cmbCustomerId.setValue("");
                txtCustName.clear();
                txtCustAddress.clear();
                txtCity.clear();
                cmbProvince.setValue("");
                txtPostalCode.clear();
            }

        } catch (SQLException e) {
            nfc.errorMassage("Can't update...!", "Please enter values again...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    boolean existCustomer(String code) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(code);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] provinces = {"Western", "Central", "Southern", "Uva", "Sabaragamuwa", "North Western", "north Central", "Nothern", "Eastern"};
        cmbProvince.getItems().addAll(provinces);

        setCustomerIds();

        Pattern namePattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
        Pattern postalCodePattern = Pattern.compile("^[1-9]{6}$");

        map.put(txtCustName,namePattern);
        map.put(txtCustAddress,addressPattern);
        map.put(txtCity,cityPattern);
        map.put(txtPostalCode,postalCodePattern);
    }

    private void setCustomerIds() {
        try {

            ArrayList<String> ids = customerDAO.getIds();

            cmbCustomerId.getItems().addAll(ids);


            cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String id= (String) newValue;

                ArrayList<CustomerDTO> customers = null;

                try {
                    customers = customerDAO.getAllCustomersById(id);
                    for (CustomerDTO c : customers) {
                        txtCustName.setText(c.getName());
                        txtCustAddress.setText(c.getAddress());
                        txtCity.setText(c.getCity());
                        cmbProvince.setValue(c.getProvince());
                        txtPostalCode.setText(c.getPostalCode());
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
