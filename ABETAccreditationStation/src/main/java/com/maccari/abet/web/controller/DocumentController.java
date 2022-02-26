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
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.domain.service.DocumentService;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.web.validation.DocumentValidator;

@Controller
@RequestMapping("/doc")
public class DocumentController {
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private DocumentService docService;
	
	@Autowired
	private DocumentValidator docValidator;
	
	@GetMapping(value = "/index")
	public String viewDocuments(Model model) {
		return "document/index";
	}
	
	@GetMapping(value = "/create")
	public String createDocument(WebDocument webDoc) {
		return "document/create";
	}
	
	@RequestMapping(value = {"/create", "/edit"}, params = "upload")
	public String uploadFile(final WebDocument webDoc, @RequestParam("file") MultipartFile file, 
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
			uploadedFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
			uploadedFile.setFileType(file.getContentType());
			uploadedFile.setFileSize(file.getSize());
			uploadedFile.setData(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("UPLOADED_FILE", uploadedFile);
		model.addAttribute("uploadedFile", uploadedFile);
		
	    return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String addDoc(Principal principal, @Valid WebDocument webDocument, 
			BindingResult bindingResult, Model model, final HttpServletRequest req,
			HttpSession session) {
		webDocument.setUploadedFile((File) session.getAttribute("UPLOADED_FILE"));
		docValidator.validate(webDocument, bindingResult);
		programService.fillPrograms(webDocument);
		
		if(programService.checkPrograms(webDocument.getFullPrograms(), 
				webDocument.getFullOutcomes(), principal.getName())) {
			bindingResult.rejectValue("programs", "programs.invalid");
		}
		
		if (bindingResult.hasErrors()) {
			return "document/create";
		}
		
		webDocument.setAuthor(principal.getName());
		docService.create(docService.webDoctoDoc(webDocument));
		
		session.removeAttribute("UPLOADED_FILE");

		return "redirect:/";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "cancel")
	public String cancelDocCreate() {
		return "redirect:/";
	}
	
	@ModelAttribute("progTypes")
	public ArrayList<Program> getPrograms(Principal principal) {
		return (ArrayList<Program>) programService.getActivePrograms(principal.getName());
	}
}
