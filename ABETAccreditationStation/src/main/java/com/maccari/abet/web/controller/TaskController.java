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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcomeData;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.relation.task.TaskAssignee;
import com.maccari.abet.domain.entity.web.WebEmail;
import com.maccari.abet.domain.entity.web.WebTask;
import com.maccari.abet.domain.service.FileService;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.domain.service.ReminderService;
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
	private ReminderService reminderService;
	
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
	public String uploadFile(Principal principal, final WebTask webTask, @RequestParam("file") MultipartFile file, 
			RedirectAttributes attributes, String upload, Model model, HttpSession session,
			final HttpServletRequest req) {
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    String result = "task/create";
	    if(mapping.contains("edit")) {
			result = "task/edit";
	    }

		if (file.isEmpty() || upload.equals("cancel")) {
			session.removeAttribute("UPLOADED_FILE");
			model.addAttribute("message", "*Please select a file to upload.*");
			model.addAttribute("uploadedFile", null);
			
			return result;
		}
		
		File uploadedFile = new File();
		try {
			uploadedFile.setAuthor(principal.getName());
			uploadedFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
			uploadedFile.setFileType(file.getContentType());
			uploadedFile.setFileSize(file.getSize());
			uploadedFile.setData(file.getBytes());
		} catch (IOException e) {
			System.out.println("Error uploading file.");
			e.printStackTrace();
		}
		
		session.setAttribute("UPLOADED_FILE", uploadedFile);
		model.addAttribute("uploadedFile", uploadedFile);
		
		return result;
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
		if(programService.checkPrograms(webTask.getFullPrograms(), 
				webTask.getFullOutcomes(), principal.getName())) {
			bindingResult.rejectValue("programs", "programs.invalid");
		}
		
		if (bindingResult.hasErrors()) {
			return "task/create";
		}
		
		webTask.setCoordinator(principal.getName());
		webTask.setId(taskService.createAndGetId(taskService.webTaskToTask(webTask)));
		
		session.removeAttribute("UPLOADED_FILE");
		
		System.out.println("here's id: " + webTask.getId());
		scheduleReminders(webTask);

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
	public String editTaskDetails(@RequestParam(value = "id", required = true) int id, 
			@RequestHeader(value="referer", defaultValue="") String referer, Model model) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		WebTask webTask = taskService.taskToWebTask(taskService.getById(id));
		File file = null;
		if(webTask.getUploadedFile() != null) {
			file = fileService.getFileById(webTask.getUploadedFile().getId());
		}
		model.addAttribute("webTask", webTask);
		model.addAttribute("uploadedFile", file);
		
		return "task/edit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editTask(Principal principal, @Valid WebTask webTask, 
			BindingResult bindingResult, Model model, HttpSession session) {
		webTask.setUploadedFile((File) session.getAttribute("UPLOADED_FILE"));
		taskValidator.validate(webTask, bindingResult);
		
		if(taskService.userExists(webTask.getAssignees()) >= 0) {
			bindingResult.rejectValue("assignees", "task.assignees.invalid");
		}
		programService.fillPrograms(webTask);
		if(programService.checkPrograms(webTask.getFullPrograms(), 
				webTask.getFullOutcomes(), principal.getName())) {
			bindingResult.rejectValue("programs", "programs.invalid");
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
	public String displayTaskForRemoval(@RequestParam(value = "id", required = true) int id, 
			@RequestHeader(value="referer", defaultValue="") String referer, Model model) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		model.addAttribute("task", taskService.getFullTaskById(id));
		
		return "task/delete";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteTask(@RequestParam(value = "id", required = true) int id,
			@RequestHeader(value="referer", defaultValue="") String referer, Model model) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		Task task = taskService.getById(id);
		taskService.remove(task);
		
		for(TaskAssignee assignee : task.getAssignees()) {
			System.out.println("About to delete: " + assignee.getEmail() + task.getId());
			reminderService.deleteJob(assignee.getEmail() + task.getId());
		}
		
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "cancel")
	public String cancelDeleteTask() {
		return "redirect:/task/viewCreated";
	}
	
	@GetMapping("/details")
	public String viewTaskDetails(@RequestParam(value = "id", required = true) int id, 
			@RequestHeader(value="referer", defaultValue="") String referer, Model model) {
		model.addAttribute("task", taskService.getFullTaskById(id));
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		return "task/details";
	}
	
	public void scheduleReminders(WebTask task) {
		WebEmail email = new WebEmail();
		
		for(String emailAddress : task.getAssignees()) {
			System.out.println("Scheduling for " + emailAddress + "...");
			email.setTo(emailAddress);
			//reminderService.sendImmediately(email, task);
			reminderService.scheduleReminder(email, task);
		}
	}

	@ModelAttribute("progTypes")
	public ArrayList<ProgramData> getPrograms(Principal principal) {
		return (ArrayList<ProgramData>) programService.getActivePrograms(principal.getName());
	}
	
	@ModelAttribute("uploadedFile")
	public File getUploadedFiles(HttpSession session) {
		return (File) session.getAttribute("UPLOADED_FILE");
	}
}
