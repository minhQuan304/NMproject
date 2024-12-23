package com.example.NMproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.NMproject.dto.AccountResponse;
import com.example.NMproject.entity.AccountEntity;

import jakarta.transaction.Transactional;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

	// Sử dụng @Query thay vì phương thức findByEmailAndPassword
	@Query("SELECT a FROM AccountEntity a WHERE a.email = :email AND a.password = :password")
	Optional<AccountEntity> findByEmailAndPassword(String email, String password);

	// Sử dụng @Query thay vì phương thức findByEmail
	@Query("SELECT a FROM AccountEntity a WHERE a.email = :email")
	Optional<AccountEntity> findByEmail(String email);

	// Phương thức này sử dụng @Query đã có sẵn
	@Query("SELECT new com.example.NMproject.dto.AccountResponse(a.userID, a.username, a.email, a.name, a.phone, a.address) FROM AccountEntity a")
	List<AccountResponse> getAllUsersWithAccountInfo();

	@Modifying
	@Transactional
	@Query("DELETE FROM AccountEntity a WHERE a.userID = :userID")
	void deleteByUserID(@Param("userID") int userID);
}
