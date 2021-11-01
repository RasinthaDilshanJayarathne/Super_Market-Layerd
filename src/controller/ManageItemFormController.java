package controller;

import bo.custom.ItemBO;
import bo.impl.BOFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dto.ItemDTO;
import util.validation.ValidationUtil;
import view.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ManageItemFormController {
    public JFXButton btnAddNewCustomer;
    public JFXTextField txtItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public AnchorPane root;

    private final ItemBO itemBO = (ItemBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.ITEM);
    public JFXButton btnUpdate;

    public void initialize(){

        txtItemCode.setText(generateNewId());
        txtItemCode.setDisable(true);

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtItemCode.setText(newValue.getItemCode());
                txtDescription.setText(newValue.getDiscription());
                txtPackSize.setText(newValue.getPackSize());
                txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
                txtItemCode.setDisable(true);
                btnSave.setDisable(true);
            }
        });

        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("discription"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadAllItems();
        btnSave.setDisable(true);
        storeValidations();
    }

    private void loadAllItems() {
        tblItem.getItems().clear();
        try {
            ArrayList<ItemDTO> allItems = itemBO.getAllItems();
            for (ItemDTO item : allItems) {
                tblItem.getItems().add(new ItemTM(item.getItemCode(), item.getDiscription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand()));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/AdminDashBoardForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        String code = tblItem.getSelectionModel().getSelectedItem().getItemCode();
        try {
            if (!existItem(code)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + code).show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!").show();
                itemBO.deleteItem(code);
                tblItem.getItems().remove(tblItem.getSelectionModel().getSelectedItem());
                tblItem.getSelectionModel().clearSelection();
                clear();
                btnSave.setDisable(false);
                txtItemCode.setText(generateNewId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the item " + code).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        try {
            String itemId = txtItemCode.getText();
            String description = txtDescription.getText();
            String packSize = txtPackSize.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            if (existItem(itemId)) {
                new Alert(Alert.AlertType.ERROR, itemId + " Already Exists").show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Saved...!").show();
                clear();
                ItemDTO dto = new ItemDTO(itemId, description, packSize, unitPrice, qtyOnHand);
                itemBO.addItem(dto);
                tblItem.getItems().add(new ItemTM(itemId, description, packSize, unitPrice, qtyOnHand));
                txtItemCode.setText(generateNewId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {
        String itemId = txtItemCode.getText();
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            if (!existItem(itemId)) {
                new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + itemId).show();
            }else {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated...!").show();
                clear();
                btnSave.setDisable(false);
                txtItemCode.setText(generateNewId());

                ItemDTO dto = new ItemDTO(itemId, description, packSize, unitPrice, qtyOnHand);
                itemBO.updateItem(dto);
                ItemTM selectedItem = tblItem.getSelectionModel().getSelectedItem();
                selectedItem.setDiscription(description);
                selectedItem.setPackSize(packSize);
                selectedItem.setQtyOnHand(qtyOnHand);
                selectedItem.setUnitPrice(unitPrice);
                tblItem.refresh();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemBO.ifItemExist(code);
    }

    private String generateNewId() {
        try {
            return itemBO.generateNewID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "I001";
    }

    public void clear(){
        txtItemCode.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern discriptionPattern = Pattern.compile("^[A-z ]{1,}$");
    Pattern packSizePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern unitPricePattern = Pattern.compile("^[0-9]{1,5}[.][0-9]{1,3}$");
    Pattern qtyOnHandPattern = Pattern.compile("^[0-9]{1,5}$");

    private void storeValidations() {
        map.put(txtDescription, discriptionPattern);
        map.put(txtPackSize, packSizePattern);
        map.put(txtUnitPrice, unitPricePattern);
        map.put(txtQtyOnHand, qtyOnHandPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnSave);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }
}
