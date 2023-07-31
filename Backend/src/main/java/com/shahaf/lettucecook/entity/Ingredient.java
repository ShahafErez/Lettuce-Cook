package com.shahaf.lettucecook.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ingredients")
@Data
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String unit;
    private Float amount;

}
