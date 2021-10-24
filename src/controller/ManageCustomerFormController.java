package controller;

import bo.custom.CustomerBO;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;
import view.tm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ManageCustomerFormController {
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBOFactory().getBO(BOFactory.BoTypes.CUSTOMER);
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerProvince;
    public JFXTextField txtCustomerCity;
    public JFXTextField txtCustomerPostalCode;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    public TableView<CustomerTM> tblCustomers;
    public TableColumn colId;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colProvince;
    public TableColumn colCity;
    public TableColumn colPCode;
    public AnchorPane root;

    public void initialize(){
        txtCustomerId.setText(generateNewId());
        txtCustomerId.setDisable(true);

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtCustomerId.setText(newValue.getId());
                txtCustomerTitle.setText(newValue.getTitle());
                txtCustomerName.setText(newValue.getName());
                txtCustomerAddress.setText(newValue.getAddress());
                txtCustomerCity.setText(newValue.getCity());
                txtCustomerProvince.setText(newValue.getProvince());
                txtCustomerPostalCode.setText(newValue.getPostalCode());
                txtCustomerId.setDisable(true);
                btnSave.setDisable(true);
            }
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        loadAllCustomers();
    }

    private void loadAllCustomers() {
        tblCustomers.getItems().clear();
       /* Get all customers*/
        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomer();
            for (CustomerDTO customer : allCustomers) {
                tblCustomers.getItems().add(new CustomerTM(customer.getId(), customer.getTitle(), customer.getName(),customer.getAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode()));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
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

    public void btnSave_OnAction(ActionEvent actionEvent) {
        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustomerName.getText();
        String address=txtCustomerAddress.getText();
        String city=txtCustomerCity.getText();
        String province=txtCustomerProvince.getText();
        String postalCode=txtCustomerPostalCode.getText();

        try {
            if (existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, id + " already exists").show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION,  "Saved...!").show();
                clear();
                txtCustomerId.setText(generateNewId());
                CustomerDTO customerDTO = new CustomerDTO(id, title, name, address, city, province, postalCode);
                customerBO.addCustomer(customerDTO);
                tblCustomers.getItems().add(new CustomerTM(id, title, name, address, city, province, postalCode));
            }
            } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        clear();
        txtCustomerId.setText(generateNewId());
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        String id = tblCustomers.getSelectionModel().getSelectedItem().getId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted...!").show();
                customerBO.deleteCustomer(id);
                tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
                tblCustomers.getSelectionModel().clearSelection();
                clear();
                txtCustomerId.setText(generateNewId());
                btnSave.setDisable(false);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {
        String id=txtCustomerId.getText();
        String title=txtCustomerTitle.getText();
        String name=txtCustomerName.getText();
        String address=txtCustomerAddress.getText();
        String city=txtCustomerCity.getText();
        String province=txtCustomerProvince.getText();
        String postalCode=txtCustomerPostalCode.getText();

        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            CustomerDTO customerDTO = new CustomerDTO(id, title, name, address, city, province, postalCode);
            customerBO.updateCustomer(customerDTO);
            clear();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + id + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        selectedCustomer.setName(name);
        selectedCustomer.setTitle(title);
        selectedCustomer.setAddress(address);
        selectedCustomer.setCity(city);
        selectedCustomer.setProvince(province);
        selectedCustomer.setPostalCode(postalCode);
        tblCustomers.refresh();
    }

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.ifCustomerExist(id);
    }

    private String generateNewId() {
        try {
            return customerBO.generateNewID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (tblCustomers.getItems().isEmpty()) {
            return "C001";
        } else {
            String id = getLastCustomerId();
            int newCustomerId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newCustomerId);
        }
    }

    private String getLastCustomerId() {
        List<CustomerTM> tempCustomersList = new ArrayList<>(tblCustomers.getItems());
        Collections.sort(tempCustomersList);
        return tempCustomersList.get(tempCustomersList.size() - 1).getId();
    }

    private void clear() {
        txtCustomerId.clear();
        txtCustomerTitle.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerCity.clear();
        txtCustomerProvince.clear();
        txtCustomerPostalCode.clear();
    }
}
