package com.maccari.abet.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcomeData;
import com.maccari.abet.domain.entity.web.WebStudentOutcome;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.exception.IllegalDownloadException;

@Controller
@RequestMapping("/outcome")
public class OutcomeController {
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/index")
	public String manage(@RequestParam(value = "id", required = true) int id,
			@RequestHeader(value="referer", defaultValue="") String referer,
			WebStudentOutcome webStudentOutcome, Model model, HttpSession session) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		ProgramData program = programService.getById(id);
		model.addAttribute("outcomes", programService.getAllWebOutcomes(program));
		model.addAttribute("progName", program.getName());
		session.setAttribute("PROGRAM", id);
		
		return "program/outcome";
	}

	@RequestMapping(value = "/create", params = "addRow")
	public String addOutcome(WebStudentOutcome webStudentOutcome, Model model,
			HttpSession session) {
		int progId = (int) session.getAttribute("PROGRAM");
		ProgramData program = programService.getById(progId);
		List<WebStudentOutcome> outcomes = programService.getAllWebOutcomes(program);
		outcomes.add(new WebStudentOutcome("", true));
		model.addAttribute("outcomes", outcomes);
		model.addAttribute("progName", program.getName());

		return "program/outcome";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitStudentOutcome(@Valid WebStudentOutcome webStudentOutcome, 
			BindingResult bindingResult, HttpSession session, Model model) {
		int id = (int) session.getAttribute("PROGRAM");
		if (bindingResult.hasErrors()) {
			ProgramData program = programService.getById(id);
			model.addAttribute("outcomes", programService.getAllWebOutcomes(program));
			model.addAttribute("progName", program.getName());
			
			return "/program/outcome";
		}
		webStudentOutcome.setProgId(id);
		programService.createOutcome(programService.convertWebOutcome(webStudentOutcome));

		return "redirect:/outcome/index?id=" + id;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelStudentOutcomeAdition(HttpSession session) {
		int id = (int) session.getAttribute("PROGRAM");

		return "redirect:/outcome/index?id=" + id;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "remove")
	public String removeStudentOutcome(@RequestParam(value = "remove", required = true) 
			int id, @RequestHeader(value="referer", defaultValue="") String referer, 
			Model model, HttpSession session) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		programService.removeOutcome(programService.getOutcomeById(id));
		int programId = (int) session.getAttribute("PROGRAM");

		return "redirect:/outcome/index?id=" + programId;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "deactivate")
	public String deactivateStudentOutcome(@RequestParam(value = "deactivate", required = true) 
			int id, @RequestHeader(value="referer", defaultValue="") String referer,
			Model model, HttpSession session) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		StudentOutcomeData studentOutcome = programService.getOutcomeById(id);
		studentOutcome.setActive(false);
		programService.updateOutcome(studentOutcome);
		int programId = (int) session.getAttribute("PROGRAM");
		
		return "redirect:/outcome/index?id=" + programId;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "reactivate")
	public String reactivateStudentOutcome(@RequestParam(value = "reactivate", required = true) 
			int id, @RequestHeader(value="referer", defaultValue="") String referer,
			HttpSession session) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		StudentOutcomeData studentOutcome = programService.getOutcomeById(id);
		studentOutcome.setActive(true);
		programService.updateOutcome(studentOutcome);
		int programId = (int) session.getAttribute("PROGRAM");
		
		return "redirect:/outcome/index?id=" + programId;
	}
}
