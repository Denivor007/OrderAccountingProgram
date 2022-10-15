package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.DBConfig;
import com.example.orderaccountingprogram.DB_API.Database;
import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class InformationControllerAbstract implements Initializable {

    @FXML
    protected Button deleteBtn;

    @FXML
    protected HBox fields;

    @FXML
    protected Button saveBtn;

    @FXML
    protected Button updateBtn;

    @FXML
    protected TextFlow textFlow;

    protected DBTable dbTable;
    protected Database db;

    public void delete(){
        dbTable.delete(db);
    }

    public abstract void update();

    public void setText(String string){
        Text text = new Text(string);
        textFlow.getChildren().add(text);
    }

    public abstract void setDBTable(DBTable dbTable);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);
        textFlow = new TextFlow();
    }
}
