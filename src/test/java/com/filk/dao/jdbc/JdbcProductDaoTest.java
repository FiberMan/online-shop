package com.filk.dao.jdbc;

import com.filk.dao.ProductDao;
import com.filk.entity.Product;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class JdbcProductDaoTest {
    private ProductDao productDao;

    @Before
    public void before() throws IOException {


        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fileInputStream);
        }
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        productDao = new JdbcProductDao(jdbcUtils);

        // refresh database
        jdbcUtils.refreshDatabase();
    }

    @Test
    public void getAll() {
        List<Product> products = productDao.getAll();
        Product product;

        assertEquals(2, products.size());

        product = products.get(0);
        assertEquals(1, product.getId());
        assertEquals("Картошка", product.getName());
        assertEquals("Большая и сырая", product.getDescription());
        assertEquals(100.50, product.getPrice(), 0.001);

        product = products.get(1);
        assertEquals(2, product.getId());
        assertEquals("Кастрюля", product.getName());
        assertEquals("Чугунная и тяжелая", product.getDescription());
        assertEquals(555.55, product.getPrice(), 0.001);
    }

    @Test
    public void getById() {
        Product product;

        product = productDao.getById(1);
        assertEquals(1, product.getId());
        assertEquals("Картошка", product.getName());
        assertEquals("Большая и сырая", product.getDescription());
        assertEquals(100.50, product.getPrice(), 0.001);

        product = productDao.getById(2);
        assertEquals(2, product.getId());
        assertEquals("Кастрюля", product.getName());
        assertEquals("Чугунная и тяжелая", product.getDescription());
        assertEquals(555.55, product.getPrice(), 0.001);

        product = productDao.getById(3);
        assertNull(product);
    }

    @Test
    public void insert() {
        Product product = new Product();
        product.setName("Кувалда");
        product.setDescription("Очень мощная");
        product.setPrice(123.32);

        productDao.insert(product);

        List<Product> products = productDao.getAll();
        Product productNew;

        assertEquals(3, products.size());

        productNew = products.get(products.size() - 1);
        assertEquals(3, productNew.getId());
        assertEquals("Кувалда", productNew.getName());
        assertEquals("Очень мощная", productNew.getDescription());
        assertEquals(123.32, productNew.getPrice(), 0.001);
    }

    @Test
    public void update() {
        Product product = new Product();
        product.setId(2);
        product.setName("Кастрюля б/у");
        product.setDescription("Чугунная и тяжелая, но подгоревшая");
        product.setPrice(333.33);

        productDao.update(product);

        Product productUpdated = productDao.getById(2);
        assertEquals(2, productUpdated.getId());
        assertEquals("Кастрюля б/у", productUpdated.getName());
        assertEquals("Чугунная и тяжелая, но подгоревшая", productUpdated.getDescription());
        assertEquals(333.33, productUpdated.getPrice(), 0.001);

        List<Product> products = productDao.getAll();
        assertEquals(2, products.size());
    }

    @Test
    public void deleteById() {
        productDao.deleteById(1);

        List<Product> products = productDao.getAll();
        assertEquals(1, products.size());

        Product product;

        product = products.get(0);
        assertEquals(2, product.getId());
        assertEquals("Кастрюля", product.getName());
        assertEquals("Чугунная и тяжелая", product.getDescription());
        assertEquals(555.55, product.getPrice(), 0.001);
    }
}