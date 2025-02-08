package com.trebor.minibiblioteca.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.trebor.minibiblioteca.entities.Autor;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.util.List;

public class AutorExporterPDF {

    private List<Autor> listaAutores;

    public AutorExporterPDF(List<Autor> listaAutores) {
        this.listaAutores = listaAutores;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("NOMBRE DEL AUTOR",font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for(Autor autor:listaAutores){
            table.addCell(String.valueOf(autor.getId()));
            table.addCell(autor.getNombre());
        }
    }

    public void export(HttpServletResponse response)throws Exception{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph p = new Paragraph("Lista de Autores",font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f,3.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();

    }



}
