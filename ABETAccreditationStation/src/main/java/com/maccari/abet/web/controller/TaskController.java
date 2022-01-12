package com.maccari.abet.web.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@GetMapping("/viewCreated")
	public String viewCreateTask() {
		return "task/viewCreated";
	}
	
	@ModelAttribute("progTypes")
	public ArrayList<String> getRoles(){
		return (ArrayList<String>) taskService.getPrograms();
	}
}
