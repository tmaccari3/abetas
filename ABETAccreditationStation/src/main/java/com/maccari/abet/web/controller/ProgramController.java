package com.maccari.abet.web.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.service.ProgramService;

@Controller
@RequestMapping("/program")
public class ProgramController {
	
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/index")
	public String manage(Model model) {
		model.addAttribute("programs", programService.getAll());
		
		return "program/index";
	}
	
	@RequestMapping(value = "/create")
	public String create(Program program) {
		return "program/create";
	}
	
	@RequestMapping(value = "/create", params = { "addRow" })
	public String addAssignee(Model model) {
		ArrayList<Program> programs = (ArrayList<Program>) programService.getAll();
		programs.add(new Program(""));
		model.addAttribute("programs", programs);
		
		return "program/index";
	}

	@RequestMapping(value = {"/create", "/edit"}, params = { "removeRow" })
	public String removeAssignee(Model model,  final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		ArrayList<Program> programs = (ArrayList<Program>) model.getAttribute("programs");
		programs.remove(programs.size());

		return "program/index";
	}
	
	/*@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitEmail(@Valid Program program, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "program/create";
		}
		
		programService.create(program);
		
		return "redirect:/program/index";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelSubmitEmail() {
		return "redirect:/program/index";
	}*/
}
