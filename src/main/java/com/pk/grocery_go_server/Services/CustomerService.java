package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Models.ShoppingList;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    public Customer getCustomerById(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    public Customer findCustomerByEmail(String email) {

        return customerRepository.findByEmail(email);
    }

    public Customer updateDetails(String email, Customer newDetails) {

        Customer customer = customerRepository.findByEmail(email);

        if (customer != null) {
            customerRepository.save(newDetails);
            return customer;
        }
        return customer;
    }

    //    Create Shopping List
    public ShoppingList addShoppingList(String customerId, ShoppingList list) {

        Optional<Customer> customer = Optional.ofNullable(getCustomerById(customerId));

        if (customer.isPresent()) {
            customer.get().getShoppingLists().add(list);
            customerRepository.save(customer.get());
            return list;
        } else {
            return null;
        }

    }

    public ShoppingList addItemToShoppingList(String email, String listName, Product body) {
        Optional<Customer> customer = Optional.ofNullable(findCustomerByEmail(email));

        if (customer.isPresent()) {
            ShoppingList list = customer.get().getShoppingLists()
                    .stream()
                    .filter(shoppingList -> Objects.equals(shoppingList.getTitle().toLowerCase(), listName.replace('-', ' ')))
                    .collect(Collectors.toList()).get(0);

//            If product already in list, Do not Add it
            if (!list.getItems().contains(body)) {
                customerRepository.save(customer.get());
                return list;
            } else {
                return null;
            }
        } else {
            return  null;
        }
    }
}