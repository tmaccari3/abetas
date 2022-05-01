package com.maccari.abet.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error")
public class ErrorController {
	@GetMapping(value = "/")
	public String error(@RequestParam(value = "msg", required = false) String msg,
			Model model) {
		model.addAttribute("msg", msg);
		
		return "error";
	}
}
			