package com.trebor.minibiblioteca.reports;
import com.trebor.minibiblioteca.entities.Autor;
import com.trebor.minibiblioteca.entities.Categoria;
import com.trebor.minibiblioteca.entities.Editorial;
import com.trebor.minibiblioteca.entities.Libro;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class EditorialExporterExcel {
    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private List<Editorial>editoriales;

    public EditorialExporterExcel(List<Editorial> editoriales) {
        this.editoriales = editoriales;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("Editoriales");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "NOMBRE", style);
        createCell(row, 2, "LIBROS", style);

    }


    private void createCell(Row row,int columnCount,Object value,CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        else if(value instanceof Boolean){
            cell.setCellValue((Boolean)value);
        }
        else{
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setFontHeight(14);
        style.setFont(font);

        for (Editorial editorial : editoriales) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, editorial.getId(), style);
            createCell(row, columnCount++, editorial.getNombre(), style);

            String librosStr = editorial.getLibros().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.joining(", "));
            createCell(row, columnCount++, librosStr, style);
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


}
