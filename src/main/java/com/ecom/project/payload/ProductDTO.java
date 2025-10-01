package com.ecom.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productID;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private double discount;

}
