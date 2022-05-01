package com.maccari.abet.web.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.service.ProgramService;

@Controller
public class HomeController {
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/")
	public String homePage(Model model) {
		return "home/index";
	}
	
	@ModelAttribute("progTypes")
	public ArrayList<ProgramData> getPrograms() {
		return (ArrayList<ProgramData>) programService.getAll();
	}
}
