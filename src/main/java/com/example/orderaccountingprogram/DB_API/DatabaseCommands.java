package com.example.orderaccountingprogram.DB_API;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseCommands {
    Database db;

    public DatabaseCommands(Database db) {
        this.db = db;
    }

    /*функции, создающие массив заказов или массив водителей исходя из переданной SQL комманды
    * Можно обобщить до работы с DBTable*/
    protected ArrayList<Order> getOrdersForCommand(String sqlCommand){
        ArrayList<Order> res = new ArrayList<>();
        try (Statement statement = db.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery(sqlCommand);
            while (rs.next()){
                res.add( new Order(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    protected ArrayList<Driver> getDriversForCommand(String sqlCommand){
        ArrayList<Driver> res = new ArrayList<>();
        try (Statement statement = db.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery(sqlCommand);
            while (rs.next()){
                res.add( new Driver(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public ArrayList<Order> getOrders(){
        return getOrdersForCommand("select * from orders");
    }
    public ArrayList<Order> getOrders(Driver driver){
        return getOrdersForCommand("select * from orders where driver = " + driver.getId());
    }
    public ArrayList<Order> getOrders(Timestamp start, Timestamp end){
        return getOrdersForCommand("select * from orders where start_trip between '" + start.toString()
                + "' and '" + end.toString() + "'");
    }
    public ArrayList<Order> getOrders(Driver driver,Timestamp start, Timestamp end){
        return getOrdersForCommand("select * from orders where (start_trip between '" + start.toString()
                + "' and '" + end.toString() + "') and (driver = " + driver.getId() + ")");
    }
    public ArrayList<Driver> getDrivers(){
        return getDriversForCommand("select * from drivers");
    }
    public ArrayList<Manager> getManagers(){
        ArrayList<Manager> res = new ArrayList<>();
        try (Statement statement = db.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery("select * from managers");
            while (rs.next()){
                res.add( new Manager(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public ArrayList<Client> getClients(){
        ArrayList<Client> res = new ArrayList<>();
        try (Statement statement = db.getConnection().createStatement()){
            ResultSet rs = statement.executeQuery("select * from clients");
            while (rs.next()){
                res.add( new Client(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private void updateCommand(String SQLCommand){
        try (Statement statement = db.getConnection().createStatement()){
            statement.executeUpdate(SQLCommand);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOrder(){
        try (Statement statement = db.getConnection().createStatement()){
            statement.executeUpdate("select * from clients");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateClient(){

    }
    public void updateDriver(){

    }
    public void deleteOrder(){

    }
    public void deleteClient(){

    }
    public void deleteDriver(){

    }
}
