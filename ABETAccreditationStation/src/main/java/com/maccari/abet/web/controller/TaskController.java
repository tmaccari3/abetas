package com.maccari.abet.web.controller;

import java.util.ArrayList;

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

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;

	@GetMapping(value = "/index")
	public String viewMyTasks() {
		return "task/index";
	}

	@GetMapping("/create")
	public String createTask(Task task) {
		
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
	public String addTask(@Valid Task task, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "user/register";
		}

		System.out.println(task);

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
