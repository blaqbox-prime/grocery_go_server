package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
