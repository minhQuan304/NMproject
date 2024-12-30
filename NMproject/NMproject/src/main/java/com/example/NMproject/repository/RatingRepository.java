package com.example.NMproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.NMproject.entity.Rating;
import com.example.NMproject.entity.RatingId;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
	List<Rating> findByUserID(Long userID);

	List<Rating> findByBookID(Long bookID);

	Optional<Rating> findByUserIDAndBookID(Long userID, Long bookID);

}