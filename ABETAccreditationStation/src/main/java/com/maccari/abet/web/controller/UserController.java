package com.maccari.abet.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.web.WebEmail;
import com.maccari.abet.domain.entity.web.WebUser;
import com.maccari.abet.domain.service.EmailServiceImpl;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.domain.service.UserService;
import com.maccari.abet.web.validation.UserValidator;
import com.maccari.abet.web.validation.WebEmailValidator;
import com.maccari.abet.web.validation.WebUserValidator;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private WebUserValidator webUserValidator;
	
	@Autowired
	private WebEmailValidator emailValidator;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final String systemEmail = "abetastest@gmail.com";
	
	@RequestMapping(value = "/user/summary")
	public String summary(Principal principal, Model model) {
		model.addAttribute("user", userService.getUserByEmail(principal.getName()));
		
		return "user/summary";
	}
	
	@RequestMapping(value = "/manage")
	public String manage(Model model) {
		model.addAttribute("users", userService.getAll());
		
		return "user/manage";
	}
	
	@RequestMapping(value = "/program/manage")
	public String managePrograms(Model model) {
		model.addAttribute("users", userService.getAll());
		
		return "user/managePrograms";
	}
	
	@RequestMapping(value = "/register/request")
	public String registerRequest(WebEmail webEmail) {
		return "user/request";
	}
	
	@RequestMapping(value = "/register/request", method = RequestMethod.POST, params = "submit")
	public String submitEmail(@Valid WebEmail email, BindingResult bindingResult) {
		//emailValidator.validate(email, bindingResult);
		if(userService.userExists(email.getFrom())) {
			bindingResult.rejectValue("from", "user.already.exists");
		}
		
		if(bindingResult.hasErrors()) {
			return "user/request";
		}
		email.setTo(systemEmail);
		try {
			emailService.sendRequestEmail(email);
		} catch(MessagingException e) {
			System.out.println("Request failed, email not sent.");
		}
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/register/request", method = RequestMethod.POST, params = "cancel")
	public String cancelSubmitEmail() {
		return "redirect:/";
	}
	
	@RequestMapping(value = "/register")
	public String displayForm(WebUser user) {
		return "user/register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String addUser(@Valid WebUser webUser, BindingResult bindingResult) {
		webUserValidator.validate(webUser, bindingResult);
		if(userService.userExists(webUser.getEmail())) {
			bindingResult.rejectValue("email", "user.already.exists");
		}
		if(bindingResult.hasErrors()) {
			return "user/register";
		}
		
		String password = userService.generateTempPassword();
		String encodedPswd = passwordEncoder.encode(password);
		User user = userService.convertWebUser(webUser);
		user.setPassword(encodedPswd);
		
		userService.create(user);
		
		try {
			emailService.sendRegistrationEmail(webUser.getEmail(), systemEmail, password);
		} catch(MessagingException e) {
			System.out.println("Registration failed, email not sent.");
		}
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/user/remove")
	public String displayUser(@RequestParam(value = "email", required = true) 
			String email, @RequestHeader(value="referer", defaultValue="") String referer, 
			Model model) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
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
	
	@RequestMapping(value = {"/user/edit", "/user/edit/programs"})
	public String editUser(@RequestParam(value = "email", required = true) 
			String email, @RequestHeader(value="referer", defaultValue="") String referer,
			WebUser webUser, Model model, final HttpServletRequest req) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		User user = userService.getUserByEmail(email);
		model.addAttribute("webUser", new WebUser(user.getEmail(), user.getRoles(), 
				user.getPrograms()));
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if(mapping.contains("programs")) {
			return "user/editPrograms";
		}
		return "user/edit";
	}
	
	@RequestMapping(value = {"/user/edit", "/user/edit/programs"}, 
			method = RequestMethod.POST, params = "submit")
	public String editUser(@Valid WebUser webUser, BindingResult bindingResult,
			final HttpServletRequest req) {
		webUserValidator.validate(webUser, bindingResult);
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if(bindingResult.hasErrors()) {
			System.out.println("EROR");
			System.out.println(bindingResult.getAllErrors());
			if(mapping.contains("programs")) {
				return "user/editPrograms";
			}
			return "user/edit";
		}
		programService.fillPrograms(webUser);
		userService.update(userService.convertWebUser(webUser));
		
		if(mapping.contains("programs")) {
			return "redirect:/program/manage";
		}
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = {"/user/edit", "/user/edit/programs"}, 
			method = RequestMethod.POST, params = "cancel")
	public String cancelUserEdit(@Valid WebUser webUser, BindingResult bindingResult,
			final HttpServletRequest req) {
		String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if(mapping.contains("programs")) {
			return "redirect:/program/manage";
		}
		
		return "redirect:/manage";
	}
	
	@RequestMapping(value = "/user/change/password", method = RequestMethod.GET)
	public String changePassword(@RequestParam(value = "email", required = true) 
			String email, @RequestHeader(value="referer", defaultValue="") String referer,
			Model model) {
		model.addAttribute("webUser", userService.getUserByEmail(email));
		
		return "user/changePassword";
	}
	
	@RequestMapping(value = "/user/change/password", method = RequestMethod.POST)
	public String submitChangePassword(@Valid WebUser webUser, BindingResult bindingResult) {
		userValidator.validate(webUser, bindingResult);
		if(bindingResult.hasErrors()) {
			return "user/changePassword";
		}
		String encodedPswd = passwordEncoder.encode(webUser.getPassword());
		userService.changePassword(encodedPswd, webUser.getEmail());
		
		return "redirect:/user/summary";
	}
	
	//automatically logs a user in after they register for an account
	private void autologin(User user, List<String> roles) {
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(String.join(",", roles));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@ModelAttribute("progTypes")
	public ArrayList<ProgramData> getPrograms() {
		return (ArrayList<ProgramData>) programService.getAll();
	}
	
	@ModelAttribute("roleTypes")
	public ArrayList<String> getRoles(){
		return (ArrayList<String>) userService.getRoles();
	}
}
