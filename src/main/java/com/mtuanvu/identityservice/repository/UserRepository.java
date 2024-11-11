package com.mtuanvu.identityservice.repository;

import com.mtuanvu.identityservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username); //kiểm tra sự tồn tại của username, nếu trùng thì không tạo được
    Optional<User> findByUsername(String username);
}
