package com.trebor.minibiblioteca.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.trebor.minibiblioteca.entities.Autor;
import com.trebor.minibiblioteca.entities.Libro;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.util.List;

public class LibroExporterPDF {

    private List<Libro> listalibros;

    public LibroExporterPDF(List<Libro> listalibros) {
        this.listalibros = listalibros;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TÍTULO", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("AUTORES", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Libro libro : listalibros) {
            table.addCell(String.valueOf(libro.getId()));
            table.addCell(libro.getTitulo());

            // Obtener nombres de los autores
            StringBuilder autores = new StringBuilder();
            for (Autor autor : libro.getAutores()) {
                if (autores.length() > 0) {
                    autores.append(", ");
                }
                autores.append(autor.getNombre());
            }
            table.addCell(autores.toString());
        }
    }

    public void export(HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Lista de Libros", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        PdfPTable table = new PdfPTable(3); // 3 columnas: ID, Título, Autores
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 4.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        document.close();
    }


}
