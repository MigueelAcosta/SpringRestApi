package aplicacion.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cat_estatus")
@Getter @Setter
public class CatEstatus implements Serializable{

    public enum ESTATUS { ACTIVO, INACTIVO, BORRADO };

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estatus")
    private Integer catEstatusId;
    @Column(name="estatus")
    private String estatusNombre;
    @Column(name="fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date estatusFechaAlta;
    @Column(name="usuario_alta")
    private String estatusUsuarioAlta;

}