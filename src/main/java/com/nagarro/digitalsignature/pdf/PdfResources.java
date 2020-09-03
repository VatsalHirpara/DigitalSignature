package com.nagarro.digitalsignature.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nagarro.digitalsignature.signature.SigningService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pdf")
@Slf4j
public class PdfResources {

    private final PdfService pdfService;
    private final SigningService signingService;
    
	static String fileBasePath="C:\\Users\\vatsalhirpara\\Downloads\\documentsign\\documentsign\\src\\main\\resources\\files\\";


    public PdfResources(PdfService pdfService, SigningService signingService) {
        this.pdfService = pdfService;
        this.signingService = signingService;
    }

    @GetMapping(value = "/export", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity exportPdf() {
    	System.out.println("in get mapping");
        try {
            byte[] pdfToSign = this.pdfService.generatePdf();
            byte[] signedPdf = this.signingService.signPdf(pdfToSign);

            return ResponseEntity.ok(signedPdf);
        } catch (IOException e) {
            log.error("Cannot generate PDF file", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    @PostMapping(value = "/signpdf", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity signPdfDocument(@RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get(fileBasePath + fileName);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
			
		
		
		
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/download/")
				.path(fileName)
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}
    
    
    
    @GetMapping("/download/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocal(@PathVariable String fileName) {
		Path path = Paths.get(fileBasePath + fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
