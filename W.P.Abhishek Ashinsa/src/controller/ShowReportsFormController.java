package controller;

import dao.CrudDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.QueryDAO;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDetailsDAOImpl;
import dao.custom.impl.QueryDAOImpl;
import db.DBConnection;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ItemDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowReportsFormController{
    public ImageView imgHome;
    public ImageView imgIncome;
    public ImageView imgMost;
    public ImageView imgLeast;
    public AnchorPane context;
    private final OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
    private final QueryDAO queryDAO = new QueryDAOImpl();
    private  final ItemDAO itemDAO = new ItemDAOImpl();

    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgHome":
                    Stage stage = (Stage) imgHome.getScene().getWindow();
                    stage.close();

                    Parent parent = FXMLLoader.load(getClass().getResource("../view/ManagementDashBoardForm.fxml"));
                    Stage stage2 = new Stage();
                    stage2.setScene(new Scene(parent));
                    stage2.setResizable(false);
                    stage2.show();
                    break;

                case "imgIncome":
                    try {
                        BigDecimal profit = orderDetailsDAO.getProfit();
                        System.out.println(profit);
                        HashMap map = new HashMap();
                        map.put("Profit",profit);
                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/OrderDetails.jasper"));
                        Connection connection = DBConnection.getDbConnection().getConnection();
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, connection);
                        JasperViewer.viewReport(jasperPrint, false);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException | JRException e) {
                        e.printStackTrace();
                    }
                    break;


                case "imgMost":

                    ArrayList<ItemDTO> items = null;
                    try {
                        HashMap map = new HashMap();
                        String itemCode = queryDAO.getMostMovableItem();
                        items = itemDAO.getItemFromCode(itemCode);
                        for (ItemDTO item : items) {
                            String Description = item.getDescription();
                            map.put("itemName",Description);
                        }
                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/MostMovebleItem.jasper"));
                        Connection connection = DBConnection.getDbConnection().getConnection();
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, connection);
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException | JRException e) {
                        e.printStackTrace();
                    }
                    break;


                case "imgLeast":
                    try {
                        HashMap map = new HashMap();
                        String itemCode = queryDAO.getLeastMovableItem();
                        items = itemDAO.getItemFromCode(itemCode);
                        for (ItemDTO item : items) {
                            String Description = item.getDescription();
                            map.put("itemName",Description);
                        }

                        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/MostMovebleItem.jasper"));
                        Connection connection = DBConnection.getDbConnection().getConnection();
                        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, connection);
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException | JRException e) {
                        e.printStackTrace();
                    }

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
}
