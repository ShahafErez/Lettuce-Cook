package com.shahaf.lettucecook.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "instructions")
@Data
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long index;
    private String description;
}