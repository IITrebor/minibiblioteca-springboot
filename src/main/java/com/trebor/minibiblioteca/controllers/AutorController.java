package com.trebor.minibiblioteca.controllers;


import com.trebor.minibiblioteca.entities.Autor;
import com.trebor.minibiblioteca.reports.AutorExporterExcel;
import com.trebor.minibiblioteca.reports.AutorExporterPDF;
import com.trebor.minibiblioteca.repositories.AutorRepository;
import com.trebor.minibiblioteca.services.AutorService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    @GetMapping({"/listar","/"})
    public String listarAutores(Model model){
        List<Autor> autores = autorService.listarTodosLosAutores();
        model.addAttribute("autores",autores);
        return  "autor/lista_autores";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAutor(Model model){
        model.addAttribute("autor",new Autor());
        return "autor/formulario_autor";
    }


    @PostMapping("/guardar")
    public String guardarAutor(@Valid  @ModelAttribute Autor autor, BindingResult result, Model model ) {

        if (result.hasErrors()){
            return "autor/formulario_autor";
        }
        autorService.guardarAutor(autor);
        return "redirect:/autores/listar";
    }


    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAutor(@PathVariable Long id, Model model){
        Optional<Autor> autor =autorService.buscarPorid(id);
        autor.ifPresent(value-> model.addAttribute("autor",value));
        return "autor/formulario_autor";
    }


    @PostMapping("/actualizar")
    public String actualizarAutor(@ModelAttribute Autor autor){
        autorService.actualizarAutor(autor);
        return "redirect:/autores/listar";
    }



    @GetMapping("eliminar/{id}")
    public String eliminarAutor(@PathVariable Long id) throws ClassNotFoundException {

        autorService.eliminarAutor(id);
        return "redirect:/autores/listar";

    }

    @GetMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response)throws Exception{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=autores" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        List<Autor> autores =autorRepository.findAll();

        AutorExporterPDF exporterPDF= new AutorExporterPDF(autores);
        exporterPDF.export(response);
    }

    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=autores" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        List<Autor> autores =autorRepository.findAll();

        AutorExporterExcel exporterExcel = new AutorExporterExcel(autores);
        exporterExcel.export(response);
    }
}
