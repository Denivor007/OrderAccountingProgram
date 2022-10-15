package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.Client;
import com.example.orderaccountingprogram.DB_API.Driver;
import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;
import com.example.orderaccountingprogram.DB_API.Order;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.util.Objects;

public class ClientInformationController extends com.example.orderaccountingprogram.InformationControllerAbstract {

    @Override
    public void update() {
        int id = ((Client)dbTable).getId();
        String email = null;
        String name= null;
        String surname= null;

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
        }

        Client client = new Client(
                id,
                email,
                name,
                surname
        );
        client.update(db);
    }

    public void setDBTable(DBTable dbTable){
        this.dbTable = dbTable;
        assert dbTable instanceof Driver;
        Client client = (Client) dbTable;

        TextField ordEmail = new TextField(client.getEmail());
        ordEmail.setId("ordEmail");
        fields.getChildren().add(ordEmail);

        TextField ordName = new TextField(client.getName());
        ordName.setId("ordName");
        fields.getChildren().add(ordName);

        TextField ordSurname = new TextField(client.getSurname());
        ordSurname.setId("ordSurname");
        fields.getChildren().add(ordSurname);
    }

}
