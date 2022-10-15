package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.*;
import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;
import com.example.orderaccountingprogram.Interfaces.DataReceiver;
import com.example.orderaccountingprogram.Interfaces.DataSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /*FXML objects*/
    @FXML
    private VBox WorkField;
    @FXML
    private Label TableCallBtn;
    @FXML
    private Label ChartCallBtn;

    @FXML
    private Label DriversCallBtn;

    @FXML
    private Label ClientsCallBtn;
    //buttons
    @FXML
    private Button refreshBtn;
    @FXML
    private Button clearS;
    @FXML
    private Button clearE;
    @FXML
    private Pane adminEmail;
    //filters
    @FXML
    private ChoiceBox<Driver> driverField;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;

    /*API Objects*/
    private DataReceiver currentWorkReceiver;
    private Database db;
    private DatabaseCommands dbc;


    /*init methods*/
    @Override // Ініціалізація стартових змінних (бажано перенести рядки з url, користувачем та паролем в окремий файл config)
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);
        this.dbc = new DatabaseCommands(this.db);
        fillTheChoiceBox();
    }

    private void fillTheChoiceBox(){
        this.driverField.getItems().add(null);
        for (Driver driver: dbc.getDrivers()) {
            this.driverField.getItems().add(driver);
        }
    }


    /*filters method*/
    private ArrayList<? extends DBTable> createData(){
        Driver driver = driverField.getValue();
        if (driver == null && startField.getValue() == null && endField.getValue() == null)
            return this.dbc.getOrders();
        if (driver != null && startField.getValue() == null && endField.getValue() == null)
            return this.dbc.getOrders(driver);

        if (startField.getValue() == null && endField.getValue() != null) {
            LocalDate endDate = endField.getValue();
            Instant instant = Instant.from(endDate.atStartOfDay(ZoneId.systemDefault()));
            Date end = Date.from(instant);
            if (driver!=null)
                return this.dbc.getOrders(driver, new Timestamp(0), new Timestamp(end.getTime()));
            return this.dbc.getOrders(new Timestamp(0), new Timestamp(end.getTime()));
        }
        if (startField.getValue() != null && endField.getValue() == null) {
            LocalDate startDate = startField.getValue();
            Instant instant = Instant.from(startDate.atStartOfDay(ZoneId.systemDefault()));
            Date start = Date.from(instant);
            if (driver!=null)
                return this.dbc.getOrders(driver,new Timestamp(start.getTime()), new Timestamp(Long.MAX_VALUE/200000));
            return this.dbc.getOrders(new Timestamp(start.getTime()), new Timestamp(Long.MAX_VALUE/200000));
        }
        if (startField.getValue() != null && endField.getValue() != null) {
            LocalDate startDate = startField.getValue();
            Instant instant = Instant.from(startDate.atStartOfDay(ZoneId.systemDefault()));
            Date start = Date.from(instant);
            LocalDate endDate = endField.getValue();
            instant = Instant.from(endDate.atStartOfDay(ZoneId.systemDefault()));
            Date end = Date.from(instant);
            if (driver!=null)
                return this.dbc.getOrders(driver,new Timestamp(start.getTime()),new Timestamp(end.getTime()));
            return this.dbc.getOrders(new Timestamp(start.getTime()), new Timestamp(end.getTime()));
        }

        return null;
    }


    @FXML
    public void clearTime(ActionEvent actionEvent){
        Button clicked = (Button) actionEvent.getSource();
        if (Objects.equals(clicked.getId(), "clearS"))
            startField.setValue(null);
        if (Objects.equals(clicked.getId(), "clearE"))
            endField.setValue(null);

    }
    @FXML
    public void sendDataToTable(){
        DBTableDataSender ods = new DBTableDataSender(this.currentWorkReceiver, createData());
        try {
            ods.sendData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void viewWorkStation(String fxmlFile){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlFile));
        try {
            VBox vBox = fxmlLoader.load();
            this.currentWorkReceiver = fxmlLoader.getController();
            for (Node node : this.WorkField.getChildren()) {
                System.out.println(node.getId());
                if(Objects.equals(node.getId(), "WorkFieldVBox")) {
                    this.WorkField.getChildren().remove(node);
                    break;
                }
            }

            this.WorkField.getChildren().add(vBox);
            sendDataToTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void viewtable(){
        viewWorkStation("OrderTable.fxml");
    }
    @FXML
    public void viewclient() {
        viewWorkStation("ClientTable.fxml");
    }
    @FXML
    public void viewdriver() {
        viewWorkStation("DriverTable.fxml");
    }
    @FXML
    public void viewchart() {
        viewWorkStation("OrderChart.fxml");
    }


}

class DBTableDataSender implements DataSender {

    DataReceiver dataReceiver;
    ArrayList<? extends DBTable> data;

    public DBTableDataSender(DataReceiver dataReceiver, ArrayList<? extends DBTable> data) {
        this.data = data;
        this.dataReceiver = dataReceiver;
    }

    @Override
    public void sendData() throws IOException {
        this.dataReceiver.receiveData(this.data);
    }
}