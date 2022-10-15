package com.example.orderaccountingprogram.DB_API;

import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager implements DBTable {

    private String email;
    private String password;

    public Manager(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Manager(ResultSet rs) throws SQLException {
        this.email = rs.getString("email");
        this.password = rs.getString("password");
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void insert(Database db) {
        //doNothing();
        return;
    }

    @Override
    public void update(Database db) {
        //doNothing();
        return;
    }

    @Override
    public void delete(Database db) {
        //doNothing();
        return;
    }
}
