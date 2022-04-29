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
import com.maccari.abet.domain.entity.web.DocumentSearch;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.domain.service.DocumentService;
import com.maccari.abet.domain.service.ProgramService;
import com.maccari.abet.exception.IllegalDownloadException;
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
	public String viewDocuments(DocumentSearch documentSearch, Model model) {
		model.addAttribute("recentDocuments", docService.getRecentDocuments(documentSearch.getRecentCount()));
		model.addAttribute("searchDocuments", docService.getBySearch(documentSearch));
		
		return "document/index";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String searchDocuments(@Valid DocumentSearch documentSearch, Model model, 
			BindingResult bindingResult) {
		System.out.println(documentSearch.getTags());
		if (bindingResult.hasErrors()) {
			return "document/index";
		}
		
		model.addAttribute("recentDocuments", docService.getRecentDocuments(documentSearch.getRecentCount()));
		model.addAttribute("searchDocuments", docService.getBySearch(documentSearch));
		
		return "document/index";
	}
	
	@GetMapping(value = "/create")
	public String createDocument(WebDocument webDoc) {
		return "document/create";
	}
	
	@GetMapping(value = "/details")
	public String documentDetails(@RequestParam(value = "id", required = true) int id, 
			@RequestHeader(value="referer", defaultValue="") String referer, Model model) {
		if(referer == null || referer.isEmpty()) {
        	return "redirect:/error/";
        }
		
		model.addAttribute("document", docService.getFullDocById(id));

		return "document/details";
	}
	
	@RequestMapping(value = {"/create", "/edit"}, params = "upload")
	public String uploadFile(final WebDocument webDoc, @RequestParam("file") MultipartFile file, 
			RedirectAttributes attributes, String upload, Model model, HttpSession session,
			final HttpServletRequest req) {
	    String mapping = (String) req.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
	    String result = "document/create";
	    if(mapping.contains("edit")) {
			result = "document/edit";
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
	
	@ModelAttribute("tags")
	public ArrayList<String> getTags() {
		return (ArrayList<String>) docService.getAllTags();
	}
	
	@ModelAttribute("progTypes")
	public ArrayList<ProgramData> getPrograms(Principal principal) {
		return (ArrayList<ProgramData>) programService.getActivePrograms(principal.getName());
	}
}
