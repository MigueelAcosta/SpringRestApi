package mx.application.www.datos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.application.www.datos.entidades.CatClasificacionMercancia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatClasificacionMercanciaRepository extends JpaRepository<CatClasificacionMercancia, Integer>{
    public List<CatClasificacionMercancia> findByClasificacionMercancia(String clasificacion);

    //public abstract boolean existsByClasificacionMercanciaIAndClasificacionMercancia(Integer clasificacionId, String clasificacion);
}
