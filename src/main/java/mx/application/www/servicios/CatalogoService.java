package mx.application.www.servicios;


import mx.application.www.comun.CatClasificacionMercanciaDTO;
import mx.application.www.comun.CatEstatusDTO;
import mx.application.www.comun.CatTipoDocumentoDTO;
import mx.application.www.datos.entidades.CatTipoDocumento;

import java.util.List;

public interface CatalogoService {
    public List<CatEstatusDTO> getCatEstatus();

    public List<CatTipoDocumentoDTO> getTipoDocumentos();
    public List<CatTipoDocumentoDTO> getTipoDocumentoByExample(CatTipoDocumentoDTO tipoDocumentoEjemplo);
    public List<CatTipoDocumentoDTO> getCatTipoDocuentos(String nombre, String estado, Integer tipoMercancia);

    public CatTipoDocumentoDTO saveTipoDocumento(CatTipoDocumentoDTO catTipoDocumentoDTO);
    public CatClasificacionMercanciaDTO saveClasificacionMercancia(CatClasificacionMercanciaDTO catClasificacionMercancia);

    public CatTipoDocumentoDTO updateTipoDocumento(Integer documentoId, CatTipoDocumentoDTO catTipoDocumentoDTO);
    public CatClasificacionMercanciaDTO updateClasificacionMercancia(Integer clasificacionId, CatClasificacionMercanciaDTO catClasificacionMercanciaDTO);

    public CatTipoDocumentoDTO getDocumentoById(Integer documentoId);
    public List<CatTipoDocumento> getDocumentosEntidades(List<Integer> ids);
    public void saveDocumentos(List<CatTipoDocumento> catDocumentos);

}
