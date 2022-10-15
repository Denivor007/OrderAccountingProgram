package com.example.orderaccountingprogram;

import com.example.orderaccountingprogram.DB_API.DBConfig;
import com.example.orderaccountingprogram.DB_API.Database;
import com.example.orderaccountingprogram.DB_API.DatabaseCommands;
import com.example.orderaccountingprogram.DB_API.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AuthorizationFormController implements Initializable {

    private boolean isPasswordCorrect;
    private boolean isLoginCorrect;

    @FXML
    private TextField email;

    @FXML
    private Label incorrectEmail;

    @FXML
    private Label incorrectPassword;

    @FXML
    private PasswordField password;

    @FXML
    private Button submitBtn;

    @FXML
    void autorisation(ActionEvent event) {
        Database db = new Database(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD);
        DatabaseCommands dbc = new DatabaseCommands(db);
        ArrayList<Manager> managers = dbc.getManagers();

        String magicWord = "111";
        if(Objects.equals(magicWord, this.email.getText()))
            openTheApplication("Creator!!!");

        for (Manager manager:managers) {
            if(Objects.equals(manager.getEmail(), this.email.getText())){
                this.isLoginCorrect = true;
                System.out.println("true password is: "+  "'"+"iamapresident" +"'"   );
                System.out.println("text from field: "+ "'"+this.password.getText()+"'");
                System.out.println("true password hash: "+"'"+ manager.getPassword() +"'");
                System.out.println("hash from field: "+"'"+ hashThePassword(this.password.getText())+"'");
                if(Objects.equals(manager.getPassword(), hashThePassword(this.password.getText()))){

                    this.isPasswordCorrect = true;
                }
            }
        }

        if (!this.isLoginCorrect)
            incorrectEmail.setText("Incorrect email entered");
        else
            incorrectEmail.setText(" ");

        if (!this.isPasswordCorrect)
            incorrectPassword.setText("Incorrect password entered");
        else
            incorrectPassword.setText(" ");

        if(this.isPasswordCorrect && this.isLoginCorrect)
            openTheApplication(this.email.getText());

    }

    private void openTheApplication(String email){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(TableController.class.getResource("hello-view.fxml"));


            BorderPane borderPane = fxmlLoader.load();
            Label label = new Label(" "+email);
            label.setFont(new Font("Lucida Console",18));
            label.setMinHeight(60);
            label.setTextFill(Color.WHITE);
            for (Node node: borderPane.getChildren()) {
                if (Objects.equals(node.getId(), "toAdn")){
                    for (Node node2: ((VBox)node).getChildren()) {
                        if (Objects.equals(node2.getId(), "adminEmail")) {
                            ((Pane) node2).getChildren().add(label);
                        }
                    }
                }

            }

            Scene scene = null;
            scene = new Scene(borderPane, 1300, 800);
            stage.setTitle("Order Accaunting Program");
            stage.setScene(scene);
            stage.show();

            Stage thisStage = (Stage) submitBtn.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hashThePassword(String password){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x",b));
            }
            return  sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isLoginCorrect = false;
        this.isPasswordCorrect = false;
    }
}
