package com.trebor.minibiblioteca.repositories;

import com.trebor.minibiblioteca.entities.Categoria;
import com.trebor.minibiblioteca.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {

    Optional<Libro> findByTitulo(String nombre);
    List<Libro> findByCategoria(Categoria categoria);
}
