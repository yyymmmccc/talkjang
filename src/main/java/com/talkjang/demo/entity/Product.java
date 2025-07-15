package com.talkjang.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.talkjang.demo.common.enums.ProductCondition;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.request.product.ProductRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long price;

    @Column(name = "delivery_fee")
    private Long deliveryFee;

    @Column(name = "product_condition")
    @Enumerated(EnumType.STRING)
    private ProductCondition productCondition;

    @Column(name = "trade_location")
    private String tradeLocation;

    @Column(name = "trade_location_detail")
    private String tradeLocationDetail;

    @Enumerated(EnumType.STRING)
    private ProductState state;

    @Column(name = "view_count")
    private Long viewCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "up_at")
    private LocalDateTime upAt;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductImage> productImageList;

    public void update(ProductRequestDto dto, Category category){
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.productCondition = dto.getProductCondition();
        this.tradeLocation = dto.getTradeLocation();
        this.tradeLocationDetail = dto.getTradeLocationDetail();
        this.state = dto.getState();
        this.category = category;
    }

    public void updateProductState(ProductState productState){
        this.state = productState;
    }

}
