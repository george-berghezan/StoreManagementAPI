package com.gb.StoreManagementAPI.service;

import com.gb.StoreManagementAPI.exception.ProductException;
import com.gb.StoreManagementAPI.model.Product;
import com.gb.StoreManagementAPI.repository.ProductRepository;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@Service
public final class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Override
    public List<Product> getAllProducts() throws ProductException {
        List<Product> productList = productRepository.findAll();
        if (!productList.isEmpty()) {
            return productList;
        } else {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String error = "No products found";
            logger.warn("User: {}. Error: {}", currentUser.getUsername(), error);
            throw new ProductException(error);
        }
    }

    @Override
    public ResponseEntity addProduct(Product product) throws URISyntaxException, ProductException {
        Product savedProduct = productRepository.save(product);
        if (savedProduct.getId() != null) {
            return ResponseEntity.created(new URI("/products/" + savedProduct.getId())).body(savedProduct);
        } else {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String error = "Product with name " + product.getName() + " was not added";
            logger.error("User: {}. Error: {}", currentUser.getUsername(), error);
            throw new ProductException(error);
        }
    }

    @Override
    public Product getProductById(Integer productId) throws ProductException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        } else {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String error = "Product with id " + productId + " does not exist";
            logger.error("User: {}. Error: {}", currentUser.getUsername(), error);
            throw new ProductException(error);
        }
    }

    @Override
    public List<Product> findByNameContaining(String name) throws ProductException {
        List<Product> productList = productRepository.findByNameContaining(name);
        if (!productList.isEmpty()) {
            return productList;
        } else {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String error = "No products found with name containing " + name;
            logger.warn("User: {}. Error: {}", currentUser.getUsername(), error);
            throw new ProductException(error);
        }
    }

    @Override
    public ResponseEntity updateProduct(Integer productId, Product product) throws ProductException {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        Product savedProduct = productRepository.save(existingProduct);
        if (savedProduct.getId() != null) {
            return ResponseEntity.ok(savedProduct);
        } else {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String error = "Product with id " + productId + " was not updated";
            logger.error("User: {}. Error: {}", currentUser.getUsername(), error);
            throw new ProductException(error);
        }
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
