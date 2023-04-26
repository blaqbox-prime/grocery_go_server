package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Inventory;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepository repo;

//    Get All products
    @GetMapping( "/")
    public List<Product> getAllProducts(){
        System.out.println("Entry point hit!!");
        return repo.findAll();
    }

//    Get Product by Id
    public Optional<Product> getProductById(@RequestBody String productId){
        Optional<Product> product = repo.findById(productId);
        if(product == null) return null;
        return product;
    }

    @PostMapping(path = "/add-product")
    public ResponseEntity addProduct(@RequestBody Map<String,Object> body){

        System.out.println(body.get("inventory"));
        Map<String,Object> invMap = (Map<String, Object>) body.get("inventory");

        Inventory inventory = new Inventory((int) invMap.get("stockAvailability"));


        Product product = new Product(
                (String) body.get("name"),
                (String) body.get("description"),
                (Double) body.get("price"),
                (String) body.get("catergory"),
                (String) body.get("imageUrl"),
                inventory
        );

        repo.save(product);

        return ResponseEntity.ok("Successful, Probably");

//        return repo.save(prod);
    }

}
