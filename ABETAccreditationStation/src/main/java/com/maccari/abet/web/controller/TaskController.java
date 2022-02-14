package com.maccari.abet.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.WebTask;
import com.maccari.abet.domain.service.FileService;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.domain.service.TaskService;
import com.maccari.abet.web.validation.TaskValidator;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FileService fileService;
	
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
	public String createTask(WebTask webTask) {
		return "task/create";
	}

	@RequestMapping(value = {"/create", "/edit"}, params = "addRow")
	public String addAssignee(final WebTask webTask, final BindingResult bindingResult,
			final HttpServletRequest req) {
		webTask.getAssignees().add("");
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}

	@RequestMapping(value = {"/create", "/edit"}, params = "removeRow")
	public String removeAssignee(final WebTask webTask, final HttpServletRequest req) {
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		webTask.getAssignees().remove(rowId.intValue());
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}
	
	@RequestMapping(value = {"/create", "/edit"}, params = "upload")
	public String uploadFile(final WebTask webTask, @RequestParam("file") MultipartFile file, 
			RedirectAttributes attributes, String upload, Model model, HttpSession session,
			final HttpServletRequest req) {
		if (file.isEmpty() || upload.equals("cancel")) {
			session.removeAttribute("UPLOADED_FILE");
			model.addAttribute("message", "*Please select a file to upload.*");
			model.addAttribute("uploadedFile", null);
			
			return "task/create";
		}
		
		File uploadedFile = new File();
		try {
			uploadedFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
			uploadedFile.setFileType(file.getContentType());
			uploadedFile.setFileSize(file.getSize());
			uploadedFile.setData(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("UPLOADED_FILE", uploadedFile);
		model.addAttribute("uploadedFile", uploadedFile);
		
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    if(mapping.contains("create")) {
			return "task/create";
	    }

	    else {
	    	return "task/edit";
	    }
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addTask(Principal principal, @Valid WebTask webTask, 
			BindingResult bindingResult, Model model, final HttpServletRequest req,
			HttpSession session) {
		webTask.setUploadedFile((File) session.getAttribute("UPLOADED_FILE"));
		taskValidator.validate(webTask, bindingResult);
		
		int invalidEmail = taskService.userExists(webTask.getAssignees());
		if(invalidEmail >= 0) {
			bindingResult.rejectValue("assignees", "task.assignees.invalid");
		}
		programService.fillPrograms(webTask);
		if(programService.checkOutcomes(webTask.getFullPrograms(), webTask.getFullOutcomes())) {
			bindingResult.rejectValue("programs", "task.programs.invalid");
		}
		
		if (bindingResult.hasErrors()) {
			return "task/create";
		}
		
		webTask.setCoordinator(principal.getName());
		taskService.create(taskService.webTaskToTask(webTask));
		
		session.removeAttribute("UPLOADED_FILE");

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
	
	@GetMapping("/edit")
	public String editTaskDetails(@RequestParam(value = "id", required = true)
			int id, Model model) {
		WebTask webTask = taskService.taskToWebTask(taskService.getById(id));
		model.addAttribute("webTask", webTask);
		
		return "task/edit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editTask(Principal principal, @Valid WebTask webTask, 
			BindingResult bindingResult, Model model, HttpSession session) {
		webTask.setUploadedFile((File) session.getAttribute("UPLOADED_FILE"));
		taskValidator.validate(webTask, bindingResult);
		int invalidEmail = taskService.userExists(webTask.getAssignees());
		if(invalidEmail >= 0) {
			bindingResult.rejectValue("assignees", "task.assignees.invalid");
		}
		programService.fillPrograms(webTask);
		if(programService.checkOutcomes(webTask.getFullPrograms(), webTask.getFullOutcomes())) {
			bindingResult.rejectValue("programs", "task.programs.invalid");
		}
		
		if (bindingResult.hasErrors()) {
			return "task/edit";
		}
		
		webTask.setCoordinator(principal.getName());
		taskService.update(taskService.webTaskToTask(webTask));
		
		session.removeAttribute("UPLOADED_FILE");

		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public String cancelTaskEdit() {
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/delete")
	public String displayTaskForRemoval(@RequestParam(value = "id", required = true) 
		int id, Model model) {
		Task task = taskService.getById(id);
		File file = task.getFile();
		if(file != null) {
			task.setFile(fileService.getFileById(file.getId()));
		}
		model.addAttribute("task", task);
		
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
	
	@GetMapping("/details")
	public String viewTaskDetails(@RequestParam(value = "id", required = true)
			int id, Model model) {
		Task task = taskService.getById(id);
		File file = task.getFile();
		if(file != null) {
			task.setFile(fileService.getFileById(file.getId()));
		}
		model.addAttribute("task", task);
		
		return "task/details";
	}
	
	@RequestMapping(value = "/complete")
	public String completeTask(@RequestParam(value = "id", required = true)
			int id, Model model) {
		Task task = taskService.getById(id);
		File file = task.getFile();
		if(file != null) {
			task.setFile(fileService.getFileById(file.getId()));
		}
		model.addAttribute("task", task);
		
		return "task/complete";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "submit")
	public String submitCompletedTask() {
		return "redirect:/task/index";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "cancel")
	public String cancelCompletedTask() {
		return "redirect:/task/index";
	}

	@ModelAttribute("progTypes")
	public ArrayList<Program> getPrograms() {
		return (ArrayList<Program>) programService.getActivePrograms();
	}
	
	@ModelAttribute("uploadedFile")
	public File getUploadedFiles(HttpSession session) {
		return (File) session.getAttribute("UPLOADED_FILE");
	}
}
