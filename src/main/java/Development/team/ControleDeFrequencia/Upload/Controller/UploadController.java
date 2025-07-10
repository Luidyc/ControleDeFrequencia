package Development.team.ControleDeFrequencia.Upload.Controller;

import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import Development.team.ControleDeFrequencia.Upload.Entity.UploadResponseDto;
import Development.team.ControleDeFrequencia.Upload.Services.UploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @GetMapping()
    public ResponseEntity<List<UploadResponseDto>> result() {
        return uploadService.getLastTen();
    }

    @PostMapping
    public ResponseEntity<UploadEntity> upload(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody @Valid UploadEntity upload) {
        String responsible = userDetails.getUsername();
        Optional<UploadEntity> response = Optional.ofNullable(uploadService.registry(responsible, upload));
        return response.map(uploadEntity -> new ResponseEntity<>(uploadEntity, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
