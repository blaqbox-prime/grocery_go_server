package com.pk.grocery_go_server.Services;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoConverter converter;

    @Autowired
    MongoClient mongoClient;

    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products;
    }

    public Product getProductById(String id){
        Optional<Product> product = productRepo.findById(id);
        return product.orElse(null);
    }

    public List<Product> getProductsByCategory(String category) {
        Query query = new Query(Criteria.where("category").is(category));
        return mongoTemplate.find(query, Product.class);
    }

    public List<Product> searchProducts(String query) {
        final List<Product> products = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("grocery_go");
        MongoCollection<Document> collection = database.getCollection("Product");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("index", "product_search")
                                .append("text",
                                        new Document("query", query)
                                                .append("path", Arrays.asList("name", "description")))),
                new Document("$sort",
                        new Document("name", 1L))));

        result.forEach(doc -> products.add(converter.read(Product.class,doc)));

        return products;
    }
}
