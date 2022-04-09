package com.maccari.abet.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.domain.service.DocumentService;
import com.maccari.abet.domain.service.FileService;
import com.maccari.abet.domain.service.TaskService;
import com.maccari.abet.web.validation.DocumentValidator;

@Controller
@RequestMapping("/task/doc")
public class TaskCompleteController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private DocumentService docService;
	
	@Autowired
	private DocumentValidator docValidator;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/complete")
	public String completeTask(@RequestParam(value = "id", required = true)
			int id, WebDocument webDocument, Model model, HttpSession session) {
		Task task = getTaskById(id);
		model.addAttribute("task", task);
		session.setAttribute("TASK_ID", task.getId());
		
		return "task/complete";
	}
	
	@RequestMapping(value = {"/complete"}, params = "upload")
	public String uploadFile(Principal principal, final WebDocument webDocument, @RequestParam("file") MultipartFile file, 
			RedirectAttributes attributes, String upload, Model model, HttpSession session) {
		int id = (int) session.getAttribute("TASK_ID");
		model.addAttribute("task", getTaskById(id));
		if (file.isEmpty() || upload.equals("cancel")) {
			session.removeAttribute("UPLOADED_FILE");
			model.addAttribute("message", "*Please select a file to upload.*");
			model.addAttribute("uploadedFile", null);
			
			return "task/complete";
		}
		
		File uploadedFile = new File();
		try {
			uploadedFile.setAuthor(principal.getName());
			uploadedFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
			uploadedFile.setFileType(file.getContentType());
			uploadedFile.setFileSize(file.getSize());
			uploadedFile.setData(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("UPLOADED_FILE", uploadedFile);
		model.addAttribute("uploadedFile", uploadedFile);
		
		return "task/complete";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "submit")
	public String submitCompletedTask(Principal principal, @Valid WebDocument webDocument, 
			BindingResult bindingResult, Model model, HttpSession session) {
		int taskId = (int) session.getAttribute("TASK_ID");
		Task task = getTaskById(taskId);
		
		webDocument.setUploadedFile((File) session.getAttribute("UPLOADED_FILE"));
		docService.fillDocWithTask(webDocument, task);
		docValidator.validate(webDocument, bindingResult);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("task", task);
			
			return "task/complete";
		}
		
		webDocument.setAuthor(principal.getName());
		webDocument.setTask(true);
		webDocument.setTaskId(taskId);
		
		docService.create(docService.webDoctoDoc(webDocument));
		task.setSubmitted(true);
		taskService.updateSubmit(task);
		
		session.removeAttribute("TASK_ID");
		
		return "redirect:/task/index";
	}
	
	private Task getTaskById(int id) {
		Task task = taskService.getById(id);
		File file = task.getFile();
		if(file != null) {
			task.setFile(fileService.getFileById(file.getId()));
		}
		
		return task;
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST, params = "cancel")
	public String cancelCompletedTask() {
		return "redirect:/task/index";
	}
	
	@RequestMapping(value = "/submit")
	public String completeTask(@RequestParam(value = "id", required = true)
		int id, Model model, HttpSession session) {
		Task task = getTaskById(id);
		ArrayList<Document> documents = (ArrayList<Document>) docService.getDocsForTask(id);
		fileService.fillFiles(documents);
		model.addAttribute("documents", documents);
		model.addAttribute("task", task);
		
		return "task/submit";
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String verifyTaskSubmission(@RequestParam(value = "id", required = true)
		int id, Model model, HttpSession session) {
		ArrayList<Document> documents = (ArrayList<Document>) docService.getDocsForTask(id);
		Task task = getTaskById(id);
		
		if(documents.isEmpty()) {
			model.addAttribute("invalid_submit", "No submission has been made, "
					+ "cannot submit Task as complete.");
			model.addAttribute("documents", documents);
			model.addAttribute("task", task);
		}
		
		task.setComplete(true);
		taskService.updateComplete(task);
		
		return "redirect:/task/viewCreated";
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST, params = "cancel")
	public String cancelVerifyTaskComplete() {
		return "redirect:/task/viewCreated";
	}
	
	@ModelAttribute("uploadedFile")
	public File getUploadedFiles(HttpSession session) {
		return (File) session.getAttribute("UPLOADED_FILE");
	}
	
	@ModelAttribute("tags")
	public ArrayList<String> getTags() {
		return (ArrayList<String>) docService.getAllTags();
	}
}
