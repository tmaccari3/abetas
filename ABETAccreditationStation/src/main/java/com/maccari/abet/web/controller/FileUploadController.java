package com.maccari.abet.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.exception.IllegalDownloadException;
import com.maccari.abet.exception.StorageFileNotFoundException;
import com.maccari.abet.repository.FileDao;

@Controller
public class FileUploadController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private FileDao fileDao;
	
	@Autowired
	public FileUploadController(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	@GetMapping("/upload")
	public String homepage(Model model) {
		String message = (String) model.getAttribute("message");
		logger.info("MESSAGE: " + message);
		return "file/index";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("file") MultipartFile file, 
			RedirectAttributes attributes, String action) {
		
		if (file.isEmpty() || action.equals("cancel")) { // not file selected or cancelled. 
			attributes.addFlashAttribute("message", "Please select a file.");
			return "redirect:/upload";
		}

		File fileToSave = new File();
		String fileName = null;
		try {
			fileName = StringUtils.cleanPath(file.getOriginalFilename());
			fileToSave.setFileName(fileName); 
			fileToSave.setFileType(file.getContentType());
			fileToSave.setFileSize(file.getSize());
			fileToSave.setData(file.getBytes());
			fileDao.save(fileToSave);
		} catch (IOException e) {
			e.printStackTrace();
		}

		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/";
	}

	@RequestMapping("/download/{id}")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") int id, 
			@RequestHeader(value="referer", defaultValue="") String referer) {
		logger.info("REFERER: " + referer);
        // the path was entered manually
        if(referer == null || referer.isEmpty()) {
        	throw new IllegalDownloadException("ILLEGAL DOWNLOAD");
        }

        File fileFromDB = fileDao.getFileById(id);
        if (fileFromDB != null) {
        	response.setContentType(fileFromDB.getFileType());
			response.addHeader("Content-Disposition", "attachment; filename=" + fileFromDB.getFileName());
			try {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(fileFromDB.getData());
				IOUtils.copy(inputStream, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        }
        else {
        	throw new StorageFileNotFoundException("FILE NOT FOUND");
        }
	}
	
	@RequestMapping(value = "/file/{id}")
	@ResponseBody
	public byte[] displayFile(@PathVariable(value = "id") int id) throws IOException {
		byte[] imageData = fileDao.getFileById(id).getData();

		return imageData;
	}
	
	// handles the two exceptions thrown by this controller
	@ExceptionHandler({StorageFileNotFoundException.class, IllegalDownloadException.class})
	public String handleStorageFileNotFound(Exception exc, Model model) {
		model.addAttribute("message","FILE NOT FOUND: " + exc.getMessage());
		return "error";
	}
}
