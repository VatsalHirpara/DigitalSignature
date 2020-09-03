package com.nagarro.digitalsignature.pdf;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.digitalsignature.signature.SigningService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pdf")
@Slf4j
public class PdfResources {

    private final PdfService pdfService;
    private final SigningService signingService;

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
}
