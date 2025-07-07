package Development.team.ControleDeFrequencia.RegistroPonto.Services;

import Development.team.ControleDeFrequencia.RegistroDiaTrabalhado.Dto.FuncionarioEmRiscoDTO;
import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import Development.team.ControleDeFrequencia.RegistroPonto.Repository.RegistroPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegistroPontoService {

    @Autowired
    RegistroPontoRepository registroPontoRepository;

    public List<FuncionarioEmRiscoDTO> verify() {

        List<RegistroPonto> allRegistries = registroPontoRepository.findFuncionariosCom6DiasConsecutivos();
        List<FuncionarioEmRiscoDTO> resultado = new ArrayList<>();
        Map<String, List<RegistroPonto>> porFuncionario = allRegistries.stream()
                .collect(Collectors.groupingBy(RegistroPonto::getCpf));

        for (Map.Entry<String, List<RegistroPonto>> entry : porFuncionario.entrySet()) {
            String cpf = entry.getKey();
            List<RegistroPonto> dias = entry.getValue();

            // Ordenar do mais recente para o mais antigo
            dias.sort(Comparator.comparing(RegistroPonto::getDate).reversed());

            int consecutivos = 1;

            for (int i = 1; i < dias.size(); i++) {
                LocalDate atual = dias.get(i-1).getDate();
                LocalDate anterior = dias.get(i).getDate();

                if (atual.minusDays(1).equals(anterior)) {
                    consecutivos++;
                } else {
                    break; // houve folga, zera
                }
            }

            String nome = dias.get(0).getName();
            resultado.add(new FuncionarioEmRiscoDTO(cpf, nome, consecutivos));
        }

        return resultado;
    }
}
