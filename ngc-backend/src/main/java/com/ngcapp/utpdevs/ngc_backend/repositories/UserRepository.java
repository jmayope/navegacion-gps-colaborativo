package com.ngcapp.utpdevs.ngc_backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ngcapp.utpdevs.ngc_backend.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
  Optional<UserModel> findByEmail(String email);
  Optional<UserModel> findByPhone(String phone);
  boolean existsByEmail(String email);
  boolean existsByPhone(String phone);
}