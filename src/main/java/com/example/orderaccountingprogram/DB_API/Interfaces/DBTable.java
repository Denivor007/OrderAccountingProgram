package com.example.orderaccountingprogram.DB_API.Interfaces;

import com.example.orderaccountingprogram.DB_API.Database;

public interface DBTable {
    public void insert(Database db);
    public void update(Database db);
    public void delete(Database db);

}
