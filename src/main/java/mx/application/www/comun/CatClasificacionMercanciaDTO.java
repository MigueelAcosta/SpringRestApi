package mx.application.www.comun;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class CatClasificacionMercanciaDTO {
    private Integer clasificacionMercanciaId;
    private String nombre;
    private Date fechaAlta;
    private Integer usuarioAlta;
    private Date fechaModificacion;
    private Integer usuarioModificacion;
    private String estatus;
}
