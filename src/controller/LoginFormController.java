package controller;

import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Label lblError;

    public void goToLoginPage(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        String query="SELECT * FROM Login";
        PreparedStatement stm = con.prepareStatement(query);

        String UserName=txtUserName.getText();
        String Password=txtPassword.getText();

        ResultSet rst=stm.executeQuery(query);

        if (rst.next()){
            if (UserName.equals(rst.getString(2)) && Password.equals(rst.getString(1))){
                URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setTitle("Admin Form");
                window.setScene(new Scene(load));
            }else {
                lblError.setText("Enter correct username or password");
            }
        }
        if (rst.next()){
            if (UserName.equals(rst.getString(2)) && Password.equals(rst.getString(1))){
                URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setTitle("Cashier Form");
                window.setScene(new Scene(load));
            }else {
                lblError.setText("Enter correct username or password");
            }
        }
    }

    public void goToCanclePage(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }
}
