package com.maccari.abet.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maccari.abet.domain.entity.Task;

@Controller
@RequestMapping("/task")
public class TaskController {
	@GetMapping(value = "/index")
	public String viewMyTasks() {
		return "task/index";
	}
	
	@GetMapping("/create")
	public String createTask(Task task) {
		return "task/create";
	}
	
	@GetMapping("/viewCreated")
	public String viewCreateTask() {
		return "task/viewCreated";
	}
}
