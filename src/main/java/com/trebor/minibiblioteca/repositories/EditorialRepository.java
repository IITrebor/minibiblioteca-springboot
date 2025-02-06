package com.trebor.minibiblioteca.repositories;

import com.trebor.minibiblioteca.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EditorialRepository extends JpaRepository<Editorial,Long> {
    Optional<Editorial> findByNombre(String nombre);
}
