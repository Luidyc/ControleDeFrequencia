package Development.team.ControleDeFrequencia.RegistroPonto.Entity;

import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String name;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "upload_id")
    @JsonBackReference
    private UploadEntity upload;
}
