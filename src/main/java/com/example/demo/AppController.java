package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller 
public class AppController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@GetMapping("")
	public String viewHomepage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showSignupForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(User user, Model model) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		// Varsayılan olarak "PATIENT" rolü atanacak
		Role patientRole = roleRepo.findByName("PATIENT");
		if (patientRole == null) {
			patientRole = new Role("PATIENT");
			roleRepo.save(patientRole);
		}
		user.addRole(patientRole);
		
		userRepo.save(user);
		
		return "register_success";
	}
	
	@GetMapping("/list_users")
	public String viewUsersList(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "users";
	}
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
	
	@GetMapping("/home")
	public String viewHome() {
		return "home";
	}
	
	@GetMapping("/patient/dashboard")
	public String patientDashboard() {
		return "patient/dashboard";
	}
	
	@GetMapping("/doctor/dashboard")
	public String doctorDashboard() {
		return "doctor/dashboard";
	}
	
	@GetMapping("/register_doctor")
	public String showDoctorSignupForm(Model model) {
		model.addAttribute("user", new User());
		
		return "register_doctor";
	}
	
	@PostMapping("/process_doctor_register")
	public String processDoctorRegistration(User user, Model model) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		// "DOCTOR" rolü atanacak
		Role doctorRole = roleRepo.findByName("DOCTOR");
		if (doctorRole == null) {
			doctorRole = new Role("DOCTOR");
			roleRepo.save(doctorRole);
		}
		user.addRole(doctorRole);
		
		userRepo.save(user);
		
		return "register_success";
	}
}
