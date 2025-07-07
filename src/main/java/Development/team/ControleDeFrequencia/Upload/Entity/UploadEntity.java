package Development.team.ControleDeFrequencia.Upload.Entity;

import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime uploadDate;

    private String responsible;

    private String archiveName;

    @OneToMany(mappedBy = "upload", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RegistroPonto> registry = new ArrayList<>();

}
