package com.trebor.minibiblioteca.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.trebor.minibiblioteca.entities.Categoria;
import com.trebor.minibiblioteca.entities.Editorial;
import com.trebor.minibiblioteca.entities.Libro;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.util.List;

public class EditorialExpoterPDF {

    private List<Editorial> listaEditoriales;

    public EditorialExpoterPDF(List<Editorial> listaEditoriales) {
        this.listaEditoriales = listaEditoriales;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("NOMBRE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("LIBROS", font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        for (Editorial editorial : listaEditoriales) {
            table.addCell(String.valueOf(editorial.getId()));
            table.addCell(editorial.getNombre());

            // Obtener lista de libros
            StringBuilder librosDeCategorias = new StringBuilder();
            for (Libro libro : editorial.getLibros()) {
                if (librosDeCategorias.length() > 0) {
                    librosDeCategorias.append(", ");
                }
                librosDeCategorias.append(libro.getTitulo());
            }
            table.addCell(librosDeCategorias.toString());
        }

    }

    public void export(HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);


        Paragraph p = new Paragraph("Lista de Editoriales", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        PdfPTable table = new PdfPTable(3); // 3 columnas: ID, Nombre, Libros
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 4.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        document.close();
    }
}