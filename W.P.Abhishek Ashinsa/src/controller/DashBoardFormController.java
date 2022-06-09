package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dao.uiLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardFormController implements uiLoader, Initializable {
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXButton btnLogin;
    public Label lblDate;
    public Label lblTime;
    NotificationController notification=new NotificationController();

    public void loginBtnOnAction(ActionEvent actionEvent) throws IOException {
        if(txtUserName.getText().equals("Management") && txtPassword.getText().equals("Management")){
            loadUi("ManagementDashBoardForm");
            notification.confirmMassage("Login Successful...!","Welcome to the System");
        }else if (txtUserName.getText().equals("Cashier") && txtPassword.getText().equals("Cashier")) {
            loadUi("CashierDashBoardForm");
            notification.confirmMassage("Login Successful...!","Welcome to the System");
        }else{
            txtPassword.setStyle("-fx-border-color: red");
            txtUserName.setStyle("-fx-border-color: red");
            notification.errorMassage("Something Wrong...!","Please Check User Name Or Password again.");
        }
    }

    @Override
    public void loadUi(String location) throws IOException {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();

        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(parent));
        stage2.setResizable(false);
        stage2.show();
    }

    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy/MM/dd, EEEE").format(new Date()));

        Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e->{
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
    }
}
