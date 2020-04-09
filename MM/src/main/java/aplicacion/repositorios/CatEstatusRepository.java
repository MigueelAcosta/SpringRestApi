package aplicacion.repositorios;

import aplicacion.entidades.CatEstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatEstatusRepository extends JpaRepository<CatEstatus, Integer> {
}
