module com.example.orderaccountingprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.orderaccountingprogram to javafx.fxml;
    opens com.example.orderaccountingprogram.DB_API to javafx.base;
    exports com.example.orderaccountingprogram;
    exports com.example.orderaccountingprogram.Interfaces;
    opens com.example.orderaccountingprogram.Interfaces to javafx.fxml;
    opens com.example.orderaccountingprogram.DB_API.Interfaces to javafx.base;
}