package com.trebor.minibiblioteca.controllers;

import com.trebor.minibiblioteca.entities.Editorial;
import com.trebor.minibiblioteca.services.EditorialService;
import com.trebor.minibiblioteca.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/editoriales")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private LibroService libroService;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaEditorial(Model model){
        model.addAttribute("editorial",new Editorial());
        return "editorial/formulario_editorial";
    }

    @PostMapping("/guardar")
    public String guardarEditorial(@ModelAttribute Editorial editorial){
        Editorial editorialGuardada =editorialService.guardarEditorial(editorial);
        return "redirect:/editoriales/listar";
    }

    @GetMapping({"/listar","/"})
    public String listarEditoriales(Model model){
        List<Editorial> editoriales = editorialService.listarTodasLasEditoriales();
        model.addAttribute("editoriales",editoriales);
        return "editorial/listar_editoriales";
    }

    @GetMapping("/{id}")
    public String mostrarEditorial(@PathVariable Long id , Model model){
        Optional<Editorial> editorialOptional= editorialService.buscarPorId(id);
        if (editorialOptional.isPresent()){
            Editorial editorial=editorialOptional.get();
            model.addAttribute("editorial",editorial);
            model.addAttribute("libros",editorial.getLibros());
        }
        return "editorial/mostrar_editoriales";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarEditorial(@PathVariable Long id,Model model){
        Optional<Editorial> editorial = editorialService.buscarPorId(id);
        editorial.ifPresent(value -> model.addAttribute("editorial",value));
        return "editorial/formulario_editorial";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarEditorial(@PathVariable Long id, @ModelAttribute Editorial editorial){
        Optional<Editorial> editorialOptional =editorialService.buscarPorId(id);
        if(editorialOptional.isPresent()){
            Editorial editorialActual = editorialOptional.get();
            editorialActual.setNombre(editorial.getNombre());
            editorialService.actualizarCategoria(editorialActual);
        }
        return "redirect:/editoriales/listar";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarEditorial(@PathVariable Long id){
        editorialService.eliminarCategoria(id);
        return "redirect:/editoriales/listar";
    }

    @GetMapping("/{id}/libros")
    public String mostrarLibrosDeEditorial(@PathVariable Long id,Model model){
        Optional<Editorial> editorialOptional = editorialService.buscarPorId(id);
        if (editorialOptional.isPresent()){
            Editorial editorial = editorialOptional.get();
            model.addAttribute("editorial",editorial);
            model.addAttribute("libros",editorial.getLibros());
        }
        return "editorial/mostrar_libros_editorial";
    }



}
