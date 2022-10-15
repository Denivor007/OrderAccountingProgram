package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;
import com.example.orderaccountingprogram.DB_API.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderingInformationController extends com.example.orderaccountingprogram.InformationControllerAbstract {

    @Override
    public void update() {
        int id = ((Order)dbTable).getId();
        int driver = 0;
        int client= 0;
        String from_address= null;
        String to_address= null;
        int distance= 0;
        double price= 0;
        Timestamp start_trip= null;
        Timestamp end_trip= null;
        String status = null;

        for (Node node: fields.getChildren()) {
            TextField textField = (TextField) node;
            if(Objects.equals(textField.getId(), "ordDriver")){
                driver = Integer.parseInt(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordClient")){
                client = Integer.parseInt(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordDistance")){
                distance = Integer.parseInt(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordPrice")){
                price = Double.parseDouble(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordFrom_address")){
                from_address = textField.getText();
            }
            if(Objects.equals(textField.getId(), "ordTo_address")){
                to_address = textField.getText();
            }
            if(Objects.equals(textField.getId(), "ordStart_trip")){
                start_trip = Timestamp.valueOf(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordEnd_trip")){
               end_trip = Timestamp.valueOf(textField.getText());
            }
            if(Objects.equals(textField.getId(), "ordStatus")){
                status = textField.getText();
            }
        }

        Order order = new Order(
                id,
                driver,
                client,
                from_address,
                to_address,
                distance,
                price,
                start_trip,
                end_trip,
                status
        );
        order.update(db);
    }

    public void setDBTable(DBTable dbTable){
        this.dbTable = dbTable;
        assert dbTable instanceof Order;
        Order order = (Order) dbTable;

        TextField ordDriver = new TextField(String.valueOf(order.getDriver()));
        ordDriver.setId("ordDriver");
        fields.getChildren().add(ordDriver);

        TextField ordClient = new TextField(String.valueOf(order.getClient()));
        ordClient.setId("ordClient");
        fields.getChildren().add(ordClient);


        TextField ordDistance = new TextField(String.valueOf(order.getDistance()));
        ordDistance.setId("ordDistance");
        fields.getChildren().add(ordDistance);


        TextField ordPrice = new TextField(String.valueOf(order.getPrice()));
        ordPrice.setId("ordPrice");
        fields.getChildren().add(ordPrice);


        TextField ordFrom_address = new TextField(order.getFrom_address());
        ordFrom_address.setId("ordFrom_address");
        fields.getChildren().add(ordFrom_address);


        TextField ordTo_address = new TextField(order.getTo_address());
        ordTo_address.setId("ordTo_address");
        fields.getChildren().add(ordTo_address);


        TextField ordStart_trip = new TextField(order.getStart_trip().toString());
        ordStart_trip.setId("ordStart_trip");
        fields.getChildren().add(ordStart_trip);


        TextField ordEnd_trip = new TextField(order.getEnd_trip().toString());
        ordEnd_trip.setId("ordEnd_trip");
        fields.getChildren().add(ordEnd_trip);


        TextField ordStatus = new TextField(order.getStatus());
        ordStatus.setId("ordStatus");
        fields.getChildren().add(ordStatus);
    }

}
