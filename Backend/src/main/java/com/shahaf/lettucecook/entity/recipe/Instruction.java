package com.shahaf.lettucecook.entity.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "instructions")
@Data
public class Instruction {
    @Id
    @GeneratedValue
    private Long id;
    private Long index;
    private String description;
}