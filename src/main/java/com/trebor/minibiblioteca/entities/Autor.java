package com.trebor.minibiblioteca.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 4, message = "El nombre debe tener al menos 4 caracteres")
    private String nombre;

    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros = new ArrayList<>();
}
