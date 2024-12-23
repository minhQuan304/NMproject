package com.example.NMproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/home")
public class HomeController {
	@GetMapping("/home")
	public String home() {
		return "home"; // Trả về file loginRegister.html trong thư mục templates
	}

	@GetMapping("/BookManagement")
	public String BookManagement() {
		return "BookManagement"; // Trả về file loginRegister.html trong thư mục templates
	}

	@GetMapping("/UserManagement")
	public String UserManagement() {
		return "UserManagement"; // Trả về file loginRegister.html trong thư mục templates
	}
}