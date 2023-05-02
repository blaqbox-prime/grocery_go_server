package com.pk.grocery_go_server.Controllers;

import com.google.gson.Gson;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import com.pk.grocery_go_server.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        System.out.println("This Endpoint Hit!!!!!!!!");
        Gson gson = new Gson();
        return productService.getAllProducts();

    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> removeProductListing(@PathVariable String id){
        // Check if the product exists in the database
        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }
//        Remove Product from database
        repo.deleteById(id);
        return  new ResponseEntity<>("Product Deleted successfully", HttpStatus.OK);
    }

}
