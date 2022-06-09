package controller;

import dao.custom.ItemDAO;
import dao.custom.impl.ItemDAOImpl;
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
import model.ItemDTO;
import view.tdm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageItemsFormController implements uiLoader, Initializable {
    public AnchorPane context;
    public ImageView imgAddNew;
    public ImageView imgUpdate;
    public ImageView imgDelete;
    public ImageView imgList;
    public Label lblNote;
    public ImageView imgHome;
    public AnchorPane context2;
    public TextField txtSearch;
    public TableView<ItemTM> tblItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    private final ItemDAO itemDAO = new ItemDAOImpl();

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgList":
                    loadUi("ItemListForm");
                    break;
                case "imgAddNew":
                    loadUi("AddNewItemForm");
                    break;
                case "imgUpdate":
                    loadUi("UpdateItemForm");
                    break;
                case "imgDelete":
                    loadUi("DeleteItemForm");
                    break;
                case "imgHome":
                    Stage stage = (Stage) imgHome.getScene().getWindow();
                    stage.close();

                    Parent parent = FXMLLoader.load(getClass().getResource("../view/ManagementDashBoardForm.fxml"));
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

    public void mouseEnteredOnAction(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgList":
                    lblNote.setText("View Item List");
                    break;
                case "imgAddNew":
                    lblNote.setText("Add New Item");
                    break;
                case "imgUpdate":
                    lblNote.setText("Update Item Details");
                    break;
                case "imgDelete":
                    lblNote.setText("Delete An Item");
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

    public void mouseExitOnAction(MouseEvent event) {
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
        setItemDetails();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void setItemDetails() {
        tblItem.getItems().clear();
        try {
            ArrayList<ItemDTO> allItems = itemDAO.getAll();
            for (ItemDTO item : allItems) {
                tblItem.getItems().add(new ItemTM(item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
