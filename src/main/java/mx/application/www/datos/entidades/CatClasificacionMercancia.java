package mx.application.www.datos.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import mx.application.www.datos.entidades.CatEstatus;

@Entity
@Table(name="cat_clasificacion_mercancia")
@Setter @Getter
public class CatClasificacionMercancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_clasificacion_mercancia_generator")
    @SequenceGenerator(name = "id_clasificacion_mercancia_generator", sequenceName = "id_clasificacion_mercancia_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name="id_clasificacion_mercancia")
    private Integer clasificacionMercanciaId;
    @Column(name="clasificacion_mercancia")
    private String clasificacionMercancia;
    @Column(name="fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    //@JoinColumn(name="usuario_alta", referencedColumnName = "usuario_alta")
    //@ManyToOne
    private Integer usuarioAlta;
    @Column(name="fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    //@JoinColumn(name="usuario_modificacion", referencedColumnName = "usuario_modificacion")
    //@ManyToOne
    private Integer usuarioModificacion;
    @JoinColumn(name = "id_estatus", referencedColumnName = "id_estatus")
    @ManyToOne
    private CatEstatus estatus;
}
