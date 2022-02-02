package com.maccari.abet.web.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.WebProgram;
import com.maccari.abet.domain.service.ProgramService;

@Controller
@RequestMapping("/program")
public class ProgramController {
	
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/index")
	public String manage(Model model) {
		model.addAttribute("programs", programService.getAllWebPrograms());
		
		return "program/index";
	}
	
	@RequestMapping(value = "/create", params = { "addRow" })
	public String addAssignee(Model model, WebProgram webProgram) {
		ArrayList<WebProgram> programs = (ArrayList<WebProgram>) programService.getAllWebPrograms();
		programs.add(new WebProgram("", true));
		model.addAttribute("programs", programs);
		
		return "program/index";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitProgram(@Valid Program program, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "program/create";
		}
		programService.create(program);
		
		return "redirect:/program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelProgramAdition(Model model) {
		return "redirect:/program/index";
	}
	
	@RequestMapping(value = "/remove")
	public String removeProgram(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		programService.remove(programService.getById(id));
		
		return "redirect:/program/index";
	}
	
	@RequestMapping(value = "/deactivate")
	public String deactivateProgram(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		Program program = programService.getById(id);
		program.setActive(false);
		programService.update(program);
		
		return "redirect:/program/index";
	}
	
	@RequestMapping(value = "/reactivate")
	public String reactivateProgram(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		Program program = programService.getById(id);
		program.setActive(true);
		programService.update(program);
		
		return "redirect:/program/index";
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
