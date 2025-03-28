package com.gb.StoreManagementAPI.controller;

import com.gb.StoreManagementAPI.exception.ProductException;
import com.gb.StoreManagementAPI.model.Product;
import com.gb.StoreManagementAPI.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (ProductException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "", e);
        }
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody Product product) throws URISyntaxException {
        try {
            return productService.addProduct(product);
        } catch (ProductException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "", e);
        }
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        try {
            return productService.getProductById(id);
        } catch (ProductException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "", e);
        }
    }

    @GetMapping("/find/{name}")
    public List<Product> findByNameContaining(@PathVariable String name) {
        try {
            return productService.findByNameContaining(name);
        } catch (ProductException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        try {
            return productService.updateProduct(id, product);
        } catch (ProductException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "", e);
        }
    }
}
