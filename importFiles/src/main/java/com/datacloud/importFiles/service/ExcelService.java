package com.datacloud.importFiles.service;

import com.datacloud.importFiles.models.Product;
import com.datacloud.importFiles.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private final ProductRepository productRepository;

    public ExcelService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public List<Product> importExcel(MultipartFile file) {

        List<Product> products = new ArrayList<>();

        int linesRead = 0, linesInserted = 0, errors = 0;

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            // Mapeia as colunas dinamicamente
            int colName = -1, colPrice = -1, colQuantity = -1;

            for (Cell cell : headerRow) {
                String column = cell.getStringCellValue().trim();
                if (column.equalsIgnoreCase("Nome")) colName = cell.getColumnIndex();
                else if (column.equalsIgnoreCase("Preco")) colPrice = cell.getColumnIndex();
                else if (column.equalsIgnoreCase("Quantidade")) colQuantity = cell.getColumnIndex();
            }

            if (colName == -1 || colPrice == -1 || colQuantity == -1) {
                throw new RuntimeException("Erro: As colunas Nome, Preço e Quantidade são obrigatórias.");
            }

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                linesRead++;

                try {
                    String name = row.getCell(colName).getStringCellValue().trim();
                    double price = row.getCell(colPrice).getNumericCellValue();
                    double quantity = row.getCell(colQuantity).getNumericCellValue();

                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setQuantity(quantity);

                    products.add(product);
                    linesInserted++;

                } catch (Exception e) {
                    errors++;
                    System.out.println("Erro ao processar linha " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            productRepository.saveAll(products);
            return products;
            //return String.format("Importação concluída: %d linhas lidas, %d inseridas, %d erros.", linesRead, linesInserted, erros);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo Excel", e);
        }
    }
}
