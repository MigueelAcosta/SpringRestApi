package mx.application.www.datos.repositorios;

import mx.application.www.datos.entidades.CatEstatus;
import mx.application.www.datos.entidades.CatTipoDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CatTipoDocumentoRepository extends JpaRepository<CatTipoDocumento, Integer>{
    public List<CatTipoDocumento> findByNombre(String nombre);
    public List<CatTipoDocumento> findByNombreIgnoreCase(String nombre);
    public Optional<CatTipoDocumento> findByTipoDocumentoId(Integer tipoDocumentoId);
    public abstract boolean existsByTipoDocumentoIdAndNombre(Integer tipoDocumentoId, String nombre);
    public abstract boolean existsByTipoDocumentoIdNotAndNombre(Integer tipoDocumentoId, String nombre);
    public abstract List<CatTipoDocumento> findByNombreAndCatClasificacionMercanciaAndEstatus(String nombre, Integer clasificacion, CatEstatus estatus);
    public abstract List<CatTipoDocumento> findByTipoDocumentoIdInOrderByFechaAlta(List<Integer> ids);


}
