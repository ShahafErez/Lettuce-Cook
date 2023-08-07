package com.shahaf.lettucecook.entity.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
}