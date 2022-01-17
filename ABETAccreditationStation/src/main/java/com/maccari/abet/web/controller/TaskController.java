package com.maccari.abet.web.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
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

import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.service.TaskService;
import com.maccari.abet.web.validation.TaskValidator;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskValidator taskValidator;

	@GetMapping(value = "/index")
	public String viewMyTasks(Model model) {
		List<Task> tasks = taskService.getAll();
		model.addAttribute("tasks", tasks);
		
		return "task/index";
	}

	@GetMapping("/create")
	public String createTask(Task task) {
		Instant instant = Instant.now();
		Timestamp ts = instant != null ? Timestamp.from(instant) : null;
		System.out.println(ts);
		return "task/create";
	}

	@RequestMapping(value = "/create", params = { "addRow" })
	public String addAssignee(final Task task, final BindingResult bindingResult) {
		task.getAssignees().add("");

		return "task/create";
	}

	@RequestMapping(value = "/create", params = { "removeRow" })
	public String removeAssignee(final Task task, final BindingResult bindingResult, 
			final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		System.out.println(rowId);
		task.getAssignees().remove(rowId.intValue());

		return "task/create";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addTask(Principal principal, @Valid Task task, 
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
		taskService.create(task);

		return "redirect:/";
	}

	@GetMapping("/viewCreated")
	public String viewCreateTask() {
		return "task/viewCreated";
	}

	@ModelAttribute("progTypes")
	public ArrayList<String> getRoles() {
		return (ArrayList<String>) taskService.getPrograms();
	}
}
