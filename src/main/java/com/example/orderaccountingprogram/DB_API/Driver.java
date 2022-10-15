package com.example.orderaccountingprogram.DB_API;

import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;

import java.sql.*;
import java.util.Formatter;

public class Driver implements DBTable{
    private int id;
    private String email;
    private String name;
    private String surname;
    private String car_number;

    public Driver(int id, String email, String name, String surname, String car_number) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.car_number = car_number;
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

    public String getCar_number() {
        return car_number;
    }

    public Driver(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.email = rs.getString("email");
        this.name = rs.getString("name");
        this.surname = rs.getString("surname");
        this.car_number = rs.getString("car_number");
    }

    @Override
    public void insert(Database db) {
        Formatter f = new Formatter();
        String pattern = "INSERT INTO drivers (email, name, surname, car_number) " +
                "VALUES ('%s', '%s', '%s', '%s');";
        String command = String.valueOf(f.format(pattern, email, name, surname, car_number));
        int key;
        try (Connection conn = db.getConnection();
             PreparedStatement stat = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
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
        String pattern = "UPDATE drivers" +
                " SET email = '%s', name = '%s', surname = '%s', car_number = '%s' " +
                " WHERE id = " + this.id;
        String command = String.valueOf(f.format(pattern, email, name, surname, car_number));
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
                " WHERE driver = " + this.id;
        String command2 = "DELETE FROM drivers" +
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
        Driver driver = null;
        try (Connection conn = db.getConnection();
             Statement stat = conn.createStatement();
        ) {
            ResultSet rs = stat.executeQuery("select * from drivers where id = "+ id);
            if (rs.next())
                driver = new Driver(rs);
        }catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        return driver;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getSurname() + " (" + this.getCar_number()+")";

    }


}
