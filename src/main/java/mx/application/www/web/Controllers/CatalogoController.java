package mx.application.www.web.Controllers;


import mx.application.www.comun.CatEstatusDTO;
import mx.application.www.comun.CatTipoDocumentoDTO;
import mx.application.www.datos.entidades.CatTipoDocumento;
import mx.application.www.servicios.CatalogoService;
import mx.application.www.web.excel.CargadorExcelTipoDocumentos;
import mx.application.www.web.excel.TipoDocumentosExcelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CatalogoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoController.class);

    @Autowired
    CargadorExcelTipoDocumentos cargadorDocumentos;

    @Autowired
    StorageService almacenador;

    @Autowired
    CatalogoService catalogoService;

    @GetMapping("/estatus")
    public List<CatEstatusDTO> getEstatus() {
        return catalogoService.getCatEstatus();
    }

    @GetMapping("tipo_documentos")
    public List<CatTipoDocumentoDTO> getTipoDocumentos(){
        LOGGER.debug("GET ALL");
        return catalogoService.getTipoDocumentos();
    }

    @GetMapping("tipo_documentos/{id}")
    public ResponseEntity<CatTipoDocumentoDTO> getTiposDocumentosById(@PathVariable("id") Integer tipoDocumentoId){
        LOGGER.debug("GET {}", tipoDocumentoId);
        CatTipoDocumentoDTO catTipoDocumentoDTO = catalogoService.getDocumentoById(tipoDocumentoId);
        if(catTipoDocumentoDTO == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(catTipoDocumentoDTO);
        }
    }

    @GetMapping(value = "tipo_documentos", params = {"tipoDocumento", "estatus", "clasificacion"})
    public List<CatTipoDocumentoDTO> buscarTipoDocumentos(
            @RequestParam(value = "tipoDocumento", required = false) String tipoDocumento,
            @RequestParam(value = "estatus", required = false) String estatus,
            @RequestParam(value = "clasificacion", required = false) Integer clasificacion){
        LOGGER.debug("GET por nombre, estatus y clasificacion de mercancia");
        CatTipoDocumentoDTO tipoDocumentos = new CatTipoDocumentoDTO();
        if(tipoDocumento != null){
            tipoDocumentos.setNombre(tipoDocumento.trim());
        }
        if (estatus != null) {
            tipoDocumentos.setEstatus(estatus);
        }
        if(clasificacion != null){
            //tipoDocumentos.setCatClasificacionMercancia(clasificacion);
        }

        return catalogoService.getTipoDocumentoByExample(tipoDocumentos);
    }

    @PostMapping("tipo_documentos_soporte")
    public ResponseEntity<CatTipoDocumentoDTO> guardaTipoDocumentoSoporte(@Valid @RequestBody CatTipoDocumentoDTO catTipoDocumento, Errors errores){
        LOGGER.debug("POST {}", catTipoDocumento);
        if(errores.hasErrors()){
            String mensajeErrores = errores.getFieldErrors().parallelStream()
                    .map(e-> e.getField() + ":" + e.getDefaultMessage()).collect(Collectors.joining(","));
            return ResponseEntity.badRequest().header("ERROR", mensajeErrores).build();
        }
        try{
            CatTipoDocumentoDTO tipoDocumentoSaved = catalogoService.saveTipoDocumento(catTipoDocumento);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(tipoDocumentoSaved.getTipoDocumentacionId()).toUri();
            return ResponseEntity.created(uri).body(tipoDocumentoSaved);
        }catch (DataIntegrityViolationException dive){
            LOGGER.warn("Error al guardar tipo de documento de soporte", dive);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("ERROR", dive.getMessage())
                    .build();
        }catch (Exception e){
            LOGGER.warn("Error al guardar Tipo de documento de soporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("ERROR", e.getMessage()).build();
        }
    }

    @PutMapping("tipo_documentos_soporte/{tipoDocumentoSoporteId}")
    public ResponseEntity<CatTipoDocumentoDTO> UpdateTipoDocumentoSoporte (@Valid @PathVariable Integer tipoDocumentoSoporteId,
                                                                           @RequestBody CatTipoDocumentoDTO catTipoDocumentoDTO, Errors errores){
        try{
            CatTipoDocumentoDTO DocSoporteSaved = catalogoService.updateTipoDocumento(tipoDocumentoSoporteId, catTipoDocumentoDTO );
            return ResponseEntity.ok(DocSoporteSaved);
        }catch (DataIntegrityViolationException dive){
            LOGGER.warn("Error al actualizar el tipo documento de soporte", dive);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error", dive.getMessage())
                    .build();
        }catch(Exception e){
            LOGGER.warn("Error al actualizar el tipo documento de soporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Error", e.getMessage()).build();
        }
    }

    @GetMapping(value = "/tipos_documentos_excel", params = {"format"})
    ModelAndView getTiposDocumentosExcel(@RequestParam(value = "ids", required = false) List<Integer> ids){
        List<CatTipoDocumento> documentos = catalogoService.getDocumentosEntidades(ids);
        ModelAndView mav = new ModelAndView(new TipoDocumentosExcelView(),"DocumentosSoporte", documentos);
        mav.addObject("filename","CatalogoDocumentosSoporte.xls");
        return mav;
    }

    @PostMapping("tipo_documentos/upload")
    public ResponseEntity uploadTipoDocumentos(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "replace", required = false, defaultValue = "false") Boolean reemplazar){
        LOGGER.debug("POST upload");
        try {
            Path destino = almacenador.store(file);
            String resultado = cargadorDocumentos.cargarTipoDocumentos(destino,reemplazar);
            LOGGER.debug("LOG {}", resultado);
            return ResponseEntity.ok()
                    .header("LOG", resultado)
                    .build();
        }
        catch(IllegalArgumentException iaex) {
            LOGGER.warn("Error en archivo", iaex);
            return ResponseEntity.badRequest()
                    .header("ERROR", iaex.getMessage())
                    .build();
        }
        catch(Exception ex) {
            LOGGER.warn("Error al cargar servicios", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("ERROR", ex.getMessage())
                    .build();
        }
    }
}
