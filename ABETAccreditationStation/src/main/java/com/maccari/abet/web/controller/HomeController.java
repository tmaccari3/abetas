package com.maccari.abet.web.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping(value = "/")
	public String homePage(Principal principal, Model model) {
		String email = "Welcome!";
		if(principal != null) {
			principal.getName();
			email = "Welcome, " + principal.getName();
		}
		model.addAttribute("msg", email);
		
		return "home/index";
	}
	
	@RequestMapping(value = "/home/edit")
	public String edit() {
		return "home/edit";
	}
}
