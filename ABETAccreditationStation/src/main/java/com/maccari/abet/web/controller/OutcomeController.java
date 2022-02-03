package com.maccari.abet.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maccari.abet.domain.entity.WebStudentOutcome;
import com.maccari.abet.domain.service.ProgramService;

@Controller
@RequestMapping("/outcome")
public class OutcomeController {
	@Autowired
	private ProgramService programService;
	
	@RequestMapping(value = "/index")
	public String manage(@RequestParam(value = "id", required = true)
		int id, WebStudentOutcome webStudentOutcome, Model model) {
		model.addAttribute("program", programService.getById(id));
		
		return "program/outcome";
	}

	/*@RequestMapping(value = "/create", params = "addRow")
	public String addAssignee(WebStudentOutcome webStudentOutcome, Model model) {
		ArrayList<WebStudentOutcome> studentOutcomes = (ArrayList<WebStudentOutcome>)
				programService.getAllOutcomesForProgram(0)
		studentOutcomes.add(new WebStudentOutcome("", true));
		model.addAttribute("studentOutcomes", studentOutcomes);

		return "outcome/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "submit")
	public String submitStudentOutcome(@Valid WebStudentOutcome webStudentOutcome, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "outcome/create";
		}
		studentOutcomeService.create(programService.convertWebStudentOutcome(webStudentOutcome));

		return "redirect:/outcome/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelStudentOutcomeAdition(Model model) {
		return "redirect:/outcome/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "remove")
	public String removeStudentOutcome(@RequestParam(value = "remove", required = true) 
		int id, Model model) {
		programService.remove(programService.getById(id));

		return "redirect:/outcome/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "deactivate")
	public String deactivateStudentOutcome(@RequestParam(value = "deactivate", required = true) 
		int id, Model model) {
		StudentOutcome studentOutcome = programService.getById(id);
		studentOutcome.setActive(false);
		programService.update(studentOutcome);

		return "redirect:/outcome/index";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "reactivate")
	public String reactivateStudentOutcome(@RequestParam(value = "reactivate", required = true) 
		int id, Model model) {
		StudentOutcome studentOutcome = programService.getById(id);
		studentOutcome.setActive(true);
		programService.update(studentOutcome);

		return "redirect:/outcome/index";
	}*/
}
