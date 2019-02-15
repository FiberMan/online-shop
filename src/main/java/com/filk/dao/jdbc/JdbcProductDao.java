package com.filk.dao.jdbc;

import com.filk.dao.ProductDao;
import com.filk.entity.Product;
import com.mysql.cj.jdbc.ConnectionWrapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao {
    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        //try(Connection connection = new )

        return products;
    }

    @Override
    public Product getById(int id) {
        return null;
    }

    @Override
    public void insert(Product product) {

    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void deleteById(int id) {

    }
}
