package com.trebor.minibiblioteca.services.impl;

import com.trebor.minibiblioteca.entities.Editorial;
import com.trebor.minibiblioteca.repositories.EditorialRepository;
import com.trebor.minibiblioteca.services.EditorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditorialServiceImpl   implements EditorialService {


    @Autowired
    EditorialRepository editorialRepository;

    @Override
    public Editorial guardarEditorial(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    @Override
    public Optional<Editorial> buscarPorId(Long id) {
        return editorialRepository.findById(id);
    }

    @Override
    public Optional<Editorial> buscarPorNombre(String nombre) {
        return editorialRepository.findByNombre(nombre);
    }

    @Override
    public List<Editorial> listarTodasLasEditoriales() {
        return editorialRepository.findAll();
    }

    @Override
    public Editorial actualizarCategoria(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    @Override
    public void eliminarCategoria(Long id) {
        editorialRepository.deleteById(id);
    }
}
