package com.employee.management.service;

import com.employee.management.model.Student;
import com.employee.management.repository.ProductRepository;
import com.employee.management.dto.ProductRequest;
import com.employee.management.dto.ProductResponse;
import com.employee.management.model.Product;
import com.employee.management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct=productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        return productResponse;

    }


    private void updateProductFromRequest(Product product, ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        productRepository.save(product);
    }


    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id).
                orElseThrow(()->new RuntimeException("Product not found with id:"+ id ));
        return mapToProductResponse(product);
//        return productRepository.findById(id).stream().map(this::mapToProductResponse);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {

//        Product product = new Product();
//        updateProductFromRequest(product,productRequest);
//        Product savedProduct=productRepository.save(product);
//        return mapToProductResponse(savedProduct);

        return productRepository.findById(id).map(existingProduct-> {
                updateProductFromRequest(existingProduct,productRequest);
                Product savedProduct=productRepository.save(existingProduct);
        return mapToProductResponse(savedProduct);}
        );



    }

    public String deleteProduct(Long id) {
        Product product=productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found with id:"+ id ));
        product.setActive(false);
        //productRepository.delete(product);

        productRepository.save(product);
        return "Product has been deleted";
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
