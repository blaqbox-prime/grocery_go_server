package com.pk.grocery_go_server.Controllers;

import com.google.gson.Gson;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import com.pk.grocery_go_server.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductRepository repo;
    @Autowired
    ProductService productService;

//    Get Product by Id
    public Optional<Product> getProductById(@RequestBody String productId){
        Optional<Product> product = repo.findById(productId);
        if(product == null) return null;
        return product;
    }

    @PostMapping(path = "/add-product")
    public Product addProduct(@RequestBody Product product){
        return repo.save(product);
    }

//    Get Products by Category
    @GetMapping(value = "/", params = "category")
    public List<Product> getProductByCategory(@RequestParam("category") String category){
        System.out.println("-------------------------------");
        System.out.println(category);
        System.out.println("-------------------------------");
        return productService.getProductsByCategory(category);
    }

    //    Search products
    @GetMapping(value = "/", params = "search")
    public List<Product> searchProducts(@RequestParam("search") String text){
        return productService.searchProducts(text);
    }

//        Get All products
    @GetMapping( "/")
    public List<Product> getAllProducts(){

        Gson gson = new Gson();
        return productService.getAllProducts();

    }

    @DeleteMapping("/{id}/delete-product")
    public ResponseEntity<Map<String,Object>> removeProductListing(@PathVariable String id){

        Map<String,Object>  responseBody = new HashMap<>();

        // Check if the product exists in the database
        Product product = productService.getProductById(id);

        if (product == null) {
            responseBody.put("error","Product not found");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
//        Remove Product from database
        repo.deleteById(id);
        responseBody.put("message","Product Deleted successfully");
        responseBody.put("data", product);
        return  new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Map<String,Object>> removeAllProducts(){
//      Remove Product from database
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("message","Products Deleted successfully");
        repo.deleteAll();
        return  new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrCreateProduct(@PathVariable String id, @RequestBody Product product) {
        Optional<Product> existingProduct = repo.findById(id);

        try {
            if (existingProduct.isPresent()) {
                System.out.println("Updating Product");
                // Update the existing product
                Product updatedProduct = existingProduct.get();
                updatedProduct.setName(product.getName());
                updatedProduct.setDescription(product.getDescription());
                updatedProduct.setPrice(product.getPrice());
                updatedProduct.setCategory(product.getCategory());
                updatedProduct.setImage(product.getImage());
                updatedProduct.setInventory(product.getInventory());
                updatedProduct.setReviews(product.getReviews());

                System.out.println(updatedProduct.toString());

                repo.save(updatedProduct);
                return ResponseEntity.ok(updatedProduct);
            } else {
                // Create a new product
//                Product newProduct = new Product();
//                newProduct.setName(product.getName());
//                newProduct.setDescription(product.getDescription());
//                newProduct.setPrice(product.getPrice());
//                newProduct.setCategory(product.getCategory());
//                newProduct.setImage(product.getImage());
//                newProduct.setInventory(product.getInventory());
//                newProduct.setReviews(product.getReviews());

                Product newProduct = repo.save(product);
                return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
            }
        }catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
    }



}
