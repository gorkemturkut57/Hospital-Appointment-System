package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email= ?1")
	User findByEmail(String email);
	
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'DOCTOR'")
	List<User> findAllDoctors();
}
