package com.talkjang.demo.repository;

import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    void deleteByProduct(Product product);
}
