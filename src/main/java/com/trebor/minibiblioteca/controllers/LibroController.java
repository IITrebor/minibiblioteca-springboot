package com.trebor.minibiblioteca.controllers;

import com.trebor.minibiblioteca.entities.Autor;
import com.trebor.minibiblioteca.entities.Categoria;
import com.trebor.minibiblioteca.entities.Editorial;
import com.trebor.minibiblioteca.entities.Libro;
import com.trebor.minibiblioteca.reports.AutorExporterExcel;
import com.trebor.minibiblioteca.reports.LibroExporterExcel;
import com.trebor.minibiblioteca.reports.LibroExporterPDF;
import com.trebor.minibiblioteca.repositories.LibroRepository;
import com.trebor.minibiblioteca.services.AutorService;
import com.trebor.minibiblioteca.services.CategoriaService;
import com.trebor.minibiblioteca.services.EditorialService;
import com.trebor.minibiblioteca.services.LibroService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AutorService autorService;

    @GetMapping({"/listar","/"})
    public String listarLibros(Model model){
        List<Libro> libros = libroService.listarTodosLosLibros();
        model.addAttribute("libros",libros);
        return "libro/listar_libros";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoLibro(Model model){
        Libro libro = new Libro();
        model.addAttribute("libro",libro);
        model.addAttribute("editoriales",editorialService.listarTodasLasEditoriales());
        model.addAttribute("autores",autorService.listarTodosLosAutores());
        model.addAttribute("categorias",categoriaService.listarTodasLasCategorias());
        return "libro/formulario_libro";
    }

    @PostMapping("/guardar")
    public String guardarLibro(@Valid @ModelAttribute Libro libro, BindingResult result, @RequestParam("editorialId")Long editorialId,
                               @RequestParam("categoriaId") Long categoriaId,
                               @RequestParam("autoresIds") List<Long> autoresIds,Model model){

        if (result.hasErrors()){
            model.addAttribute("editoriales", editorialService.listarTodasLasEditoriales());
            model.addAttribute("categorias", categoriaService.listarTodasLasCategorias());
            model.addAttribute("autores", autorService.listarTodosLosAutores());
            return "libro/formulario_libro";
        }

        //Obtener y asignar la editorial y la categoria al libro
        Optional<Editorial> editorial=editorialService.buscarPorId(editorialId);
        editorial.ifPresent(libro::setEditorial);

        Optional<Categoria>categoria=categoriaService.buscarPorId(categoriaId);
        categoria.ifPresent(libro::setCategoria);

        List<Autor>autores =autorService.buscarPorIds(autoresIds);
        libro.setAutores(new ArrayList<>(autores));

        libroService.saveLibro(libro);
        return "redirect:/libros/listar";

    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarLibro(@PathVariable Long id,Model model){

        Optional<Libro> libro = libroService.buscarPorId(id);

        if (libro.isPresent()){
            model.addAttribute("libro",libro.get());
            model.addAttribute("editoriales",editorialService.listarTodasLasEditoriales());
            model.addAttribute("autores",autorService.listarTodosLosAutores());
            model.addAttribute("categorias",categoriaService.listarTodasLasCategorias());
        }
        return "libro/formulario_libro";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarLibro(@PathVariable Long id,@ModelAttribute Libro libro,
                                  @RequestParam("editorialId")Long editorialId,
                                  @RequestParam("categoriaId") Long categoriaId,
                                  @RequestParam("autoresIds") List<Long> autoresIds){
        //Obtener y asignar la editorial y la categoria al libro
        Optional<Editorial>editorial=editorialService.buscarPorId(editorialId);
        editorial.ifPresent(libro::setEditorial);

        Optional<Categoria>categoria=categoriaService.buscarPorId(categoriaId);
        categoria.ifPresent(libro::setCategoria);

        List<Autor>autores =autorService.buscarPorIds(autoresIds);
        libro.setAutores(new ArrayList<>(autores));

        libro.setId(id);
        libroService.actualizarLibro(libro);
        return "redirect:/libros/listar";
    }

    @GetMapping("/{id}/autores")
    public String mostrarAutoresDelLibro(@PathVariable Long id,Model model){
        Optional<Libro> libroOptional = libroService.buscarPorId(id);
        if (libroOptional.isPresent()){
            Libro libro =libroOptional.get();
            model.addAttribute("libro",libro);
            model.addAttribute("autores",libro.getAutores());
        }
        return "libro/mostrar_autores_libro";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarLibro(@PathVariable Long id){
        libroService.eliminarLibro(id);
        return "redirect:/libros/listar";
    }


    @GetMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=libros_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Libro> libros = libroRepository.findAll();
        LibroExporterPDF exporterPDF = new LibroExporterPDF(libros);
        exporterPDF.export(response);
    }


    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=libros" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Libro> libros = libroRepository.findAll();

        LibroExporterExcel exporterExcel = new LibroExporterExcel(libros);
        exporterExcel.export(response);
    }


}
