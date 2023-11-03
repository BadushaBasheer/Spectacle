package com.ecommerce.library.model;

import com.ecommerce.library.annotation.NoWhiteSpace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Slf4j
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private boolean is_deleted;

    private boolean is_activated;

    public Category(String name) {
        this.name = name;
        this.is_activated = true;
        this.is_deleted = false;
    }


}
