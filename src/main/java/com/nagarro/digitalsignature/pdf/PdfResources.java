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
	private final SigningService signingService;

	public PdfResources(SigningService signingService) {
		this.signingService = signingService;
	}

	@PostMapping(value = "/sign", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity signPdf(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		try {
			byte[] pdfToSign = file.getBytes();
			byte[] signedPdf = this.signingService.signPdf(pdfToSign);
			return ResponseEntity.ok(signedPdf);
		} catch (IOException e) {
			log.error("Cannot generate PDF file", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
