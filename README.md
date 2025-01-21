# Sistema de Restaurante com Java Spring Boot

## Sobre o Projeto

Este é um sistema de gerenciamento para restaurantes, desenvolvido em Java utilizando o framework Spring Boot. O objetivo do sistema é facilitar o cadastro de usuários, o gerenciamento de mesas, pedidos de clientes e o processamento de pagamentos. Além disso, irá ser implementada uma funcionalidade inovadora que permite que os clientes recebam a conta por e-mail, oferecendo a opção de pagamento posterior, sem a necessidade de realizar o pagamento diretamente no restaurante.

## Funcionalidades

- **Cadastro de Usuários**: Permite o registro de clientes e funcionários, com autenticação e controle de acesso.
- **Gerenciamento de Mesas**: Controle das mesas disponíveis, status de ocupação e associação de pedidos às mesas.
- **Gestão de Pedidos**: Criação, edição e cancelamento de pedidos vinculados a mesas e usuários.
- **Processamento de Pagamentos**:
  - Pagamento direto no restaurante.
  - **Pagamento via e-mail**: Envio da conta por e-mail para o cliente, com um link para pagamento posterior.

## Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Framework**: Spring Boot
- **Banco de Dados**: PostgreSQL
- **Ferramentas**:
  - Spring Data JPA (para persistência de dados)
  - Spring Security (para autenticação e controle de acesso)
  - Thymeleaf (para renderização de páginas)
  - (Irá ser implementado) API de envio de e-mails (exemplo: JavaMailSender)
- **Frontend**: Bootstrap, HTML5, CSS3 (Bootstrap), JavaScript

## EM DESENVOLVIMENTO
- Padronizar e mudar o acesso aos campos do formulário nos Controllers e Services. Atualmente estou pegando por atributo pelo @RequestParam mas vou mudar para receber o objeto inteiro via th:object no formulário, afim de validar melhor no Service os campos obrigatórios.
- Criar telas de cadastro e listagem dos itens do cardápio para serem associadas nas mesas do restaurante.

## PRÓXIMAS ISSUES 
- (BANCO/BACKEND) Criar banco de dados (Models/Entities) das mesas, itens do cardápio e clientes sendo relacionadas entre eles.
- (FRONTEND) Criar telas de cadastro de itens e clientes.
- (FRONTEND/BACKEND) Utilizar API de validação de cadastro de CPF requerido no cadastro de clientes.

## Melhorias Futuras

- Implementar integração com gateways de pagamento populares.
- Adicionar relatórios gerenciais para o restaurante.
- Inserir autenticação de 2 fatores para login e cadastro no sistema.
- Implementar OAuth 2.0 para ususários do sistema.
- Implementar Angular no projeto utilizando a aplicação SPA (Single Page Application) para o frontend.

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

Sendo desenvolvido por Lucas Matheus de Camargo.
