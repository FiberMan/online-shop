package com.filk.dao.jdbc;

import com.filk.dao.ProductDao;
import com.filk.dao.jdbc.mapper.ProductMapper;
import com.filk.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao implements ProductDao {
    private final String SQL_GET_ALL_PRODUCTS = "SELECT * FROM product";
    private final String SQL_GET_PRODUCT_BY_ID = "SELECT * FROM product WHERE product_id = ?";
    private final String SQL_GET_PRODUCT_BY_NAME = "SELECT * FROM product WHERE name = ?";
    private final String SQL_INSERT_PRODUCT = "INSERT INTO product (name, description, price) VALUES (?, ?, ?)";
    private final String SQL_UPDATE_PRODUCT = "UPDATE product SET name = ?, description = ?, price = ? WHERE product_id = ?";
    private final String SQL_DELETE_PRODUCT = "DELETE FROM product WHERE product_id = ?";

    private JdbcUtils jdbcUtils;

    public JdbcProductDao(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        try {
            ResultSet resultSet = jdbcUtils.select(SQL_GET_ALL_PRODUCTS);
            while (resultSet.next()) {
                products.add(ProductMapper.map(resultSet));
            }
            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: Can't get all products.", e);
        }
        return products;
    }

    @Override
    public Product getById(int id) {
        Product product = null;
        try {
            ResultSet resultSet = jdbcUtils.select(SQL_GET_PRODUCT_BY_ID, id);
            if (resultSet.next()) {
                product = ProductMapper.map(resultSet);
            }
            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: Can't get Product by ID. ID = " + id, e);
        }
        return product;
    }

    @Override
    public Product getByName(String name) {
        Product product = null;
        try {
            ResultSet resultSet = jdbcUtils.select(SQL_GET_PRODUCT_BY_NAME, name);
            if (resultSet.next()) {
                product = ProductMapper.map(resultSet);
            }
            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: Can't get Product by Name. Name = " + name, e);
        }
        return product;
    }

    @Override
    public boolean insert(Product product) {
        try {
            return !productExists(product) && jdbcUtils.update(SQL_INSERT_PRODUCT, product.getName(), product.getDescription(), product.getPrice()) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when inserting the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            return jdbcUtils.update(SQL_UPDATE_PRODUCT, product.getName(), product.getDescription(), product.getPrice(), product.getId()) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when updating the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            return jdbcUtils.update(SQL_DELETE_PRODUCT, id) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when deleting the product. Product ID = " + id, e);
        }
    }

    private boolean productExists(Product product) {
        return getById(product.getId()) != null && getByName(product.getName()) != null;
    }
}
