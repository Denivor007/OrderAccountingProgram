package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.Driver;
import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;
import com.example.orderaccountingprogram.DB_API.Order;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.util.Objects;

public class DriverInformationController extends com.example.orderaccountingprogram.InformationControllerAbstract {

    @Override
    public void update() {
        int id = ((Driver)dbTable).getId();
        String email = null;
        String name= null;
        String surname= null;
        String car_number= null;

        for (Node node: fields.getChildren()) {
            TextField textField = (TextField) node;

            if(Objects.equals(textField.getId(), "ordEmail")){
                email = textField.getText();
            }
            if(Objects.equals(textField.getId(), "ordName")){
                name = textField.getText();
            }
            if(Objects.equals(textField.getId(), "ordSurname")){
                surname = textField.getText();
            }
            if(Objects.equals(textField.getId(), "ordCar_number")){
                car_number = textField.getText();
            }

        }

        Driver driver = new Driver(
                id,
                email,
                name,
                surname,
                car_number
        );
        driver.update(db);
    }

    public void setDBTable(DBTable dbTable){
        this.dbTable = dbTable;
        assert dbTable instanceof Driver;
        Driver driver = (Driver) dbTable;

        TextField ordEmail = new TextField(driver.getEmail());
        ordEmail.setId("ordEmail");
        fields.getChildren().add(ordEmail);

        TextField ordName = new TextField(driver.getName());
        ordName.setId("ordName");
        fields.getChildren().add(ordName);

        TextField ordSurname = new TextField(driver.getSurname());
        ordSurname.setId("ordSurname");
        fields.getChildren().add(ordSurname);

        TextField ordCar_number = new TextField(driver.getCar_number());
        ordCar_number.setId("ordCar_number");
        fields.getChildren().add(ordCar_number);
    }

}
