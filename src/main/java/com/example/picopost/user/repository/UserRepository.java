package com.example.picopost.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.picopost.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
