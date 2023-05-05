package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.User;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;



    public User createUser(String username, String password, String role) {
        User user = new User(username, password, role);
        return userRepository.save(user);
    }

    public Customer authenticate(String email, String password) throws Exception {
        Customer user = customerRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        if (!(password.equals(user.getPassword()))) {
            throw new Exception("Invalid password");
        }
        return user;
    }

    public boolean isAdmin(User user) {
        return user.getRole().equals("admin");
    }
}
