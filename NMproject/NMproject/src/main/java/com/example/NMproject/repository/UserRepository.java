package com.example.NMproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.NMproject.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	Optional<UserEntity> findByEmailAndPassword(String email, String password);

	Optional<UserEntity> findByEmail(String email);
}
