package com.trebor.minibiblioteca.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 4, message = "El nombre debe tener al menos 4 caracteres")
    private String nombre;

    @OneToMany(mappedBy = "categoria",cascade = CascadeType.ALL)
    private List<Libro> libros = new ArrayList<>();



}
