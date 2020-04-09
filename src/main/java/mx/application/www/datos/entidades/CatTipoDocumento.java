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

@Entity
@Table(name="cat_tipo_documento")
@Setter @Getter
public class CatTipoDocumento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_tipo_documento_generator")
    @SequenceGenerator(name = "id_tipo_documento_generator", sequenceName = "id_tipo_documento_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name="id_tipo_documento")
    private Integer tipoDocumentoId;
    @Column(name="tipo_documento")
    private String nombre;
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

    @JoinColumn(name = "id_clasificacion_mercancia", referencedColumnName = "id_clasificacion_mercancia")
    @ManyToOne(optional = false)
    private CatClasificacionMercancia catClasificacionMercancia;
}
