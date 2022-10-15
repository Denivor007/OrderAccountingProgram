package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.*;
import com.example.orderaccountingprogram.Interfaces.DataReceiver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import java.util.Objects;


public class TableController implements DataReceiver {
    private ObservableList<Order> OrdersData = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> OrderTable;


    @FXML
    private TableColumn<Order, Integer> idCol;

    @FXML
    private TableColumn<Order, Integer> driverCol;

    @FXML
    private TableColumn<Order, Integer> clientCol;

    @FXML
    private TableColumn<Order, Integer> distCol;

    @FXML
    private TableColumn<Order, Integer> priceCol;

    @FXML
    private TableColumn<Order, Timestamp> startCol;

    @FXML
    private TableColumn<Order, Timestamp> endCol;

    @FXML
    private TableColumn<Order, String> statCol;

    private String generateTextData(Order order){
        /*Зєднання з БД в цьому місці таким чином - це ультракостиль,
        * Воно працює, але так не має бути в программах. Мені піджали строки, тому навряд пофікшу*/
        Database db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);

        Client client = (Client) Client.findById(db,order.getClient());
        Driver driver = (Driver) Driver.findById(db,order.getDriver());
        double time = (order.getEnd_trip().getTime() - order.getStart_trip().getTime())/60000;
        String str = "ід замовлення: %d\n" +
                "__________________\n" +
                "Замовник поїздки: %s %s\n" +
                "Електронна пошта замовника: %s\n" +
                "__________________\n" +
                "Водій, що прийняв замовлення: %s %s\n" +
                "Електронна пошта водія: %s\n" +
                "Машина водія: %s\n" +
                "__________________\n" +
                "Поїдка розпочалась о %s і тривала %dхв (завершення поїздки відбулось о %s)\n" +
                "Адресою відправки було зазначено %s, адреса прибуття машини %s\n" +
                "Довжина шляху становила %d кілометрів, середня швидкість відповідно %dкм/година.\n" +
                "Ціна поїзки склала %dгрн.\n" +
                "Поїздку було %s!";
        return String.format(
                str,
                order.getId(),
                client.getName(), client.getSurname(),
                client.getEmail(),
                driver.getName(), driver.getSurname(),
                driver.getEmail(),
                driver.getCar_number(),
                order.getStart_trip().toString(),(int)time ,order.getEnd_trip().toString(),
                order.getFrom_address(), order.getTo_address(),
                order.getDistance(), order.getDistance()*60/((int)time),
                (int)order.getPrice(),
                order.getStatus()
        );
    }
    @FXML
    public void tableFieldAction(MouseEvent mouseEvent){
        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            if (mouseEvent.getClickCount() == 2){
                Order selectedOrder = (Order)(OrderTable.getSelectionModel().getSelectedItem());
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(TableController.class.getResource("OrderingInformation.fxml"));
                    VBox vbox = fxmlLoader.load();
                    Text text = new Text(generateTextData(selectedOrder));
                    text.setFont(new Font("Lucida Console", 16));
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setId("textFlow");
                    textFlow.setMinHeight(450);
                    textFlow.setPadding(new Insets(15));
                    vbox.getChildren().add(textFlow);

                    Scene scene = new Scene(vbox, 450, 550);
                    stage.setTitle("Detailed ordering information");

                    stage.setScene(scene);
                    stage.setMinWidth(450);
                    stage.setMinHeight(550);
                    stage.setResizable(false);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.show();

                    OrderingInformationController controller = fxmlLoader.getController();
                    controller.setDBTable(selectedOrder);
//

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    private void initialize() {
        data = new ArrayList<>();
        initData();
        fillTheTable();
    }

    private void fillTheTable(){
        idCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        driverCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("driver"));
        clientCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("client"));
        distCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("distance"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("price"));
        startCol.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("start_trip"));
        endCol.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("end_trip"));
        statCol.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
        // заполняем таблицу данными
        OrderTable.setItems(OrdersData);
    }
    private void initData() {
        OrdersData = FXCollections.observableArrayList(data);
    }

    ArrayList<Order> data;
    @Override
    public <T> void receiveData(ArrayList<T> smt) {
        ArrayList<Order> arr = (ArrayList<Order>) smt;
        data.clear();
        data.addAll(arr);

        initData();
        fillTheTable();

    }
}
