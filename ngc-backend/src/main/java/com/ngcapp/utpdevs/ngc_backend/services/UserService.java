package com.ngcapp.utpdevs.ngc_backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ngcapp.utpdevs.ngc_backend.models.UserModel;
import com.ngcapp.utpdevs.ngc_backend.repositories.UserRepository;

@Service
public class UserService {
  private UserRepository repository;
    
  public List<UserModel> findAll() {
      return repository.findAll();
  }
  
  public UserModel findById(UUID id) {
      return repository.findById(id).orElse(null);
  }
  
  public UserModel findByEmail(String email) {
      return repository.findByEmail(email).orElse(null);
  }
  
  public UserModel findByPhone(String phone) {
      return repository.findByPhone(phone).orElse(null);
  }
  
  public UserModel save(UserModel usuario) {
      return repository.save(usuario);
  }
  
  public void delete(UUID id) {
      repository.deleteById(id);
  }
  
  public boolean existsEmail(String email) {
      return repository.existsByEmail(email);
  }
  
  public boolean existsPhone(String phone) {
      return repository.existsByPhone(phone);
  }
}
