package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.User;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;



    public Object createUser(String username, String password, String role) {

        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());

        User user = new User(username, hashedPassword, role);
        User savedUser = userRepository.save(user);

//       If role == customer then create customer profile
        if(role.equals("customer")){
            Customer customer = new Customer();
            customer.setEmail(savedUser.getEmail());
            customer.setUser(savedUser);
            return customerRepository.save(customer);
        }
        return savedUser;
    }

    public Object authenticate(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }
        if (!BCrypt.checkpw(password,user.getPassword())) {
            throw new Exception("Invalid password");
        }
        if(user.getRole() == "customer"){
            return customerRepository.findByEmail(email);
        }else {
            return user;
        }
    }

    public boolean isAdmin(User user) {
        return user.getRole().equals("admin");
    }
}
