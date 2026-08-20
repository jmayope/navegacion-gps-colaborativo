package com.ngcapp.utpdevs.ngc_backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngcapp.utpdevs.ngc_backend.models.User;

@Service
public class UserService {
    @Autowired
    private SupabaseCrudService crudService;

    private static final String TABLE = "users";

    public List<User> findAll() {
        return crudService.findAll(TABLE, User[].class);
    }

    public User findById(UUID id) {
        return crudService.findById(TABLE, id, User[].class);
    }

    public User findByUsername(String username) {
        List<User> result = crudService.find(
            TABLE, 
            "username=eq." + username, 
            User[].class
        );
        return result.isEmpty() ? null : result.get(0);
    }

    public User findByEmail(String email) {
        List<User> result = crudService.find(
            TABLE, 
            "email=eq." + email, 
            User[].class
        );
        return result.isEmpty() ? null : result.get(0);
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        List<User> result = crudService.find(
            TABLE, 
            "or=(username.eq." + usernameOrEmail + ",email.eq." + usernameOrEmail + ")", 
            User[].class
        );
        return result.isEmpty() ? null : result.get(0);
    }

    public User create(User usuario) {
        return crudService.insert(TABLE, usuario, User[].class);
    }

    public User update(UUID id, User usuario) {
        return crudService.update(TABLE, id, usuario, User[].class);
    }

    public void delete(UUID id) {
        crudService.delete(TABLE, id);
    }

    public boolean existsByEmail(String email) {
        return crudService.exists(TABLE, "email=eq." + email);
    }

    public boolean existsByPhone(String phone) {
        return crudService.exists(TABLE, "phone=eq." + phone);
    }
}
