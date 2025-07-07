package Development.team.ControleDeFrequencia.RegistroPonto.Controller;

import Development.team.ControleDeFrequencia.RegistroDiaTrabalhado.Dto.FuncionarioEmRiscoDTO;
import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import Development.team.ControleDeFrequencia.RegistroPonto.Services.RegistroPontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorio")
public class RegistroDiaTrabalhadoController {

    @Autowired
    RegistroPontoService registroPonto;

    @GetMapping()
    public List<FuncionarioEmRiscoDTO> result() {
        return registroPonto.verify();
    }

}
