package controller;

import bo.PurchaseOrderBO;
import bo.PurchaseOrderBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.ValidationUtil;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailsDAOImpl;
import javafx.animation.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tdm.CartTM;
import view.tdm.OrderDetailTM;
import view.tdm.OrderTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceOrderFormController {
    public AnchorPane context;
    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtCustID;
    public JFXComboBox cmbCustomerID;
    public JFXTextField txtCustName;
    public JFXTextField txtCustAddress;
    public JFXTextField txtCity;
    public JFXTextField txtPostalCode;
    public JFXTextField txtProvince;
    public JFXButton btnSaveCustomer;
    public JFXComboBox cmbItemCode;
    public Label lblDescription;
    public Label lblPackSize;
    public Label lblUnitPrice;
    public Label lblQtyOnHand;
    public JFXTextField txtQty;
    public JFXButton btnAddToCart;
    public TableView<CartTM> tblCart;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colRemove;
    public Label lblTotal;
    public JFXButton btnPrintBill;
    public JFXButton btnPlaceOrder;
    public ImageView imgHome;
    public Pane pane1;
    public Label lblOrderId;

    public TableView<CustomDTO> tblCartTwo;
    public TableColumn colSecItemCode;
    public TableColumn colSecDescription;
    public TableColumn colSecUnitPrice;
    public TableColumn colSecQty;
    public TableColumn colSecTotal;
    ObservableList<CustomDTO> tmSecond = FXCollections.observableArrayList();


    NotificationController nfc = new NotificationController();
    ObservableList<CartTM> tmList = FXCollections.observableArrayList();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private String orderId;
    private final PurchaseOrderBO purchaseOrderBO = new PurchaseOrderBOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void enterKeyPressed(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddToCart);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddToCart);
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Work");
            }
        }
    }

    public void saveCustomerBtnOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setDisable(true);
        pane1.setVisible(false);
        cmbCustomerID.setVisible(true);

        String id = txtCustID.getText();
        String name = txtCustName.getText();
        String address = txtCustAddress.getText();
        String city = txtCity.getText();
        String province = txtProvince.getText();
        String postalCode = txtPostalCode.getText();

        try {
            if (existCustomer(id)){
                nfc.upperErrorMessage("Failed to save..!","Entered customer is already exists...");
                ArrayList<String> ids = customerDAO.getIds();
                cmbCustomerID.getItems().addAll(ids);
            }
            boolean save = customerDAO.save(new CustomerDTO(id, name, address, city, province, postalCode));
            if (save){
                nfc.confirmMassage("Saved...!", "Customer details saved successfully");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            nfc.errorMassage("Failed to save...!", "Enter customer details again...");
        }

    }

    public void addToCartOnAction(ActionEvent actionEvent) {
        btnPlaceOrder.setDisable(false);
        btnPrintBill.setDisable(false);
        String itemCode = (String) cmbItemCode.getValue();
        String description = lblDescription.getText();
        BigDecimal unitPrice = new BigDecimal(lblUnitPrice.getText()).setScale(2);
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);


        CartTM isExists = isExists((String) cmbItemCode.getValue());

        if (isExists != null) {
            for (CartTM temp : tmList
            ) {
                if (temp.equals(isExists)) {
                    int newQty = temp.getQty() + qty;
                    temp.setQty((temp.getQty() + qty));
                    temp.setTotal(total.add(temp.getTotal()));
                }
            }
        } else {
            Button btn = new Button("Delete");
            btn.setStyle("-fx-background-color: RED");

            CartTM tm = new CartTM(
                    itemCode,
                    description,
                    unitPrice,
                    qty,
                    total,
                    btn
            );

            CustomDTO c = new CustomDTO(
                    (String) cmbItemCode.getValue(),
                    lblDescription.getText(),
                    unitPrice,
                    qty,
                    total
            );

            btn.setOnAction(e -> {
                tmList.remove(tm);
                calculateTotal();
            });

            tmList.add(tm);
            tblCart.setItems(tmList);

            tmSecond.add(c);
            tblCartTwo.setItems(tmSecond);
        }
        tblCart.refresh();
        calculateTotal();

        enableOrDisablePlaceOrderButton();
        lblOrderId.setText(orderId);
    }

    public void printBillOnAction(ActionEvent actionEvent) {
        String orderID = lblOrderId.getText();
        String customerName = txtCustName.getText();
        BigDecimal tot = BigDecimal.valueOf(Double.parseDouble(lblTotal.getText()));

        HashMap map = new HashMap();
        map.put("orderID",orderID);
        map.put("customerName",customerName);
        map.put("Total",tot);

        //ObservableList<Cart> tableRecords = tblSecondCart.getItems();

        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/NewBillReport.jasper"));
            ObservableList<CustomDTO> tableRecords = tblCartTwo.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport,map,new JRBeanCollectionDataSource(tableRecords));
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }

        cmbCustomerID.getSelectionModel().clearSelection();
        txtCustName.clear();
        txtCustAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();

        cmbItemCode.getSelectionModel().clearSelection();
        lblDescription.setText("");
        lblPackSize.setText("");
        lblUnitPrice.setText("");
        lblQtyOnHand.setText("");
        tblCart.getItems().clear();
        txtQty.clear();

    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        String itemcode = (String) cmbItemCode.getValue();
        String customerId = (String) cmbCustomerID.getValue();
        BigDecimal total = new BigDecimal(lblTotal.getText());
        int qty = Integer.parseInt(txtQty.getText());
        String Date = lblDate.getText();
        orderId = generateNewOrderId();
        int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());

        int newQty = qtyOnHand-qty;


        try {
            boolean b1 = orderDAO.updateQty(newQty, itemcode);
            boolean b2 = orderDAO.save(new OrderDTO(orderId, Date, customerId, total));
            boolean b3 = orderDetailsDAO.save(new OrderDetailDTO(orderId, customerId, itemcode, qty, total));

            if (b1 & b2 & b3){
                nfc.confirmMassage("Saved...!", "Order has been placed successfully");
            }else{
                nfc.errorMassage("Failed to save...!", "Order has not been placed successfully");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            nfc.errorMassage("Failed to save...!", "Order has not been placed successfully");
        }

        lblOrderId.setText(orderId);
        calculateTotal();
    }

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

    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            String time = currentTime.format(dateTimeFormatter);
            lblTime.setText(time);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void initialize() {
        btnSaveCustomer.setDisable(true);
        btnPrintBill.setDisable(true);
        btnPlaceOrder.setDisable(true);
        loadDateAndTime();

        setCustomerDetails();
        setItemDetails();

        tblCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblCart.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        TableColumn<CartTM, Button> lastCol = (TableColumn<CartTM, Button>) tblCart.getColumns().get(5);

        Pattern qtyPattern = Pattern.compile("^[0-9]{0,2000}$");

        map.put(txtQty,qtyPattern);

        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");

            btnDelete.setOnAction(event -> {
                tblCart.getItems().remove(param.getValue());
                tblCart.getSelectionModel().clearSelection();
                calculateTotal();
                enableOrDisablePlaceOrderButton();
            });

            return new ReadOnlyObjectWrapper<>(btnDelete);
        });
    }

    private void setItemDetails() {
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

    private void setCustomerDetails() {
        try {

            ArrayList<String> ids = customerDAO.getIds();

            cmbCustomerID.getItems().addAll(ids);


            cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String id= (String) newValue;

                ArrayList<CustomerDTO> customers = null;

                try {
                    customers = customerDAO.getAllCustomersById(id);
                    for (CustomerDTO c : customers) {
                        txtCustName.setText(c.getName());
                        txtCustAddress.setText(c.getAddress());
                        txtCity.setText(c.getCity());
                        txtProvince.setText(c.getProvince());
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

    public void addNewCustomerBtnOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setDisable(false);
        cmbCustomerID.setVisible(false);
        pane1.setVisible(true);
    }

    private void calculateTotal() {
        BigDecimal total = new BigDecimal(0);

        for (CartTM detail : tblCart.getItems()) {
            total = total.add(detail.getTotal());
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerID.getSelectionModel().getSelectedItem() != null && !tblCart.getItems().isEmpty()));
    }

    private CartTM isExists(String itemCode) {
        for (CartTM tm : tmList
        ) {
            if (tm.getCode().equals(itemCode)) {
                return tm;
            }
        }
        return null;
    }

    public String generateNewOrderId() {
        try {
            return purchaseOrderBO.generateNewOrderID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Or001";
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }
}
