package Development.team.ControleDeFrequencia.Upload.Entity;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDto {
    private Long id;
    private LocalDateTime uploadDate;
    private String responsible;
    private String archiveName;
    private int quantidadeDeRegistros;
}
