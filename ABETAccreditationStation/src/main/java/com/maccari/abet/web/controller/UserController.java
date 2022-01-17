package com.maccari.abet.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.WebUser;
import com.maccari.abet.domain.service.UserService;
import com.maccari.abet.web.validation.UserValidator;
import com.maccari.abet.web.validation.WebUserValidator;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private WebUserValidator webUserValidator;
	
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/manage")
	public String manage(Model model) {
		List<User> users = userService.getAll();
		model.addAttribute("users", users);
		
		return "user/manage";
	}
	
	@RequestMapping(value = "/register")
	public String displayForm(User user) {
		return "user/register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String addUser(@Valid User user, BindingResult bindingResult) {
		userValidator.validate(user, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/register";
		}
		
		String encodedPswd = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPswd);
		
		userService.create(user);
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/remove")
	public String displayUser(@RequestParam(value = "email", required = true) 
		String email, Model model) {
		User user = userService.getUserByEmail(email);
		model.addAttribute("user", user);
		
		return "user/remove";
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String removeUser(@RequestParam(value = "email", required = true) 
		String email, Model model) {
		if(email.isEmpty()) {
			return "redirect:/manage";
		}
		User user = userService.getUserByEmail(email);
		userService.remove(user);
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/edit")
	public String editUser(@RequestParam(value = "email", required = true) 
		String email, WebUser webUser,
			Model model) {
		User user = userService.getUserByEmail(email);
		model.addAttribute("webUser", new WebUser(user.getEmail(), user.getRoles()));
		
		return "user/edit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
	public String editUser(@Valid WebUser webUser, BindingResult bindingResult) {
		webUserValidator.validate(webUser, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		System.out.println(webUser.getEmail());
		userService.update(userService.convertWebUser(webUser));
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public String cancelUserEdit(@Valid WebUser webUser, BindingResult bindingResult) {
		return "redirect:/manage";
	}
	
	//automatically logs a user in after they register for an account
	private void autologin(User user, List<String> roles) {
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(String.join(",", roles));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@ModelAttribute("roleTypes")
	public ArrayList<String> getRoles(){
		return (ArrayList<String>) userService.getRoles();
	}
}
