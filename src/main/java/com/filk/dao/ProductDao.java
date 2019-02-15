package com.filk.dao;

import com.filk.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAll();

    Product getById(int id);

    void insert(Product product);

    void update(Product product);

    void deleteById(int id);
}
