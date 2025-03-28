package com.gb.StoreManagementAPI.controller;

import com.gb.StoreManagementAPI.model.Product;
import com.gb.StoreManagementAPI.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void getAllProductsWhenAnonymousUser() throws Exception {
        mockMvc.perform(get("/products/getAllProducts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = { "ADMIN", "USER" })
    public void getAllProductsWhenNonAdminUser() throws Exception {
        mockMvc.perform(get("/products/getAllProducts"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin")
    public void getAllProductsWhenAdminUserAndNoProducts() throws Exception {
        mockMvc.perform(get("/products/getAllProducts"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "admin")
    void addProductWhenAdminUser() throws Exception {
        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", authorities = { "ADMIN", "USER" })
    void addProductWhenNonAdminUser() throws Exception {
        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    // first add a product, then get the added product
    @Test
    @WithUserDetails(value = "admin")
    void getProductByIdWhenAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        productRepository.save(product);
        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk());
    }

    // first add a product, then get the added product
    @Test
    @WithMockUser(username = "user", authorities = { "ADMIN", "USER" })
    void getProductByIdWhenNonAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk());
    }

    // first add a product, then get the added product by substring match
    @Test
    @WithUserDetails(value = "admin")
    void findByNameContainingWhenAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        productRepository.save(product);
        mockMvc.perform(get("/products/find/ame"))
                .andExpect(status().isOk());
    }

    // first add a product, then get the added product by substring match
    @Test
    @WithMockUser(username = "user", authorities = { "ADMIN", "USER" })
    void findByNameContainingWhenNonAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        productRepository.save(product);
        mockMvc.perform(get("/products/find/ame"))
                .andExpect(status().isOk());
    }

    // first add a product, then update the added product
    @Test
    @WithUserDetails(value = "admin")
    void updateProductWhenAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        mockMvc.perform(put("/products/"+product.getId()).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk());
    }

    // first add a product, then update the added product
    @Test
    @WithMockUser(username = "user", authorities = { "ADMIN", "USER" })
    void updateProductWhenNonAdminUser() throws Exception {
        Product product = new Product();
        product.setName("name1");
        product.setPrice(11);
        product = productRepository.save(product);
        mockMvc.perform(put("/products/"+product.getId()).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }
}