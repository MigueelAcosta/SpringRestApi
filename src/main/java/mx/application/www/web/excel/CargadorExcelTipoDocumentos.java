package mx.application.www.web.excel;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import mx.application.www.datos.entidades.CatClasificacionMercancia;
import mx.application.www.datos.entidades.CatEstatus;
import mx.application.www.datos.entidades.CatTipoDocumento;
import mx.application.www.datos.repositorios.CatClasificacionMercanciaRepository;
import mx.application.www.datos.repositorios.CatEstatusRepository;
import mx.application.www.datos.repositorios.CatTipoDocumentoRepository;
import mx.application.www.servicios.CatalogoService;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CargadorExcelTipoDocumentos {

    private final static Logger LOGGER = LoggerFactory.getLogger(CargadorExcelTipoDocumentos.class);

    private final static String NOMBRE_LABEL = "Nombre del documento".toUpperCase();
    private final static String CLASIFICACION_MERCANCIA = "Clasificacion".toUpperCase();
    private final static String ESTATUS_LABEL = "Estatus".toUpperCase();
    private final static int MAX_NOMBRE = 100;

    @Autowired
    CatEstatusRepository catEstatusRepo;

    @Autowired
    CatClasificacionMercanciaRepository catClasificacionMercanciaRepo;

    @Autowired
    CatTipoDocumentoRepository catTipoDocumentoRepo;

    @Autowired
    CatalogoService catalogoService;

    private Map<Integer, CatEstatus> mapEstatus;

    @PostConstruct
    void getEstatus() {
        mapEstatus = new HashMap<>();
        for (CatEstatus ce : catEstatusRepo.findAll()) {
            mapEstatus.put(ce.getCatEstatusId(), ce);
        }
    }

    private Map<Integer, CatClasificacionMercancia> mapClasificacion;

    /*@PostConstruct
    void getClasificacion() {
        mapClasificacion = new HashMap<>();
        for (CatClasificacionMercancia cc : catClasificacionMercanciaRepo.findAll()) {
            mapClasificacion.put(cc.getClasificacionMercancia());
        }
    }*/

    public String cargarTipoDocumentos(Path archivoExcel, boolean reemplazarDuplicados) {
        LOGGER.debug("cargarTipoDocumentos {}", archivoExcel.toString());
        try {
            Workbook workbook = WorkbookFactory.create(archivoExcel.toFile());
            LOGGER.debug("Found {} sheets", workbook.getNumberOfSheets());

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> columnas = new HashMap<>();
            List<CatTipoDocumento> docsAGuardar = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    for (Cell cell : row) {
                        columnas.put(cell.getStringCellValue().toUpperCase(), cell.getColumnIndex());
                        LOGGER.debug("{} => []", cell.getColumnIndex(), cell.getStringCellValue());
                    }
                    if (!columnas.containsKey(NOMBRE_LABEL) ||
                            !columnas.containsKey(CLASIFICACION_MERCANCIA) ||
                            !columnas.containsKey(ESTATUS_LABEL)) {
                        throw new IllegalAccessException("Encabezados incorrectos");
                    }
                } else {
                    String nombre = "";
                    if (row.getCell(columnas.get(NOMBRE_LABEL)) != null) {
                        nombre = row.getCell(columnas.get(NOMBRE_LABEL)).getStringCellValue();
                    }
                    String clasificacion = "";
                    if (row.getCell(columnas.get(CLASIFICACION_MERCANCIA)) != null) {
                        nombre = row.getCell(columnas.get(CLASIFICACION_MERCANCIA)).getStringCellValue();
                    }

                    int estatus = -1;
                    if (row.getCell(columnas.get(ESTATUS_LABEL)) != null) {
                        nombre = row.getCell(columnas.get(ESTATUS_LABEL)).getStringCellValue();
                    }

                    CatTipoDocumento tipoDocumento = new CatTipoDocumento();
                    List<CatTipoDocumento> docsPorNombre = catTipoDocumentoRepo.findByNombreIgnoreCase(nombre);
                    if (docsPorNombre != null && !docsPorNombre.isEmpty()) {
                        if (!reemplazarDuplicados) {
                            throw new IllegalArgumentException("Se encontraron registros duplicados");
                        } else {
                            tipoDocumento.setTipoDocumentoId(docsPorNombre.get(0).getTipoDocumentoId());
                        }
                    }

                    if (nombre == null || nombre.trim().length() == 0) {
                        sb.append(row.getRowNum() + ": Sin valor de Nombre");
                    } else if (nombre.trim().length() > MAX_NOMBRE) {
                        sb.append(row.getRowNum() + ": El nombre excede los " + MAX_NOMBRE + "caracteres");
                    } else if (clasificacion == null || clasificacion.trim().length() == 0) {
                        sb.append(row.getRowNum() + ": La clasificación está vacia");
                    } else if (estatus != 0 && estatus != 1) {
                        sb.append(row.getRowNum() + ": El Estatus debe ser 0 o 1");
                    } else {
                        tipoDocumento.setNombre(nombre);
                        //tipoDocumento.setClasificacion(mapClasificacion.get(clasificacion));
                        tipoDocumento.setEstatus(mapEstatus.get(estatus));
                        docsAGuardar.add(tipoDocumento);
                    }
                    catalogoService.saveDocumentos(docsAGuardar);
                }
            }return sb.toString();
        }catch (InvalidFormatException ifex){
            throw new IllegalArgumentException("Formato incorrecto");
        }catch (IOException e){
            throw new IllegalArgumentException("Error inesperado: " + e.getMessage());
        } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
