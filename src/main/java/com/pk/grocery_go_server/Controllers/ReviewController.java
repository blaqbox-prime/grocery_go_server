package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Models.Review;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import com.pk.grocery_go_server.Repositories.ReviewRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/")
    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }


    // Endpoint to create a new review
    @PostMapping("/add-review")
    public ResponseEntity<Object> createReview(@RequestBody Review review) {

        Map<String, Object> map = new HashMap<>();

        // Check if the customer exists
        if (!customerRepository.existsById(review.getCustomer().get_id())) {
            map.put("message","Customer does not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // Check if the product exists
        if (!productRepository.existsById(review.getProduct().get_id())) {
            map.put("message","Product does not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        if(review.getRating() > 5){
            review.setRating(5);
        }

        if(review.getRating() < 0){
            review.setRating(0);
        }

        // Save the review
        Review savedReview = reviewRepository.save(review);

        return new ResponseEntity<>(savedReview,HttpStatus.OK);
    }

    // Endpoint to get all reviews for a single product
    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getAllReviewsForProduct(@PathVariable String productId) {
        Map<String, Object> map = new HashMap<>();

        // Check if the product exists
        if (!productRepository.existsById(productId)) {
            map.put("message","Product Not Found");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }

        List<Review> reviews = reviewRepository.findByProduct__id(productId);

        return ResponseEntity.ok(reviews);
    }

    // Endpoint to get reviews for a single customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getReviewsForCustomer(@PathVariable String customerId) {

        Map<String, Object> map = new HashMap<>();

        // Check if the customer exists
        if (!customerRepository.existsById(customerId)) {
            map.put("message","Customer Not Found");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        List<Review> reviews = reviewRepository.findByCustomer__id(customerId);

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> addReview(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();

        if(reviewRepository.existsById(id)){
           Review review = reviewRepository.findById(id).orElseGet(null);
            reviewRepository.deleteById(id);
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        response.put("message","Failed to Delete");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/most-reviewed")
    public ResponseEntity<Object> getMostReviewedProducts(){
        try {
            List<Product> products = productRepository.findAll();

            Collections.sort(products, Comparator.comparingInt(product -> product.getReviews().size()));

            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch(Exception e){
            Map<String,Object> map = new HashMap<>();
            map.put("message",e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

