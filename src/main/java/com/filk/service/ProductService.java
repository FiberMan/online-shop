package com.filk.service;

import com.filk.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product getById(int id);

    Product getByName(String name);

    boolean insert(Product product);

    boolean update(Product product);

    boolean deleteById(int id);
}
