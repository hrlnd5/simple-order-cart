package com.example.restfulapi.service;

import com.example.restfulapi.entity.Product;
import com.example.restfulapi.entity.ProductType;
import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.product.CreateProductRequest;
import com.example.restfulapi.model.product.ProductResponse;
import com.example.restfulapi.model.product.SearchProductRequest;
import com.example.restfulapi.model.product.UpdateProductRequest;
import com.example.restfulapi.repository.OrderDetailRepository;
import com.example.restfulapi.repository.ProductRepository;
import com.example.restfulapi.repository.ProductTypeRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ValidationService validationService;

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse
                .builder()
                .productId(product.getProductId())
                .name(product.getName())
                .productType(product.getProductTypeId().getName())
                .stock(product.getStock())
                .price(product.getPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public ProductResponse get(User user, Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

        return toProductResponse(product);
    }

    @Transactional
    public ProductResponse create(User user, CreateProductRequest request) {
        validationService.validate(request);

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Type is not found"));

        Product product = new Product();
        product.setName(request.getName());
        product.setProductTypeId(productType);
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setCreatedBy(user);

        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public ProductResponse update(User user, UpdateProductRequest request) {
        validationService.validate(request);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

        ProductType productType = productTypeRepository.findById(request.getProductTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Type is not found"));

        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        product.setUpdatedBy(user);

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }

        if (request.getStock() > 0) {
            product.setStock(request.getStock());
        }

        if (request.getPrice() > 0) {
            product.setPrice(request.getPrice());
        }

        if (request.getProductTypeId() > 0) {
            product.setProductTypeId(productType);
        }

        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public boolean delete(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));
        boolean isProductUsed = orderDetailRepository.existsByProduct(product);
        if (isProductUsed) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the product is still in use");

        productRepository.delete(product);

        return true;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(User user, SearchProductRequest request) {
        validationService.validate(request);

        Specification<Product> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(builder.like(root.get("name"), "%" + request.getName() + "%")));
            }

            if (Objects.nonNull(request.getMinPrice()) && Objects.nonNull(request.getMaxPrice())) {
                predicates.add(builder.or(
                                builder.between(root.get("price"),
                                        request.getMinPrice(),
                                        request.getMaxPrice())
                        )
                );
            } else if (Objects.nonNull(request.getMinPrice())) {
                predicates.add(builder.or(builder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice())));
            } else if (Objects.nonNull(request.getMaxPrice())) {
                predicates.add(builder.or(builder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice())));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Product> products = productRepository.findAll(specification, pageable);

        List<ProductResponse> responses = products
                .getContent()
                .stream()
                .map(this::toProductResponse)
                .toList();

        return new PageImpl<>(responses, pageable, products.getTotalElements());
    }
}
