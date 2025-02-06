package com.trebor.minibiblioteca.services;

import com.trebor.minibiblioteca.entities.Editorial;

import java.util.List;
import java.util.Optional;

public interface EditorialService {

    Editorial guardarEditorial(Editorial editorial);

    Optional<Editorial> buscarPorId(Long id);

    Optional<Editorial> buscarPorNombre(String nombre);

    List<Editorial> listarTodasLasEditoriales();

    Editorial actualizarCategoria(Editorial editorial);

    void eliminarCategoria(Long id);
}
