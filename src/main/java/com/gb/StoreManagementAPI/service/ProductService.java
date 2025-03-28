package com.gb.StoreManagementAPI.service;

import com.gb.StoreManagementAPI.exception.ProductException;
import com.gb.StoreManagementAPI.model.Product;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;

@Service
public sealed interface ProductService
        permits ProductServiceImpl {

    List<Product> getAllProducts() throws ProductException;

    ResponseEntity addProduct(Product product) throws URISyntaxException, ProductException;

    Product getProductById(Integer productId) throws ProductException;

    ResponseEntity updateProduct(Integer productId, Product product) throws ProductException;

    List<Product> findByNameContaining(String name) throws ProductException;
}
