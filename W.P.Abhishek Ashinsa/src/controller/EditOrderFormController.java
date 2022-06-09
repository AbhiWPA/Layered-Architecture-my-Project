package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailsDAOImpl;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
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
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;
import view.tdm.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditOrderFormController implements Initializable {
    public AnchorPane context;
    public ImageView imgHome;
    public JFXComboBox cmbOrderId;
    public JFXTextField txtQty;
    public JFXTextField txtDiscount;
    public JFXComboBox cmbCustomerId;
    public Label lblCustomerName;
    public JFXComboBox cmbItemCode;
    public Label lblDescription;
    public Label lblTotal;
    public JFXButton btnSaveEdit;
    public JFXButton btnCancel;
    public Label lblCustomerId;
    public Label lblItemCode;
    public Label lblDiscount;
    public Label lblQty;
    public TableView<OrderDetailTM> tblOrderDetail;
    public TableColumn colOrderId;
    public TableColumn colCustomerId;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colTotal;
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    public TableColumn colCustomerName;
    public JFXTextField txtUnitPrice;
    public TableColumn colRemove;
    public Label lblOrderId;
    public Label lblCustId;
    public Label lblTot;
    public JFXButton btnDelete;
    NotificationController nfc = new NotificationController();

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
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
                case "imgHome":
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
        }
    }

    public void enterKeyPressed(KeyEvent keyEvent) {
    }

    public void saveEditsBtnOnAction(ActionEvent actionEvent) {
        String orID = lblOrderId.getText();
        String customerID = (String) cmbCustomerId.getValue();
        String itemCode = (String) cmbItemCode.getValue();
        int qty = Integer.parseInt(txtQty.getText());
        int oldQty = Integer.parseInt(lblQty.getText());
        BigDecimal total = calculateTotal();
        int newQty = 0;
        int qtyOnHand = 0;
        ArrayList<ItemDTO> itemFromCode=null;




        try {
            itemFromCode = itemDAO.getItemFromCode(itemCode);
            for (ItemDTO item : itemFromCode) {
                qtyOnHand=item.getQtyOnHand();
            }
            boolean update = orderDAO.update(new OrderDTO(orID,customerID, total));

            if (qty > oldQty){
                int temp = qty - oldQty;
                newQty=oldQty+temp;
                int qty1=qtyOnHand-newQty;
                boolean b1 = itemDAO.updateQty(qty1, itemCode);
                boolean update1 = orderDetailsDAO.update(new OrderDetailDTO(orID, customerID, itemCode, newQty, total));

                if (b1 & update & update1){
                    nfc.confirmMassage("Saved...!", "Order details saved successfully...");
                }
            }else{
                newQty=oldQty+qty;
                int qty2 = qtyOnHand - oldQty;
                boolean update1 = orderDetailsDAO.update(new OrderDetailDTO(orID, customerID, itemCode, newQty, total));
                boolean b2 = itemDAO.updateQty(qty2, itemCode);

                if (b2 & update & update1){
                    nfc.confirmMassage("Saved...!", "Order details saved successfully...");
                }
            }
        } catch (SQLException e) {
            nfc.errorMassage("Can't update...!", "Please enter values again...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        lblTotal.setText(String.valueOf(total));
        setOrderDetails();
    }

    private BigDecimal calculateTotal() {
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText());
        int QTY = Integer.parseInt(txtQty.getText());

        BigDecimal newTotal = unitPrice.multiply(new BigDecimal(QTY)).setScale(2);

        return newTotal;
    }

    public void cancelBtnOnAction(ActionEvent actionEvent) {
        cmbItemCode.setValue("");
        cmbCustomerId.setValue("");
        lblCustomerName.setText("");
        lblDescription.setText("");
        txtUnitPrice.clear();
        txtQty.clear();
        lblTotal.setText("");

        lblOrderId.setText("");
        lblCustId.setText("");
        lblItemCode.setText("");
        lblTot.setText("");

        btnSaveEdit.setDisable(true);
        btnDelete.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblQty.setVisible(false);
        btnSaveEdit.setDisable(true);
        btnDelete.setDisable(true);
        customerDetails();
        setOrderDetails();
        itemDetails();

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("CustId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("orQty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblOrderDetail.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnSaveEdit.setDisable(false);
                btnDelete.setDisable(false);

                lblOrderId.setText(newValue.getOrderId());
                lblCustId.setText(newValue.getCustId());
                lblItemCode.setText(newValue.getItemCode());
                lblTot.setText(String.valueOf(newValue.getTotal()));
                lblQty.setText(String.valueOf(newValue.getOrQty()));

                cmbCustomerId.setValue(newValue.getCustId());
                cmbItemCode.setValue(newValue.getItemCode());
                txtQty.setText(String.valueOf(newValue.getOrQty()));
                lblTotal.setText(String.valueOf(newValue.getTotal()));
            }
        });
    }

    private void itemDetails() {
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
                        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
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

    private void customerDetails() {
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

    private void setOrderDetails() {
        tblOrderDetail.getItems().clear();
        try {
            ArrayList<OrderDetailDTO> allItems = orderDetailsDAO.getAll();
            for (OrderDetailDTO order : allItems) {
                tblOrderDetail.getItems().add(new OrderDetailTM(order.getOrderId(), order.getCustId(), order.getItemCode(), order.getOrQty(), order.getTotal()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void deletebtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "This will delete permanently!, Do You Want Delete?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType=alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            try {
                boolean delete1 = orderDAO.delete(lblOrderId.getText());
                boolean delete2 = orderDetailsDAO.delete(lblOrderId.getText());
                if (delete1 & delete2){
                    nfc.confirmMassage("Deleted...!", "Order details deleted successfully");
                }else {
                    nfc.errorMassage("Can't Delete...!", "Try again later");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                nfc.errorMassage("Can't Delete...!", "Try again later"+e);
                e.printStackTrace();
            }
            lblOrderId.setText("");
            lblCustId.setText("");
            lblItemCode.setText("");
            lblTot.setText("");

            cmbCustomerId.setValue("");
            cmbItemCode.setValue("");
            lblCustomerName.setText("");
            lblDescription.setText("");
            txtUnitPrice.clear();
            txtQty.clear();

            btnSaveEdit.setDisable(true);
            btnDelete.setDisable(true);
        }
        setOrderDetails();

    }
}
