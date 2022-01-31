package com.maccari.abet.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;
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

import com.maccari.abet.domain.entity.WebEmail;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.WebUser;
import com.maccari.abet.domain.service.EmailServiceImpl;
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
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final String systemEmail = "abetastest@gmail.com";
	
	@RequestMapping(value = "/manage")
	public String manage(Model model) {
		model.addAttribute("users", userService.getAll());
		
		return "user/manage";
	}
	
	@RequestMapping(value = "/register/request")
	public String registerRequest(WebEmail webEmail) {
		return "user/request";
	}
	
	@RequestMapping(value = "/register/request", method = RequestMethod.POST, params = "submit")
	public String submitEmail(@Valid WebEmail email, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "user/request";
		}
		email.setTo(systemEmail);
		try {
			emailService.sendEmail(email);
		} catch(MessagingException e) {
			System.out.println("Email not sent.");
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/register/request", method = RequestMethod.POST, params = "cancel")
	public String cancelSubmitEmail() {
		return "redirect:/";
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
	
	@RequestMapping(value = "/user/remove")
	public String displayUser(@RequestParam(value = "email", required = true) 
		String email, Model model) {
		model.addAttribute("user", userService.getUserByEmail(email));
		
		return "user/remove";
	}
	
	@RequestMapping(value = "/user/remove", method = RequestMethod.POST)
	public String removeUser(@RequestParam(value = "email", required = true) 
		String email, Model model) {
		if(email.isEmpty() || email == null) {
			return "redirect:/manage";
		}
		userService.remove(userService.getUserByEmail(email));
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/user/remove", method = RequestMethod.POST, params = "cancel")
	public String cancelRemoveUser() {
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/user/edit")
	public String editUser(@RequestParam(value = "email", required = true) 
		String email, WebUser webUser, Model model) {
		User user = userService.getUserByEmail(email);
		model.addAttribute("webUser", new WebUser(user.getEmail(), user.getRoles()));
		
		return "user/edit";
	}
	
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST, params = "submit")
	public String editUser(@Valid WebUser webUser, BindingResult bindingResult) {
		webUserValidator.validate(webUser, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		userService.update(userService.convertWebUser(webUser));
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST, params = "cancel")
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
