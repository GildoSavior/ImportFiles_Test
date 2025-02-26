package com.datacloud.importFiles.service;

import com.datacloud.importFiles.dto.ProductDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    public List<ProductDTO> readCSVFile(MultipartFile file) {
        List<ProductDTO> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader() // Ignora a primeira linha (cabeçalho)
                     .withTrim())) { // Remove espaços em branco

            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("name");
                double price = Double.parseDouble(csvRecord.get("price"));
                int quantity = Integer.parseInt(csvRecord.get("quantity"));

                ProductDTO product = new ProductDTO(name, price, quantity);
                products.add(product);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar CSV: " + e.getMessage());
        }

        return products;
    }
}
