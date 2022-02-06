package com.maccari.abet.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.WebTask;
import com.maccari.abet.domain.entity.WebUser;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.domain.service.TaskService;
import com.maccari.abet.web.validation.TaskValidator;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private TaskValidator taskValidator;

	@GetMapping("/index")
	public String viewMyTasks(Principal principal, Model model) {
		model.addAttribute("tasks", taskService.getAssigned(principal.getName()));
		
		return "task/index";
	}

	@GetMapping("/create")
	public String createTask(Task task) {
		return "task/create";
	}

	@RequestMapping(value = {"/create", "/edit"}, params = { "addRow" })
	public String addAssignee(final Task task, final BindingResult bindingResult,
			final HttpServletRequest req) {
		task.getAssignees().add("");
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}

	@RequestMapping(value = {"/create", "/edit"}, params = { "removeRow" })
	public String removeAssignee(final Task task, final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		task.getAssignees().remove(rowId.intValue());
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}
	
	@RequestMapping(value = {"/create", "/edit"}, params = { "addProgram" })
	public String addProgram(@RequestParam(value = "addProgram", required = true) 
		int id, final Task task, final HttpServletRequest req) {
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    
	    System.out.println(task.getPrograms());
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}
	
	@RequestMapping(value = {"/create", "/edit"}, params = { "removeProgram" })
	public String removeProgram(@RequestParam(value = "removeProgram", required = true) 
		int id, final Task task, final HttpServletRequest req) {
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    
	    System.out.println(task.getPrograms());
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addTask(Principal principal, @Valid Task task, 
			BindingResult bindingResult, Model model, final HttpServletRequest req) {
		taskValidator.validate(task, bindingResult);
		
		int invalidEmail = taskService.userExists(task.getAssignees());
		if(invalidEmail >= 0) {
			bindingResult.rejectValue("assignees", "task.assignees.invalid");
		}
		
		if (bindingResult.hasErrors()) {
		    String mapping = (String) req.getAttribute(
	                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		    if(mapping.contains("create")) {
				return "task/create";
		    }
		    else {
				return "task/edit";
		    }
		}
		
		task.setCoordinator(principal.getName());
		taskService.create(task);

		return "redirect:/";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelTaskCreate() {
		return "redirect:/";
	}

	@GetMapping("/viewCreated")
	public String viewCreatedTasks(Principal principal, Model model) {
		model.addAttribute("tasks", taskService.getCreated(principal.getName()));
		
		return "task/viewCreated";
	}
	
	@GetMapping("/details")
	public String viewTaskDetails(@RequestParam(value = "id", required = true)
			int id, Model model) {
		model.addAttribute("task", taskService.getById(id));
		
		return "task/details";
	}
	
	@GetMapping("/edit")
	public String editTaskDetails(@RequestParam(value = "id", required = true)
			int id, Model model) {
		model.addAttribute("task", taskService.getById(id));
		
		return "task/edit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editTask(Principal principal, @Valid Task task, 
			BindingResult bindingResult, Model model) {
		taskValidator.validate(task, bindingResult);
		
		int invalidEmail = taskService.userExists(task.getAssignees());
		if(invalidEmail >= 0) {
			bindingResult.rejectValue("assignees", "task.assignees.invalid");
		}
		
		if (bindingResult.hasErrors()) {
			return "task/create";
		}
		
		task.setCoordinator(principal.getName());
		taskService.update(task);

		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public String cancelTaskEdit() {
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/delete")
	public String displayTaskForRemoval(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		model.addAttribute("task", taskService.getById(id));
		
		return "task/delete";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteTask(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		taskService.remove(taskService.getById(id));
		
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "cancel")
	public String cancelDeleteTask() {
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/complete")
	public String completeTask(@RequestParam(value = "id", required = true)
			int id, Task task, Model model) {
		model.addAttribute("task", taskService.getById(id));
		
		return "task/complete";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "submit")
	public String submitCompletedTask(@Valid Task task, BindingResult bindingResult) {
		return "redirect:/task/index";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "cancel")
	public String cancelCompletedTask(@Valid Task task, BindingResult bindingResult) {
		return "redirect:/task/index";
	}

	@ModelAttribute("progTypes")
	public ArrayList<Program> getPrograms() {
		return (ArrayList<Program>) programService.getAll();
	}
	
	@ModelAttribute("outcomeTypes")
	public ArrayList<Program> getOutcomes() {
		return (ArrayList<Program>) programService.getAll();
	}
}
