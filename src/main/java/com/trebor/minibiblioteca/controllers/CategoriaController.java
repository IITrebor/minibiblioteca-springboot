package com.trebor.minibiblioteca.controllers;

import com.trebor.minibiblioteca.entities.Categoria;
import com.trebor.minibiblioteca.entities.Libro;
import com.trebor.minibiblioteca.reports.AutorExporterPDF;
import com.trebor.minibiblioteca.reports.CategoriaExporterPDF;
import com.trebor.minibiblioteca.repositories.CategoriaRepository;
import com.trebor.minibiblioteca.services.CategoriaService;
import com.trebor.minibiblioteca.services.LibroService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private LibroService libroService;

    @GetMapping({"/listar", "/"})
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.listarTodasLasCategorias();
        model.addAttribute("categorias", categorias);
        return "categoria/listar_categoria";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaCategoria(Model model) {
        Categoria categoria = new Categoria();
        model.addAttribute("categoria", categoria);
        return "categoria/formulario_categoria";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        Categoria categoriaGuardada = categoriaService.guardarCategoria(categoria);
        List<Libro> libros = libroService.buscarPorCategoria(categoriaGuardada);
        categoriaGuardada.setLibros(libros);
        categoriaService.guardarCategoria(categoria);
        return "redirect:/categorias/listar";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        categoria.ifPresent(value -> model.addAttribute("categoria", value));
        return "categoria/formulario_categoria";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarCategoria(@PathVariable Long id, @ModelAttribute Categoria categoria) {
        Categoria categoriaActual = categoriaService.buscarPorId(id).orElse(null);
        if (categoriaActual != null) {
            categoria.setLibros(categoriaActual.getLibros());
            categoriaService.actualizarCategoria(categoria);
        }
        return "redirect:/categorias/listar";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return "redirect:/categorias/listar";
    }

    @GetMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categorias" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Categoria> categorias = categoriaRepository.findAll();

        CategoriaExporterPDF exporterPDF = new CategoriaExporterPDF(categorias);
        exporterPDF.export(response);

    }
}