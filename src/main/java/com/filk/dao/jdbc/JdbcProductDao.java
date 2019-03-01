package com.filk.dao.jdbc;

import com.filk.dao.ProductDao;
import com.filk.dao.jdbc.mapper.ProductRowMapper;
import com.filk.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class JdbcProductDao implements ProductDao {
    private final String GET_ALL_PRODUCTS_SQL = "SELECT * FROM product";
    private final String GET_PRODUCT_BY_ID_SQL = "SELECT * FROM product WHERE product_id = ?";
    private final String GET_PRODUCT_BY_NAME_SQL = "SELECT * FROM product WHERE name = ?";
    private final String INSERT_PRODUCT_SQL = "INSERT INTO product (name, description, price) VALUES (?, ?, ?)";
    private final String UPDATE_PRODUCT_SQL = "UPDATE product SET name = ?, description = ?, price = ? WHERE product_id = ?";
    private final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE product_id = ?";

    private JdbcUtils jdbcUtils;

//    @Autowired
//    public JdbcProductDao(JdbcUtils jdbcUtils) {
//        this.jdbcUtils = jdbcUtils;
//    }
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query(GET_ALL_PRODUCTS_SQL, new ProductRowMapper());
    }

    @Override
    public Product getById(int id) {
        Product product = null;
//        try {
//            ResultSet resultSet = jdbcUtils.select(GET_PRODUCT_BY_ID_SQL, id);
//            if (resultSet.next()) {
//                product = ProductRowMapper.map(resultSet);
//            }
//            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
//        } catch (SQLException e) {
//            throw new RuntimeException("SQL Exception: Can't get Product by ID. ID = " + id, e);
//        }
        return product;
    }

    @Override
    public Product getByName(String name) {
        Product product = null;
//        try {
//            ResultSet resultSet = jdbcUtils.select(GET_PRODUCT_BY_NAME_SQL, name);
//            if (resultSet.next()) {
//                product = ProductRowMapper.map(resultSet);
//            }
//            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
//        } catch (SQLException e) {
//            throw new RuntimeException("SQL Exception: Can't get Product by Name. Name = " + name, e);
//        }
        return product;
    }

    @Override
    public boolean insert(Product product) {
        try {
            return !productExists(product) && jdbcUtils.update(INSERT_PRODUCT_SQL, product.getName(), product.getDescription(), product.getPrice()) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when inserting the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            return jdbcUtils.update(UPDATE_PRODUCT_SQL, product.getName(), product.getDescription(), product.getPrice(), product.getId()) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when updating the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            return jdbcUtils.update(DELETE_PRODUCT_SQL, id) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception when deleting the product. Product ID = " + id, e);
        }
    }

    private boolean productExists(Product product) {
        return getById(product.getId()) != null && getByName(product.getName()) != null;
    }
}
