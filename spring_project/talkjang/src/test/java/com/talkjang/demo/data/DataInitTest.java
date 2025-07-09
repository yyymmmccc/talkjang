package com.talkjang.demo.data;

import com.talkjang.demo.common.enums.ProductCondition;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.entity.Category;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.ProductImage;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.repository.CategoryRepository;
import com.talkjang.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DataInitTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    @Autowired
    TransactionTemplate transactionTemplate;

    @PersistenceContext
    EntityManager entityManager;

    static final int BULK_INSERT_SIZE = 100;
    static final int EXECUTE_COUNT = 100;

    @Test
    void ProductDataInitialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Category category = categoryRepository.findById(1L).orElse(null);
        User user = userRepository.findById("jimindong100").orElse(null);

        for(int i = 0; i < EXECUTE_COUNT; ++i){
            executorService.submit(() -> { // 스레드가 10개이므로 순차적이 아닌 병렬로 실행 :
                // 동시에 스레드-1이 insert (2000번 반복 실행), 스레드-2 insert(2000번 반복실행) ..
                insertProduct(category, user);
                latch.countDown();
                System.out.println("latch.getCount() + " + latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();
    }

    private void insertProduct(Category category, User user) {
        transactionTemplate.executeWithoutResult(status ->{
            for(int i = 0; i < BULK_INSERT_SIZE; ++i){
                Product product = Product.builder()
                        .name("아이폰 15 ㅍㅍㅍㅍ")
                        .description("사용감있음")
                        .price(Long.valueOf(new Random(15000).toString()))
                        .productCondition(ProductCondition.DAMAGED)
                        .state(ProductState.ON_SALE)
                        .category(category)
                        .user(user)
                        .build();
                entityManager.persist(product);

                ProductImage productImage = ProductImage.builder()
                        .imageUrl("http://localhost:8080/image/f1ba1fcf-d69c-4925-b04a-888f69988764.jpg")
                        .product(product)
                        .thumbnail(true)
                        .build();

                entityManager.persist(productImage);
            }
        });
    }
}
