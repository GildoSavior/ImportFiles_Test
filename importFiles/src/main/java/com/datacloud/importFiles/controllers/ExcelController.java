package com.datacloud.importFiles.controllers;

import com.datacloud.importFiles.models.Product;
import com.datacloud.importFiles.service.ExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: O arquivo está vazio!");
        }

        List<Product> result = excelService.importExcel(file);
        return ResponseEntity.ok(result);
    }
}
