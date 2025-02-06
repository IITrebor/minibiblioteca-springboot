package com.trebor.minibiblioteca.services;

import com.trebor.minibiblioteca.entities.Autor;

import java.util.List;
import java.util.Optional;

public interface AutorService {
    Autor guardarAutor(Autor autor);

    Optional<Autor> buscarPorid(Long id);

    Optional<Autor> buscarPorNombre(String nombre);

    List<Autor> listarTodosLosAutores();

    Autor actualizarAutor(Autor autor);

    void eliminarAutor(Long id) throws ClassNotFoundException;

    List<Autor> buscarPorIds(List<Long>ids);

}
