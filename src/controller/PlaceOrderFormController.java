package controller;

import bo.custom.PurchaseOrderBO;
import bo.impl.BOFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.validation.ValidationUtil;
import view.tm.OrderDetailTM;
import javafx.scene.control.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceOrderFormController {
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtCustomerName;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXTextField txtDiscount;
    public Label lblId;
    public Label lblDate;
    public JFXButton btnSave;
    public TableView<OrderDetailTM> tblOrderDetails;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public AnchorPane root;

    private String orderId;
    private final PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.PURCHASE_ORDER);

    public void initialize(){
        orderId =generateNewOrderId();
        lblId.setText(orderId);
        lblDate.setText(LocalDate.now().toString());
        //lblTime.setText(LocalTime.now().toString());
        btnPlaceOrder.setDisable(true);

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();

            if (newValue != null) {
                try {
                    try {
                        if (!existCustomer(newValue + "")) {
                            //"There is no such customer associated with the id " + id
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + newValue + "").show();
                        }
                        /*Search Customer*/
                        CustomerDTO customerDTO = purchaseOrderBO.searchCustomer(newValue + "");
                        txtCustomerName.setText(customerDTO.getName());

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + newValue + "" + e).show();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQtyOnHand.setEditable(newItemCode != null);
            btnSave.setDisable(newItemCode == null);

            if (newItemCode != null) {
                try {
                    if (!existItem(newItemCode + "")) {
                        //throw new NotFoundException("There is no such item associated with the id " + code);
                    }
                    /*Find Item*/
                    ItemDTO item = purchaseOrderBO.searchItem(newItemCode + "");
                    txtDescription.setText(item.getDiscription());
                    txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                    Optional<OrderDetailTM> optOrderDetail = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getQty() : item.getQtyOnHand()) + "");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                txtDescription.clear();
                txtQtyOnHand.clear();
                txtQty.clear();
                txtUnitPrice.clear();
                txtDiscount.clear();
            }
        });

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        TableColumn<OrderDetailTM, Button> lastCol = (TableColumn<OrderDetailTM, Button>) tblOrderDetails.getColumns().get(5);

        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");

            btnDelete.setOnAction(event -> {
                tblOrderDetails.getItems().remove(param.getValue());
                tblOrderDetails.getSelectionModel().clearSelection();
                calculateTotal();
                enableOrDisablePlaceOrderButton();
            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {
            if (selectedOrderDetail != null) {
                cmbItemCode.setDisable(true);
                cmbCustomerId.setDisable(true);
                txtCustomerName.setDisable(true);
                txtDescription.setDisable(true);
                txtQtyOnHand.setDisable(true);
                txtUnitPrice.setDisable(true);
                cmbItemCode.setValue(selectedOrderDetail.getCode());
                btnSave.setText("Update");
                txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
                txtDiscount.setText(selectedOrderDetail.getDiscount() + "");
                txtQty.setText(selectedOrderDetail.getQty() + "");
            } else {
                btnSave.setText("Add");
                cmbItemCode.setDisable(false);
                cmbItemCode.getSelectionModel().clearSelection();
                txtQtyOnHand.clear();
            }
        });
        loadAllCustomerIds();
        loadAllItemCodes();
        btnSave.setDisable(true);
        storeValidations();
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) throws IOException {
        cmbCustomerId.setDisable(false);
        txtCustomerName.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        boolean b = saveOrder(orderId, LocalDate.now(), LocalTime.now(), cmbCustomerId.getValue(),
                tblOrderDetails.getItems().stream().map(tm -> new OrderDetailDTO(orderId,
                        tm.getCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList()), Double.parseDouble(lblTotal.getText()));
        if (b) {
            new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").show();
            //makePayment();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").show();
        }

        orderId = generateNewOrderId();
        lblId.setText(orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        txtQty.clear();
        calculateTotal();
        printBill();
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        btnPlaceOrder.setDisable(false);
        String itemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        double unitPrice = Double.parseDouble((txtUnitPrice.getText()));
        int qtyWant = Integer.parseInt(txtQty.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        double total = unitPrice*(qtyWant)-qtyWant*(unitPrice*(discount)/100);

        if (qtyWant<=Integer.parseInt(txtQtyOnHand.getText())){
            boolean exists = tblOrderDetails.getItems().stream().anyMatch(detail -> detail.getCode().equals(itemCode));

            if (exists) {
                OrderDetailTM orderDetailTM = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(itemCode)).findFirst().get();

                if (btnSave.getText().equalsIgnoreCase("Update")){
                    txtDescription.setDisable(false);
                    txtQtyOnHand.setDisable(false);
                    txtUnitPrice.setDisable(false);
                    orderDetailTM.setQty(qtyWant);
                    orderDetailTM.setTotal(total);
                    orderDetailTM.setDiscount(discount);
                    tblOrderDetails.getSelectionModel().clearSelection();
                } else {
                    orderDetailTM.setQty(orderDetailTM.getQty() + qtyWant);
                    total = orderDetailTM.getQty()*(unitPrice);
                    orderDetailTM.setTotal(total);
                }
                tblOrderDetails.refresh();
            } else {
                tblOrderDetails.getItems().add(new OrderDetailTM(itemCode, qtyWant, unitPrice, discount, total ));
            }
            cmbItemCode.getSelectionModel().clearSelection();
            cmbCustomerId.requestFocus();
            calculateTotal();
            enableOrDisablePlaceOrderButton();
        }else {
            new Alert(Alert.AlertType.WARNING, "Stock Out...").show();
            cmbItemCode.getSelectionModel().clearSelection();
            txtDescription.clear();
            txtQtyOnHand.clear();
            txtQty.clear();
            txtUnitPrice.clear();
            txtDiscount.clear();
        }
    }

    private void loadAllCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = purchaseOrderBO.getAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerId.getItems().add(customerDTO.getId());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printBill(){

        String orderId=lblId.getText();
        LocalDate orderDate= LocalDate.parse(lblDate.getText());
        String custId=cmbCustomerId.getSelectionModel().getSelectedItem();
        String itemCode=cmbItemCode.getSelectionModel().getSelectedItem();
        double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        int orderQty=Integer.parseInt(txtQty.getText());
        double discount=Double.parseDouble(txtDiscount.getText());
        double cost=Double.parseDouble(lblTotal.getText());

        HashMap map=new HashMap();
        map.put("orderId",orderId);
        map.put("orderDate",orderDate);
        map.put("custId",custId);
        map.put("itemCode",itemCode);
        map.put("unitPrice",unitPrice);
        map.put("orderQty",orderQty);
        map.put("discount",discount);
        map.put("cost",cost);

        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Payment_Bill.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map,DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItemCodes() {
        try {
            ArrayList<ItemDTO> all = purchaseOrderBO.getAllItems();
            for (ItemDTO dto : all) {
                cmbItemCode.getItems().add(dto.getItemCode());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.ifCustomerExist(id);
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return purchaseOrderBO.ifItemExist(code);
    }

    private void calculateTotal() {
        double total=0 ;
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            total = total+detail.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblOrderDetails.getItems().isEmpty()));
    }

    public String generateNewOrderId() {
        try {
            return purchaseOrderBO.generateNewOrderId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrder(String orderId, LocalDate orderDate, LocalTime orderTime, String customerId, List<OrderDetailDTO> orderDetails, double orderTotal) {
        try {
            OrderDTO orderDTO = new OrderDTO(orderId, orderDate, orderTime, customerId, orderDetails, orderTotal);
            return purchaseOrderBO.purchaseOrder(orderDTO);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/CashierDashBoardForm.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern qtyPattern = Pattern.compile("^[0-9]{1,5}$");
    Pattern discountPattern = Pattern.compile("^[0-9]{1,5}[.][0-9]{1,3}|[0-9]{1,5}$");

    private void storeValidations() {
        map.put(txtQty, qtyPattern);
        map.put(txtDiscount, discountPattern);
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

    private void makePayment() throws IOException {
        URL resource = getClass().getResource("../view/MakePaymentForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
