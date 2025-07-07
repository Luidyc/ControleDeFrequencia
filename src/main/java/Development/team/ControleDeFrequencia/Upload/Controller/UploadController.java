package Development.team.ControleDeFrequencia.Upload.Controller;

import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import Development.team.ControleDeFrequencia.Upload.Services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @GetMapping()
    public String result() {
        return "<p>Olá krlho</p>";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadEntity> upload(
            @RequestParam("file")MultipartFile file,
            @RequestParam("username") String username
            ) {
        //String username =request.getResponsible(); //Será alterado para UserDetails pois preciso identificar no TOKEN.

        Optional<UploadEntity> response = Optional.ofNullable(uploadService.registry(username, file));
        if(response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
