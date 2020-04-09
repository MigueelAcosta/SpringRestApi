package mx.application.www.datos.repositorios;

import mx.application.www.datos.entidades.CatEstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatEstatusRepository extends JpaRepository<CatEstatus, Integer> {
    public CatEstatus findByEstatusNombre(String estatus);
}
