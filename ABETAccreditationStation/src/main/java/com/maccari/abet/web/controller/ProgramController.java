package com.maccari.abet.web.controller;

import java.util.ArrayList;

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
	public String manage(WebProgram webProgram, Model model) {
		model.addAttribute("programs", programService.getAllWebPrograms());

		return "program/index";
	}

	@RequestMapping(value = "/create", params = "addRow")
	public String addAssignee(WebProgram webProgram, Model model) {
		ArrayList<WebProgram> programs = (ArrayList<WebProgram>) programService
				.getAllWebPrograms();
		programs.add(new WebProgram("", true));
		model.addAttribute("programs", programs);

		return "program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitProgram(@Valid WebProgram webProgram, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "program/create";
		}
		programService.create(programService.convertWebProgram(webProgram));

		return "redirect:/program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelProgramAdition(Model model) {
		return "redirect:/program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "remove")
	public String removeProgram(@RequestParam(value = "remove", required = true) 
		int id, Model model) {
		programService.remove(programService.getById(id));

		return "redirect:/program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "outcome")
	public String viewOutcomes(@RequestParam(value = "outcome", required = true) int id) {
		return "redirect:/outcome/index?id=" + id;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "deactivate")
	public String deactivateProgram(@RequestParam(value = "deactivate", required = true) 
		int id, Model model) {
		Program program = programService.getById(id);
		program.setActive(false);
		programService.update(program);

		return "redirect:/program/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "reactivate")
	public String reactivateProgram(@RequestParam(value = "reactivate", required = true) 
		int id, Model model) {
		Program program = programService.getById(id);
		program.setActive(false);
		programService.update(program);

		return "redirect:/program/index";
	}

	/*
	 * @RequestMapping(value = "/create", method = RequestMethod.POST, params =
	 * "submit") public String submitEmail(@Valid Program program, BindingResult
	 * bindingResult) { if(bindingResult.hasErrors()) { return "program/create"; }
	 * 
	 * programService.create(program);
	 * 
	 * return "redirect:/program/index"; }
	 * 
	 * @RequestMapping(value = "/create", method = RequestMethod.POST, params =
	 * "cancel") public String cancelSubmitEmail() { return
	 * "redirect:/program/index"; }
	 */
}
