package com.filk.dao.jdbc;

import com.filk.dao.ProductDao;
import com.filk.dao.jdbc.mapper.ProductRowMapper;
import com.filk.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class JdbcProductDao implements ProductDao {
    private final String GET_ALL_PRODUCTS_SQL = "SELECT * FROM product";
    private final String GET_PRODUCT_BY_ID_SQL = "SELECT * FROM product WHERE product_id = ?";
    private final String GET_PRODUCT_BY_NAME_SQL = "SELECT * FROM product WHERE name = ?";
    private final String INSERT_PRODUCT_SQL = "INSERT INTO product (name, description, price) VALUES (?, ?, ?)";
    private final String UPDATE_PRODUCT_SQL = "UPDATE product SET name = ?, description = ?, price = ? WHERE product_id = ?";
    private final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE product_id = ?";

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
        List<Product> products = jdbcTemplate.query(GET_PRODUCT_BY_ID_SQL, new ProductRowMapper(), id);
        if(products.size() == 0) {
            return null;
        }
        return products.get(0);
    }

    @Override
    public Product getByName(String name) {
        List<Product> products = jdbcTemplate.query(GET_PRODUCT_BY_NAME_SQL, new ProductRowMapper(), name);
        if(products.size() == 0) {
            return null;
        }
        return products.get(0);
    }

    @Override
    public boolean insert(Product product) {
        try {
            return !productExists(product) && jdbcTemplate.update(INSERT_PRODUCT_SQL, product.getName(), product.getDescription(), product.getPrice()) > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL Exception when inserting the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            return jdbcTemplate.update(UPDATE_PRODUCT_SQL, product.getName(), product.getDescription(), product.getPrice(), product.getId()) > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL Exception when updating the product. Product name = " + product.getName(), e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            return jdbcTemplate.update(DELETE_PRODUCT_SQL, id) > 0;
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL Exception when deleting the product. Product ID = " + id, e);
        }
    }

    private boolean productExists(Product product) {
        return getById(product.getId()) != null && getByName(product.getName()) != null;
    }
}
