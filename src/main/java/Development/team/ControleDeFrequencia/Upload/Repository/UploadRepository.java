package Development.team.ControleDeFrequencia.Upload.Repository;

import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UploadRepository extends JpaRepository<UploadEntity,Long> {
    @Query("SELECT u FROM UploadEntity u WHERE u.uploadDate >= :limitDate")
    List<UploadEntity> findUploadsFromLastDays(@Param("limitDate") LocalDateTime limitDate);
}
