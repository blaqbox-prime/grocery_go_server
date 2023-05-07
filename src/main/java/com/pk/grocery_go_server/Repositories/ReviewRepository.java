package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProduct_Id(String productId);
    List<Review> findByCustomer_Id(String customerId);
}
