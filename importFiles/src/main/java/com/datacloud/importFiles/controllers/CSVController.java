package com.datacloud.importFiles.controllers;

import com.datacloud.importFiles.dto.ProductDTO;
import com.datacloud.importFiles.service.CSVService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/api/csv")
public class CSVController {

    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<ProductDTO>> uploadCSV(@RequestParam("file") MultipartFile file) {
        List<ProductDTO> products = csvService.readCSVFile(file);
        return ResponseEntity.ok(products);
    }
}
