package com.trebor.minibiblioteca.repositories;

import com.trebor.minibiblioteca.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository  extends JpaRepository<Categoria,Long> {
    Optional<Categoria> findByNombre(String nombre);
}
