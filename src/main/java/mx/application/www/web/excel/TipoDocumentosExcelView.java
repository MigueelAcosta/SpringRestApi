package mx.application.www.web.excel;

import mx.application.www.datos.entidades.CatTipoDocumento;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class TipoDocumentosExcelView extends AbstractXlsView {


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<CatTipoDocumento> listaDocs = (List<CatTipoDocumento>) model.get("documentos");
        String filename = (String) model.get("filename");

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        Sheet sheet = workbook.createSheet("Servicios");
        sheet.setDefaultColumnWidth(30);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        style.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nombre");

        header.createCell(2).setCellValue("Estatus");
        header.getCell(2).setCellStyle(style);

        sheet.setColumnWidth(0, 2000);

        int rowCount = 1;

        for(CatTipoDocumento doc: listaDocs){
            Row engineRow = sheet.createRow(rowCount++);
            engineRow.createCell(1).setCellValue(doc.getNombre());
            engineRow.createCell(2).setCellValue(doc.getEstatus().getEstatusNombre());
        }
    }
}
