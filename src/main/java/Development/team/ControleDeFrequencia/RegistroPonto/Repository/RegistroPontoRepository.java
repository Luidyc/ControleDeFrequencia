package Development.team.ControleDeFrequencia.RegistroPonto.Repository;
import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto,Long> {
    @Query(value = """
SELECT *
FROM registro_ponto as r
WHERE date BETWEEN CURRENT_DATE - INTERVAL '9 days' AND CURRENT_DATE
ORDER BY date DESC, cpf;
""", nativeQuery = true)
    List<RegistroPonto> findFuncionariosCom6DiasConsecutivos();
}
