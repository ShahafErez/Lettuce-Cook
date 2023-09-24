package com.shahaf.lettucecook.entity.recipe;

import jakarta.persistence.*;
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
    private Integer index;
    @Column(columnDefinition = "TEXT")
    private String description;
}