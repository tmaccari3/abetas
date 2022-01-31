package com.maccari.abet.web.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.service.UserService;
import com.maccari.abet.web.validation.UserValidator;

@Controller
@RequestMapping("/secret")
public class SecretController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/***IMPORTANT***
	 * 
	 * Backdoor access, delete before production.
	 */
	@RequestMapping(value = "/register")
	public String displayForm(User user) {
		return "user/secretRegister";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String addUser(@Valid User user, BindingResult bindingResult) {
		userValidator.validate(user, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/secretRegister";
		}
		
		String encodedPswd = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPswd);
		System.out.println(user.getEmail());
		userService.create(user);
		
		return "redirect:/login";
	}
	
	@ModelAttribute("roleTypes")
	public ArrayList<String> getRoles(){
		return (ArrayList<String>) userService.getRoles();
	}
}
