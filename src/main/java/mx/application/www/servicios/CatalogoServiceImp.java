package mx.application.www.servicios;


import mx.application.www.comun.CatClasificacionMercanciaDTO;
import mx.application.www.comun.CatEstatusDTO;
import mx.application.www.comun.CatTipoDocumentoDTO;
import mx.application.www.datos.entidades.CatClasificacionMercancia;
import mx.application.www.datos.entidades.CatEstatus;
import mx.application.www.datos.entidades.CatTipoDocumento;
import mx.application.www.datos.repositorios.CatClasificacionMercanciaRepository;
import mx.application.www.datos.repositorios.CatEstatusRepository;
import mx.application.www.datos.repositorios.CatTipoDocumentoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogoServiceImp implements CatalogoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoServiceImp.class);

    @Autowired
    CatEstatusRepository catEstatusRepo;

    @Autowired
    CatTipoDocumentoRepository catTipoDocumentoRepository;

    @Autowired
    CatClasificacionMercanciaRepository catClasificacionMercanciaRepo;

    @Autowired
    ModelMapper modelMapper;

    private EnumMap<CatEstatus.ESTATUS, CatEstatus> mapEstatus;

    @Override
    public List<CatEstatusDTO> getCatEstatus() {
        return modelMapper.map(catEstatusRepo.findAll(), new TypeToken<List<CatEstatusDTO>>() {
        }.getType());
    }


    @Override
    public List<CatTipoDocumentoDTO> getTipoDocumentos() {
        return modelMapper.map(catTipoDocumentoRepository.findAll(), new TypeToken<List<CatTipoDocumento>>(){}.getType());
    }

    @Override
    public List<CatTipoDocumentoDTO> getCatTipoDocuentos(String nombre, String estado, Integer tipoMercancia) {
        CatEstatus est = null;
        if(estado != null){
            est = mapEstatus.get(CatEstatus.ESTATUS.valueOf(estado));
        }

        return modelMapper.map(catTipoDocumentoRepository.findByNombreAndCatClasificacionMercanciaAndEstatus(nombre, tipoMercancia, est),
                new TypeToken<List<CatTipoDocumentoDTO>>(){}.getType());
    }

    @Override
    public List<CatTipoDocumentoDTO> getTipoDocumentoByExample(CatTipoDocumentoDTO tipoDocumentoEjemplo) {
        LOGGER.debug("getTipoDocumentoByExample {}", tipoDocumentoEjemplo);
        CatTipoDocumento tipo = modelMapper.map(tipoDocumentoEjemplo, CatTipoDocumento.class);
        List<CatTipoDocumento> tipos = null;

        if(tipoDocumentoEjemplo.getEstatus() == null || tipoDocumentoEjemplo.getEstatus().trim().length() == 0){

        }
        return modelMapper.map(tipos, new TypeToken<List<CatTipoDocumento>>(){}.getType());
    }

    @Override
    public CatTipoDocumentoDTO saveTipoDocumento(CatTipoDocumentoDTO catTipoDocumentoDTO) {
        if(!catTipoDocumentoRepository.findByNombre(catTipoDocumentoDTO.getNombre()).isEmpty()){
            throw new DataIntegrityViolationException("Documentación de soporte con nombre existente");
        }

        CatTipoDocumento tipoDocumento = modelMapper.map(catTipoDocumentoDTO, CatTipoDocumento.class);
        tipoDocumento.setEstatus(catEstatusRepo.findByEstatusNombre(catTipoDocumentoDTO.getEstatus()));
        //tipoDocumento.setEstatus(mapEstatus.get(CatEstatus.ESTATUS.valueOf(catTipoDocumentoDTO.getEstatus())));
        tipoDocumento.setFechaAlta(new Date());
        tipoDocumento.setUsuarioAlta(0);
        //tipoDocumento.setCatClasificacionMercancia(catTipoDocumentoDTO.getClasificacionMercancia());

        return modelMapper.map(catTipoDocumentoRepository.save(tipoDocumento), CatTipoDocumentoDTO.class);
    }

    @Override
    public CatTipoDocumentoDTO updateTipoDocumento(Integer documentoId, CatTipoDocumentoDTO catTipoDocumentoDTO) {
        if(catTipoDocumentoRepository.existsByTipoDocumentoIdAndNombre(documentoId, catTipoDocumentoDTO.getNombre())){
            throw new DataIntegrityViolationException("Documento de soporte con nombre existente");
        }

        Optional<CatTipoDocumento> optTipoDoc = catTipoDocumentoRepository.findByTipoDocumentoId(documentoId);
        if(optTipoDoc.isPresent()){
            CatTipoDocumento tipoDocumento = modelMapper.map(catTipoDocumentoDTO, CatTipoDocumento.class);
            CatTipoDocumento tipoDoc = optTipoDoc.get();
            tipoDocumento.setTipoDocumentoId(documentoId);
            tipoDocumento.setFechaAlta(tipoDoc.getFechaAlta());
            tipoDocumento.setUsuarioAlta(tipoDoc.getUsuarioAlta());
            tipoDocumento.setFechaModificacion (new Date());
            tipoDocumento.setUsuarioModificacion(0);
            if(tipoDocumento.getEstatus() != null){
                tipoDocumento.setEstatus(mapEstatus.get(CatEstatus.ESTATUS.valueOf(catTipoDocumentoDTO.getEstatus())));
            }else {
                tipoDocumento.setEstatus(tipoDoc.getEstatus());
            }
            return modelMapper.map(catTipoDocumentoRepository.save(tipoDocumento), CatTipoDocumentoDTO.class);
        }else{
            throw new DataIntegrityViolationException("No se encontro Documento de soporte con el id " + documentoId);
        }
    }

    @Override
    public CatClasificacionMercanciaDTO saveClasificacionMercancia(CatClasificacionMercanciaDTO catClasificacionMercancia) {
        if(!catClasificacionMercanciaRepo.findByClasificacionMercancia(catClasificacionMercancia.getNombre()).isEmpty()){
            throw new DataIntegrityViolationException("Clasificación de mercancía con nombre existente");
        }

        CatClasificacionMercancia clasificacionMercancia = modelMapper.map(catClasificacionMercancia, CatClasificacionMercancia.class);
        clasificacionMercancia.setEstatus(mapEstatus.get(CatEstatus.ESTATUS.ACTIVO));
        clasificacionMercancia.setFechaAlta(new Date());
        clasificacionMercancia.setUsuarioAlta(0);
        clasificacionMercancia = catClasificacionMercanciaRepo.save(clasificacionMercancia);
        return modelMapper.map(clasificacionMercancia, CatClasificacionMercanciaDTO.class);
    }

    @Override
    public CatClasificacionMercanciaDTO updateClasificacionMercancia(Integer clasificacionId, CatClasificacionMercanciaDTO catClasificacionMercanciaDTO) {
        /*List<CatClasificacionMercancia> clasificacionPorNombre = catClasificacionMercanciaRepo.findByClasificacionMercancia(catClasificacionMercanciaDTO.getNombre());
        if(clasificacionPorNombre.size() > 1 || (clasificacionPorNombre.size() == 1 && clasificacionPorNombre.get(0)
                .getClasificacionMercanciaId() != catClasificacionMercanciaDTO.getClasificacionMercanciaId())){
            throw new DataIntegrityViolationException("Clasificación de mercancia con nombre existente");
        }

        Optional<CatClasificacionMercancia> optClas = catClasificacionMercanciaRepo.findById(clasificacionId);
        if(optClas.isPresent()){
            CatClasificacionMercancia clasMerca = optClas.get();
            clasMerca.setNombre(catClasificacionMercanciaDTO.getNombre());
            if(catClasificacionMercanciaDTO.getEstatus() != null){
                clasMerca.setEstatus(mapEstatus.get(CatEstatus.ESTATUS.valueOf(catClasificacionMercanciaDTO.getEstatus())));
            }
            clasMerca.setFechaModificacion(new Date());
            clasMerca.setUsuarioModificacion(0);
            clasMerca = catClasificacionMercanciaRepo.save(clasMerca);
            return modelMapper.map(clasMerca,CatClasificacionMercanciaDTO.class);
        }else{
            throw new DataIntegrityViolationException("No se encontró Clasificación de mercancia con id " +clasificacionId);
        }*/
        return null;
    }

    @Override
    public CatTipoDocumentoDTO getDocumentoById(Integer documentoId) {
        Optional<CatTipoDocumento> optDoc = catTipoDocumentoRepository.findById(documentoId);
        if(optDoc.isPresent()){
            return modelMapper.map(optDoc.get(), CatTipoDocumentoDTO.class);
        }
        return null;
    }

    @Override
    public List<CatTipoDocumento> getDocumentosEntidades(List<Integer> ids) {
        List<CatTipoDocumento> tipoDocumentos;
        if(ids == null || ids.isEmpty()){
            Sort orden = Sort.by("fechaAlta");
            tipoDocumentos = catTipoDocumentoRepository.findAll(orden);
        }else{
            tipoDocumentos = catTipoDocumentoRepository.findAllById(ids).stream()
                    .filter(cs -> cs.getEstatus().getEstatusNombre() != "BORRADO")
                    .collect(Collectors.toList());
            tipoDocumentos.sort((CatTipoDocumento d1, CatTipoDocumento d2) -> d1.getFechaAlta().compareTo(d2.getFechaAlta()));
        }
        return tipoDocumentos;
    }

    @Override
    public void saveDocumentos(List<CatTipoDocumento> catDocumentos) {
        LOGGER.debug("Guardando {} TiposDocumentos", catDocumentos.size());
        //Long nuevoConsecutivo = catTipoDocumentoRepository.getMaximoConsecutivo();
        for(CatTipoDocumento ctd: catDocumentos){
            ctd.setFechaAlta(new Date());
            ctd.setUsuarioAlta(0);
            ctd = catTipoDocumentoRepository.save(ctd);
        }
    }


}
