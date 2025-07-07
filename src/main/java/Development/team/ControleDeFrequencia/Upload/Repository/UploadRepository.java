package Development.team.ControleDeFrequencia.Upload.Repository;

import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<UploadEntity,Long> {
}
