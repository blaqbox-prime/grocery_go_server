package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Review;
import com.pk.grocery_go_server.Repositories.ReviewRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("/")
    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }


    @PostMapping("/add-review")
    public Review addReview(@RequestBody Review review){
            if(review.getRating() > 5){
                review.setRating(5);
            }

            if(review.getRating() < 0){
                review.setRating(0);
            }

            return reviewRepository.save(review);
        }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String,Object>> addReview(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();

        if(reviewRepository.existsById(id)){
           Review review = reviewRepository.findById(id).orElseGet(null);
            reviewRepository.deleteById(id);
            response.put("message","Successfully Deleted");
            response.put("data", review);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

