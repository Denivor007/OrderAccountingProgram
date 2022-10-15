package com.example.orderaccountingprogram.DB_API;

import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;

import java.sql.*;
import java.util.Formatter;

public class Client implements DBTable {
    private int id;
    private String email;
    private String name;
    private String surname;

    public Client(int id, String email, String name, String surname) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public Client(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.email = rs.getString("email");
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public void insert(Database db) {
        Formatter f = new Formatter();
        String pattern = "INSERT INTO clients (email, name, surname) " +
                "VALUES ('%s', '%s', '%s');";
        String command = String.valueOf(f.format(pattern, email, name, surname));
        int key;
        try (Connection conn = db.getConnection();
             PreparedStatement stat = conn.prepareStatement(command,Statement.RETURN_GENERATED_KEYS);
        ) {
            System.out.println(command);
            stat.executeUpdate();
            System.out.println("insertion is ok");
            try (ResultSet keys = stat.getGeneratedKeys()) {
                keys.next();
                key = keys.getInt("id");
                this.id = key;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Database db) {
        Formatter f = new Formatter();
        String pattern = "UPDATE clients" +
                " SET email = '%s', name = '%s', surname = '%s'" +
                " WHERE id = " + this.id;
        String command = String.valueOf(f.format(pattern, email, name, surname));
        int key;
        try (Connection conn = db.getConnection();
             PreparedStatement stat = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
        ) {
            System.out.println(command);
            stat.executeUpdate();
            System.out.println("update is ok");
            try (ResultSet keys = stat.getGeneratedKeys()) {
                keys.next();
                key = keys.getInt("id");
                this.id = key;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Database db) {
        String command1 = "DELETE FROM orders" +
                " WHERE client = " + this.id;
        String command2 = "DELETE FROM clients" +
                " WHERE id = " + this.id;
        int key;
        try (Connection conn = db.getConnection();
             PreparedStatement stat2 = conn.prepareStatement(command2, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stat1 = conn.prepareStatement(command1, Statement.RETURN_GENERATED_KEYS);
        ) {
            System.out.println(command2);
            stat1.executeUpdate();
            stat2.executeUpdate();
            System.out.println("update is ok");
            try (ResultSet keys = stat2.getGeneratedKeys()) {
                keys.next();
                key = keys.getInt("id");
                this.id = key;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBTable findById(Database db, int id) {
        Client client = null;
        try (Connection conn = db.getConnection();
             Statement stat = conn.createStatement();
        ) {
            ResultSet rs = stat.executeQuery("select * from clients where id = "+ id);
            if (rs.next())
                client = new Client(rs);
        }catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        return client;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
