package controller;

import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CustomerDTO;
import view.tdm.CustomerTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerListFormController implements Initializable {
    public AnchorPane context;
    public TextField txtSearch;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustId;
    public TableColumn colCustName;
    public TableColumn colCustAddress;
    private final CustomerDAO customerDAO=new CustomerDAOImpl();
    public TableColumn colCity;

    public void enterKeyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomerDetails();

        colCustId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
    }

    private void setCustomerDetails() {
        tblCustomer.getItems().clear();
        try {
            ArrayList<CustomerDTO> allCustomers = customerDAO.getAll();
            for (CustomerDTO customer : allCustomers) {
                tblCustomer.getItems().add(new CustomerTM(customer.getCustId(), customer.getName(), customer.getAddress(), customer.getCity()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
