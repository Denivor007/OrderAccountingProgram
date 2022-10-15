package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.*;
import com.example.orderaccountingprogram.Interfaces.DataReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;



public class ClientTableController  implements DataReceiver {
    private ObservableList<Client> ClientsData = FXCollections.observableArrayList();

    @FXML
    private TableView<Client> ClientTable;
    @FXML
    private TableColumn<Client, Integer> idCol;
    @FXML
    private TableColumn<Client, String> emailCol;
    @FXML
    private TableColumn<Client, String> nameCol;
    @FXML
    private TableColumn<Client, String> surnameCol;

    private String generateTextData(Client driver){
        /*Зєднання з БД в цьому місці таким чином - це ультракостиль,
         * Воно працює, але так не має бути в программах. Мені піджали строки, тому навряд пофікшу*/
        Database db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);

        String str = "ід клієнта: %d\n" +
                "__________________\n" +
                "Ім'я та фамілія клієнта: %s %s\n" +
                "Електронна пошта клієнта: %s\n";
        return String.format(
                str,
                driver.getId(),
                driver.getName(), driver.getSurname(),
                driver.getEmail()
        );
    }
    @FXML
    public void tableFieldAction(MouseEvent mouseEvent){
        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            if (mouseEvent.getClickCount() == 2){
                Client selectedOrder = (Client)(ClientTable.getSelectionModel().getSelectedItem());
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(ClientTableController.class.getResource("ClientInformation.fxml"));
                    VBox vbox = fxmlLoader.load();
                    Text text = new Text(generateTextData(selectedOrder));
                    text.setFont(new Font("Lucida Console", 16));
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setId("textFlow");
                    textFlow.setMinHeight(450);
                    textFlow.setPadding(new Insets(15));
                    vbox.getChildren().add(textFlow);
                    Scene scene = new Scene(vbox, 450, 550);
                    stage.setTitle("Detailed сlient information");

                    stage.setScene(scene);
                    stage.setMinWidth(450);
                    stage.setMinHeight(550);
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.show();


                    ClientInformationController controller = fxmlLoader.getController();
                    controller.setDBTable(selectedOrder);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void initialize() {
        Database db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);
        DatabaseCommands dbc = new DatabaseCommands(db);
        data = dbc.getClients();
        initData();
        fillTheTable();
    }

    private void fillTheTable(){
        this.idCol.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        this.surnameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));



        // заполняем таблицу данными
        ClientTable.setItems(ClientsData);
    }
    private void initData() {
        ClientsData = FXCollections.observableArrayList(data);
    }

    ArrayList<Client> data;

    @Override
    public <T> void receiveData(ArrayList<T> smt) {

    }
}
