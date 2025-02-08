package com.trebor.minibiblioteca.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.trebor.minibiblioteca.entities.Categoria;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.util.List;

public class CategoriaExporterPDF {

    public List<Categoria> listaCategorias;

    public CategoriaExporterPDF(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
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
    }

    private void writeTableData(PdfPTable table) {
        for (Categoria categorias : listaCategorias) {
            table.addCell(String.valueOf(categorias.getId()));
            table.addCell(String.valueOf(categorias.getNombre()));
        }
    }

    public void export(HttpServletResponse response) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Lista de Categorias", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        PdfPTable table = new PdfPTable(2); // 2 columnas: ID, Nombre,
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1f, 3.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }

}