package com.maccari.abet.web.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.service.ProgramService;

@Controller
@RequestMapping("/outcome")
public class OutcomeController {
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/index")
	public String manage(@RequestParam(value = "id", required = true) int id,
			StudentOutcome studentOutcome, Model model, HttpSession session) {
		model.addAttribute("program", programService.getById(id));
		session.setAttribute("PROGRAM", id);
		
		return "program/outcome";
	}

	@RequestMapping(value = "/create", params = "addRow")
	public String addOutcome(StudentOutcome studentOutcome, Model model,
			HttpSession session) {
		Program program = programService.getById((int) session.getAttribute("PROGRAM"));
		ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>) program.getOutcomes();
		outcomes.add(new StudentOutcome("", true));
		model.addAttribute("program", program);

		return "program/outcome";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitStudentOutcome(@Valid StudentOutcome studentOutcome, 
			BindingResult bindingResult, HttpSession session, Model model) {
		int id = (int) session.getAttribute("PROGRAM");
		if (bindingResult.hasErrors()) {
			model.addAttribute("program", programService.getById(id));
			
			return "/program/outcome";
		}
		studentOutcome.setProgramId(id);
		programService.createOutcome(studentOutcome);

		return "redirect:/outcome/index?id=" + id;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelStudentOutcomeAdition(HttpSession session) {
		int id = (int) session.getAttribute("PROGRAM");

		return "redirect:/outcome/index?id=" + id;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "remove")
	public String removeStudentOutcome(@RequestParam(value = "remove", required = true) 
		int id, Model model, HttpSession session) {
		programService.removeOutcome(programService.getOutcomeById(id));
		int programId = (int) session.getAttribute("PROGRAM");

		return "redirect:/outcome/index?id=" + programId;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "deactivate")
	public String deactivateStudentOutcome(@RequestParam(value = "deactivate", required = true) 
		int id, Model model, HttpSession session) {
		StudentOutcome studentOutcome = programService.getOutcomeById(id);
		studentOutcome.setActive(false);
		programService.updateOutcome(studentOutcome);
		int programId = (int) session.getAttribute("PROGRAM");
		
		return "redirect:/outcome/index?id=" + programId;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "reactivate")
	public String reactivateStudentOutcome(@RequestParam(value = "reactivate", required = true) 
		int id, HttpSession session) {
		StudentOutcome studentOutcome = programService.getOutcomeById(id);
		studentOutcome.setActive(true);
		programService.updateOutcome(studentOutcome);
		int programId = (int) session.getAttribute("PROGRAM");
		
		return "redirect:/outcome/index?id=" + programId;
	}
}
