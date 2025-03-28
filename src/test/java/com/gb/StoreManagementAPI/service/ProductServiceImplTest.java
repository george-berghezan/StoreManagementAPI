package com.gb.StoreManagementAPI.service;

import com.gb.StoreManagementAPI.exception.ProductException;
import com.gb.StoreManagementAPI.model.Product;
import com.gb.StoreManagementAPI.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    private final ProductServiceImpl productServiceImpl = new ProductServiceImpl();

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        productServiceImpl.setProductRepository(productRepository);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getAllProductsWhenNoProducts() {
        try {
            productServiceImpl.getAllProducts();
        } catch (ProductException e) {
            assertEquals(ProductException.class, e.getClass());
            assertEquals("No products found", e.getMessage());
        }

    }

    // first add a product, then get all the products
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getAllProductsWhenOneProductAdded() throws ProductException {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productServiceImpl.getAllProducts().getFirst());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void addProduct() throws URISyntaxException, ProductException {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        assertEquals(HttpStatus.CREATED.value(), productServiceImpl.addProduct(product).getStatusCode().value());
    }

    // first add a product, then get the added product
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getProductByIdWhenProductFound() throws ProductException {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productServiceImpl.getProductById(product.getId()));

    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getProductByIdWhenProductNotFound() {
        try {
            productServiceImpl.getProductById(0);
        } catch (ProductException e) {
            assertEquals(ProductException.class, e.getClass());
            assertEquals("Product with id 0 does not exist", e.getMessage());
        }
    }

    // first add a product, then get the added product by substring match
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void findByNameContainingWhenProductFound() throws ProductException {
        Product product = new Product();
        product.setName("name1");
        product = productRepository.save(product);
        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productServiceImpl.findByNameContaining("ame").getFirst());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void findByNameContainingWhenProductNotFound() {
        try {
            productServiceImpl.findByNameContaining("axme");
        } catch (ProductException e) {
            assertEquals(ProductException.class, e.getClass());
            assertEquals("No products found with name containing axme", e.getMessage());
        }
    }

    // first add a product, then update the added product
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void updateProduct() throws ProductException {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        product.setName("name2");
        product.setPrice(22);
        productServiceImpl.updateProduct(product.getId(), product);
        assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productServiceImpl.getProductById(product.getId()));
    }
}