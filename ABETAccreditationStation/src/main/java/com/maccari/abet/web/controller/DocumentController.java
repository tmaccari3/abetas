package com.maccari.abet.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doc")
public class DocumentController {
	@GetMapping(value = "/index")
	public String viewDocuments() {
		return "documents/index";
	}
	
	@GetMapping(value = "/create")
	public String createDocument() {
		return "documents/create";
	}
}
