package Development.team.ControleDeFrequencia.Upload.Services;

import Development.team.ControleDeFrequencia.RegistroDiaTrabalhado.Entity.RegistroDiaTrabalhado;
import Development.team.ControleDeFrequencia.RegistroPonto.Entity.RegistroPonto;
import Development.team.ControleDeFrequencia.Upload.Entity.UploadEntity;
import Development.team.ControleDeFrequencia.Upload.Entity.UploadResponse;
import Development.team.ControleDeFrequencia.Upload.Repository.UploadRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Autowired
    UploadRepository uploadRepository;

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString(); // ou .toString()
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // ex: CPF sem casas decimais
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // ou cell.getStringCellValue() dependendo do seu uso
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public UploadEntity registry(String username, MultipartFile file) {
        // 1. Começo buscando registrar quem trabalhou.

        Map<String, RegistroDiaTrabalhado> UniqueRegistry = new HashMap<>();
        // Capturando o input da request e passando para a depedência que ler xls.
        try(InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for(Row row : sheet) {
                if(row.getRowNum() == 0) continue; // Pular cabeçalho

                String cpf = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String dateStr = getCellValueAsString(row.getCell(2));
                //Possível utilizar o horário.
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);

                //Criando cheve única do Cpf+Dia Trabalhado
                String chave = cpf+"_"+dateStr;

                //Só adiciona se não estiver no map.
                if(!UniqueRegistry.containsKey(chave)) {
                    UniqueRegistry.put(chave,new RegistroDiaTrabalhado(cpf,name,date));
                }
            }

        //Convertendo para o Banco de dados.
        List<RegistroPonto> pontos = UniqueRegistry.values().stream()
                .map(r->{
                    RegistroPonto ponto = new RegistroPonto();
                    ponto.setCpf(r.getCpf());
                    ponto.setName(r.getName());
                    ponto.setDate(r.getDate());
                    return ponto;
                })
                .toList();

        //Upload
        UploadEntity upload = new UploadEntity();
        upload.setUploadDate(LocalDateTime.now());
        upload.setResponsible(username);
        upload.setArchiveName(file.getOriginalFilename());
        upload.setRegistry(pontos);
        pontos.forEach(p-> p.setUpload(upload));

        return uploadRepository.save(upload);

        } catch (IOException e) {
            return null;
        }

    }


    public ResponseEntity<List<UploadResponse>> getLastTen() {
        LocalDateTime dezDiasAtras = LocalDateTime.now().minusDays(10);
        List<UploadEntity> uploads = uploadRepository.findUploadsFromLastDays(dezDiasAtras);

        List<UploadResponse> response = uploads.stream()
                .map(upload-> new UploadResponse(
                        upload.getId(),
                        upload.getUploadDate(),
                        upload.getResponsible(),
                        upload.getArchiveName(),
                        upload.getRegistry() != null ? upload.getRegistry().size() : 0
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

}
