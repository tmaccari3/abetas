package com.maccari.abet.web;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/")
	public String homePage(Principal principal, Model model) {
		String email = "user email";
		if(principal != null) {
			principal.getName();
			email = principal.getName();
		}
		model.addAttribute("msg", email);
		
		return "home/index";
	}
	
	@RequestMapping(value = "/edit")
	public String edit() {
		return "home/edit";
	}
	
	@RequestMapping(value = "/manage")
	public String manage(Model model) {
		List<User> users = userService.getAll();
		model.addAttribute("users", users);
		
		return "home/manage";
	}
	
	@RequestMapping(value = "/register")
	public String displayForm(User user) {
		return "home/register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String addUser(@Valid User user, BindingResult bindingResult) {
		userValidator.validate(user, bindingResult);
		if(bindingResult.hasErrors()) {
			return "home/register";
		}
		
		String encodedPswd = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPswd);
		
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_GENERAL");
		/*for(String role : user.getRoles()) {
			roles.add("ROLE_" + role);
		}*/

		user.setRoles(roles);
		userService.create(user);
		
		autologin(user, roles);
		
		return "redirect:/";
	}
	
	//automatically logs a user in after they register for an account
	private void autologin(User user, List<String> roles) {
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(String.join(",", roles));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
