package controller;

import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.uiLoader;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CustomerDTO;
import view.tdm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageCustomerDetailsFormController implements uiLoader, Initializable {
    public AnchorPane context;
    public ImageView imgHome;
    public ImageView imgList;
    public ImageView imgAddCustomer;
    public ImageView imgUpdateCustomer;
    public ImageView imgDeleteCustomer;
    public Label lblNote;
    public AnchorPane context2;
    public TextField txtSearch;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgList":
                    loadUi("CustomerListForm");
                    break;
                case "imgAddCustomer":
                    loadUi("AddNewCustomerForm");
                    break;
                case "imgUpdateCustomer":
                    loadUi("UpdateCustomerForm");
                    break;
                case "imgDeleteCustomer":
                    loadUi("DeleteCustomerForm");
                    break;
                case "imgHome":
                    Stage stage = (Stage) imgHome.getScene().getWindow();
                    stage.close();

                    Parent parent = FXMLLoader.load(getClass().getResource("../view/CashierDashBoardForm.fxml"));
                    Stage stage2 = new Stage();
                    stage2.setScene(new Scene(parent));
                    stage2.setResizable(false);
                    stage2.show();
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.context.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgList":
                    lblNote.setText("Show Available Customers List");
                    break;
                case "imgAddCustomer":
                    lblNote.setText("Add New Customer");
                    break;
                case "imgUpdateCustomer":
                    lblNote.setText("Update Customer Details");
                    break;
                case "imgDeleteCustomer":
                    lblNote.setText("Delete a Customer");
                    break;
                case "imgHome":
                    lblNote.setText("Select One of above");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblNote.setText("Select One of above");
        }
    }

    @Override
    public void loadUi(String location) throws IOException {
        context2.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        context2.getChildren().add(parent);
    }

    public void enterKeyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomerDetails();

        colCustId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
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
