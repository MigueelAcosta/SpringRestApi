package mx.application.www.comun;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class CatTipoDocumentoDTO {
    private Integer tipoDocumentacionId;
    private String clave;
    private String nombre;
    private Date fechaAlta;
    private Integer usuarioAlta;
    private Date fechaModificacion;
    private Integer usuarioModificacion;
    private String Estatus;
    private CatClasificacionMercanciaDTO clasificacionMercancia;
}
