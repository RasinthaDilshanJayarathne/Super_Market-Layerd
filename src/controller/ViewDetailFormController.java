package controller;

import db.DbConnection;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class ViewDetailFormController {
    public AnchorPane root;
    public ImageView imgCustomer;
    public ImageView imgItem;
    public ImageView imgOrders;
    public Label lblMenu;
    public Label lblDescription;

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/AdminDashBoardForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void navigate(MouseEvent event) throws IOException, JRException, SQLException, ClassNotFoundException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgCustomer":
                    try {
                        JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Customer_Reports.jrxml"));
                        JasperReport compileReport = JasperCompileManager.compileReport(design);

                        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "imgItem":
                    try {
                        JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Item_Report.jrxml"));
                        JasperReport compileReport = JasperCompileManager.compileReport(design);

                        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "imgOrders":
                    try {
                        JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Order_Report.jrxml"));
                        JasperReport compileReport = JasperCompileManager.compileReport(design);

                        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
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
                case "imgCustomer":
                    lblMenu.setText("View Customers");
                    lblDescription.setText("Click view all customers");
                    break;
                case "imgItem":
                    lblMenu.setText("View Detail");
                    lblDescription.setText("Click view all items");
                    break;
                case "imgOrders":
                    lblMenu.setText("View Orders");
                    lblDescription.setText("Click view all orders");
                    break;
                case "imgMovableItem":
                    lblMenu.setText("View Movable Items");
                    lblDescription.setText("Click view all movable items");
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
            lblMenu.setText("View Detail");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
}
