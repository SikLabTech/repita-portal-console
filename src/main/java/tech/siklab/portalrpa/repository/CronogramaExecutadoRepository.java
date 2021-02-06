package tech.siklab.portalrpa.repository;

import tech.siklab.portalrpa.model.CronogramaExecutado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronogramaExecutadoRepository extends JpaRepository<CronogramaExecutado, String> {

    List<CronogramaExecutado> findAll();
    CronogramaExecutado findById(Long id);
}
