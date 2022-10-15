package com.example.orderaccountingprogram.DB_API;

import com.example.orderaccountingprogram.DB_API.Interfaces.DBTable;

import java.sql.*;
import java.util.Formatter;

public class Order implements DBTable {
    private final String[] status_list = {"Очікується","Підтверджено","Скасовано","Сплачено"};
    private int id;
    private int driver;
    private int client;
    private String from_address;
    private String to_address;
    private int distance;
    private double price;
    private Timestamp start_trip;
    private Timestamp end_trip;
    private String status;

    public Order(int id,
                 int driver,
                 int client,
                 String from_address,
                 String to_address,
                 int distance,
                 double price,
                 Timestamp start_trip,
                 Timestamp end_trip,
                 String status) throws IllegalArgumentException {
        boolean flag = true;

        for (String s: status_list) {
            if (status == s) flag = false;
        }
        //if (flag) throw new IllegalArgumentException("Статус должен быть один из следующих: \"Очікується\",\"Підтверджено\",\"Скасовано\",\"Сплачено\", а он " + status);

        this.status = status;
        this.id = id;
        this.driver = driver;
        this.client = client;
        this.from_address = from_address;
        this.to_address = to_address;
        this.distance = distance;
        this.price = price;
        this.start_trip = start_trip;
        this.end_trip = end_trip;
    }
    public Order(ResultSet rs) throws SQLException {
        this(rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getString(4),
                rs.getString(5),
                rs.getInt(6),
                rs.getDouble(7),
                rs.getTimestamp(8),
                rs.getTimestamp(9),
                rs.getString(10));
    }

    public int getId() {
        return id;
    }

    public int getDriver() {
        return driver;
    }

    public int getClient() {
        return client;
    }

    public String getFrom_address() {
        return from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public int getDistance() {
        return distance;
    }

    public double getPrice() {
        return price;
    }

    public Timestamp getStart_trip() {
        return start_trip;
    }

    public Timestamp getEnd_trip() {
        return end_trip;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", driver=" + driver +
                ", client=" + client +
                ", from_address='" + from_address + '\'' +
                ", to_address='" + to_address + '\'' +
                ", distance=" + distance +
                ", price=" + price +
                ", start_trip=" + start_trip +
                ", end_trip=" + end_trip +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public void insert(Database db) {
        Formatter f = new Formatter();
        String pattern = "INSERT INTO orders (driver, client, from_address, to_address, distance, price, start_trip, end_trip, status) " +
                "VALUES (%d, %d, '%s', '%s', %d, %d, '%s', '%s', '%s');";
        String command = String.valueOf(f.format(pattern, driver, client, from_address, to_address, distance,
                (int) price, start_trip.toString(), end_trip.toString(), status));
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
        String pattern = "UPDATE orders" +
                " SET driver = %d, client = %d, from_address = '%s', to_address = '%s', distance = %d, price = %d, start_trip = '%s', end_trip = '%s', status = '%s'" +
                " WHERE id = " + this.id;

        String command = String.valueOf(f.format(pattern, driver, client, from_address, to_address, distance,
                (int) price, start_trip.toString(), end_trip.toString(), status));

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
        Formatter f = new Formatter();
        String pattern = "DELETE FROM orders" +
                " WHERE id = " + this.id;
        String command = pattern;
        int key;
        try (Connection conn = db.getConnection();
             PreparedStatement stat = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
        ) {
            System.out.println(command);
            stat.executeUpdate();
            System.out.println("delete is ok");
            try (ResultSet keys = stat.getGeneratedKeys()) {
                keys.next();
                key = keys.getInt("id");
                this.id = key;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static DBTable findById(Database db, int id) {
        Order ord = null;
        try (Connection conn = db.getConnection();
             Statement stat = conn.createStatement();
        ) {
            ResultSet rs = stat.executeQuery("select * from orders where id = "+ id);
            if (rs.next())
                ord = new Order(rs);
        }catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        return ord;
    }

}
