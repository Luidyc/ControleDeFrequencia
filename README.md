# Controle de Ponto 🕒

Projeto simples para análise de batidas de ponto por CPF, com o objetivo de gerar relatórios de dias trabalhados consecutivamente.

## ✨ Funcionalidades

- Upload de planilha `.csv` ou `.xlsx` com dados de ponto (CPF, Nome, Data, Hora)
- API REST para processar os dados
- Relatório com os funcionários que mais trabalharam dias seguidos
- Classificação dos funcionários por risco (e.g., excesso de dias consecutivos)

## 🛠️ Tecnologias

- Java 17
- Spring Boot
- Apache POI (leitura de Excel)
- PostgreSQL (opcional)
- HTML + JavaScript (frontend básico)

## ▶️ Como executar localmente

### Pré-requisitos
- Java 17+
- Maven 3.8+
- (Opcional) PostgreSQL

### Passos

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/controledeponto.git
cd controledeponto

# Rode o backend
./mvnw spring-boot:run
