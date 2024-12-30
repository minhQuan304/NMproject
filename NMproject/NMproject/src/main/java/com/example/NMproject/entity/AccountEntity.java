package com.example.NMproject.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userID;
	private String email;
	private String username;
	private String password;
	private String name;
	private String phone;
	private String address;
	private LocalDate createAt;
	private LocalDate updateAt;
	private long userRole;

	public AccountEntity(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

}
