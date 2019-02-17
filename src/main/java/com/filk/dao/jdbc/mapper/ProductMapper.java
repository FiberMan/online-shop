package com.filk.dao.jdbc.mapper;

import com.filk.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {
    public static Product map(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        product.setId(resultSet.getInt("product_id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));

        return product;
    }
}
