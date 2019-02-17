package com.filk.service.impl;

import com.filk.dao.ProductDao;
import com.filk.entity.Product;
import com.filk.service.ProductService;

import java.util.List;

public class DefaultProductService implements ProductService {
    private ProductDao productDao;

    public DefaultProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product getById(int id) {
        return productDao.getById(id);
    }

    @Override
    public Product getByName(String name) {
        return productDao.getByName(name);
    }

    @Override
    public boolean insert(Product product) {
        return productDao.insert(product);
    }

    @Override
    public boolean update(Product product) {
        return productDao.update(product);
    }

    @Override
    public boolean deleteById(int id) {
        return productDao.deleteById(id);
    }
}
