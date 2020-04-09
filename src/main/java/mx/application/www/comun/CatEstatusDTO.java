package mx.application.www.comun;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CatEstatusDTO {
    private Integer catEstatusId;
    private String estatusNombre;
    private Date estatusFechaAlta;
    private String estatusUsuarioAlta;
}
