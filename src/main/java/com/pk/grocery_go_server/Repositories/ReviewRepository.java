package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
