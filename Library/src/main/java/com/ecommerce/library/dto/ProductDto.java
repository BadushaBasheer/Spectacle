package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name should not be blank")
    private String name;

    @NotBlank(message = "Description should not be blank")
    private String description;

    @NotNull(message = "Price should not be null")
    private Double costPrice;

    @NotNull(message = "Quantity should not be null")
    private Integer currentQuantity;

    @NotNull(message = "Category should not be null")
    private Category category;

    private boolean activated;

    private boolean deleted;

    private List<String> imageUrls;
}
