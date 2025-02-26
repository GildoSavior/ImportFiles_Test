package com.datacloud.importFiles.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String name;
    private double price;
    private double quantity;

    public ProductDTO(String name, double price, double quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductDTO{name='" + name + "', price=" + price + ", quantity=" + quantity + "}";
    }
}
