package controller;

import com.jfoenix.controls.JFXComboBox;
import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.CustomerDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeleteCustomerFormController implements Initializable {
    public AnchorPane context;
    public JFXComboBox cmbCustomerId;
    public Label lblCustomerName;
    public Label lblCustomerAddress;
    public Label lblCustomerCity;
    public Label lblCustomerProvince;
    public Label lblPostalCode;
    NotificationController nfc = new NotificationController();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    public void DeleteBtnOnAction(ActionEvent actionEvent) {
        String id = (String) cmbCustomerId.getValue();
        try {
            if (!existCustomer(id)) {
                nfc.upperErrorMessage("Can't Delete...!", "There is no such customer associated with the id"+id);
            }

            boolean delete = customerDAO.delete(id);
            if (delete){
                nfc.confirmMassage("Deleted...!", "Delete customer details successfully...");
                cmbCustomerId.setValue("");
                lblCustomerName.setText("");
                lblCustomerCity.setText("");
                lblCustomerAddress.setText("");
                lblCustomerProvince.setText("");
                lblPostalCode.setText("");
            }

        } catch (SQLException e) {
            nfc.errorMassage("Can't Delete...!", "Failed to delete the customer"+id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomerIds();
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
                        lblCustomerName.setText(c.getName());
                        lblCustomerAddress.setText(c.getAddress());
                        lblCustomerCity.setText(c.getCity());
                        lblCustomerProvince.setText(c.getProvince());
                        lblPostalCode.setText(c.getPostalCode());
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

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }
}
