# Análise de registros de tempo

Aplicação containerizada que lê um arquivo `data.json` com registros de timesheet, processa os dados e gera um arquivo `result.json` com o resumo analítico.

## Tecnologias

- Java 21
- Maven
- Jackson (leitura e escrita de JSON)
- Docker

## Como executar

Com o Docker instalado e rodando, execute na raiz do projeto:

```bash
docker compose up --build
```

O arquivo `result.json` será gerado automaticamente na raiz do projeto.

## O que a aplicação calcula

- Total geral de minutos trabalhados
- Total de minutos por tarefa com percentual
- Tarefa mais trabalhada
- Top 3 tarefas por minutos
- Top 3 funcionários com maior total de minutos
- Usuário com mais tarefas distintas
- Quantidade de registros ignorados (minutes <= 0)