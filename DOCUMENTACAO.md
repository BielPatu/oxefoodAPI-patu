# Documentação Detalhada do Projeto OxeFood API

---

## Visão Geral da Arquitetura
O oxefoodAPI-patu segue o padrão arquitetural REST, utilizando Java com Spring Boot. O projeto é dividido em camadas:
- **Controller:** expõe endpoints HTTP e recebe requisições do cliente.
- **Service:** contém a lógica de negócio e orquestra operações entre entidades e repositórios.
- **Repository:** abstrai o acesso ao banco de dados, geralmente estendendo interfaces do Spring Data JPA.
- **Modelo/Entidades:** representam as tabelas do banco de dados e os objetos de domínio.
- **Utilitários:** funções auxiliares, tratamento de erros, envio de e-mails, etc.

O projeto utiliza injeção de dependências do Spring, anotações para validação, segurança, documentação e transações.

---

## Sumário
1. [Introdução](#introducao)
2. [Autenticação e JWT](#jwt)
3. [Gerenciamento de Usuários](#usuarios)
4. [Gestão de Clientes](#clientes)
5. [Gestão de Funcionários](#funcionarios)
6. [Gestão de Produtos e Categorias](#produtos)
7. [Segurança e Permissões](#seguranca)
8. [Documentação da API (Swagger)](#swagger)
9. [Envio de E-mails](#email)
10. [Tratamento de Erros](#erros)
11. [Utilitários](#util)

---

## 1. <a name="introducao"></a>Introdução
Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, responsável por gerenciar clientes, funcionários, produtos e autenticação de usuários, utilizando JWT para segurança. A seguir, cada funcionalidade é detalhada, com explicações de trechos de código relevantes.

---


## 2. <a name="jwt"></a>Autenticação, JWT e Segurança
A autenticação é baseada em JWT (JSON Web Token), um padrão seguro e stateless para autenticação de APIs.

### Como funciona o JWT
O JWT é um token assinado digitalmente, composto por três partes (header, payload, signature). Ele é gerado após o login e enviado em todas as requisições subsequentes no header `Authorization`.

#### Exemplo de payload JWT:
```json
{
  "sub": "usuario@email.com",
  "iat": 1700000000,
  "exp": 1700003600
}
```

### Fluxo detalhado de autenticação
1. O usuário faz POST em `/api/auth` com JSON:
   ```json
   { "username": "usuario", "password": "senha" }
   ```
2. O `AuthenticationController` recebe e chama:
   ```java
   Usuario authenticatedUser = usuarioService.authenticate(data.getUsername(), data.getPassword());
   ```
   - Usa o `AuthenticationManager` do Spring para validar as credenciais.
   - Se inválido, retorna erro 401.
3. Se válido, o `JwtService` gera o token:
   ```java
   String jwtToken = jwtService.generateToken(authenticatedUser);
   ```
   - O token inclui o username e data de expiração.
4. O token é retornado ao cliente:
   ```json
   { "username": "usuario", "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...", "tokenExpiresIn": 3600000 }
   ```
5. O cliente deve enviar o token em todas as requisições protegidas:
   ```http
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
   ```

### Validação do Token e Filtro de Segurança
- O filtro `JwtAuthenticationFilter` intercepta todas as requisições:
  ```java
  final String authHeader = request.getHeader("Authorization");
  if (authHeader != null && authHeader.startsWith("Bearer ")) {
      final String jwt = authHeader.substring(7);
      final String userEmail = jwtService.extractUsername(jwt);
      // ...
  }
  ```
- O filtro valida a assinatura, expiração e se o usuário existe.
- Se tudo ok, autentica o usuário no contexto do Spring Security.

#### Expiração e Segurança
- O tempo de expiração é configurado em `application.properties`:
  ```properties
  security.jwt.expiration-time=3600000
  ```
- O token é assinado com uma chave secreta:
  ```java
  @Value("${security.jwt.secret-key}")
  private String secretKey;
  ```
- Se o token expirar ou for inválido, a requisição é negada.

#### Observações de Segurança
- O backend é stateless: não armazena sessão, apenas valida o token.
- O JWT pode ser invalidado apenas expirando ou trocando a chave secreta.
- O filtro trata exceções e retorna mensagens apropriadas.

#### Anotações importantes
- `@RestController`: indica que a classe expõe endpoints REST.
- `@RequestMapping("/api/auth")`: define o prefixo dos endpoints.
- `@PostMapping`: mapeia requisições POST.
- `@Autowired`: injeta dependências automaticamente.

#### Exemplo de resposta de erro
```json
{
  "timestamp": "2025-11-19T12:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciais inválidas"
}
```

---


## 3. <a name="usuarios"></a>Gerenciamento de Usuários
O `UsuarioService` centraliza a lógica de autenticação, cadastro e busca de usuários.

### Principais métodos e responsabilidades
- **Autenticação:**
  ```java
  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  ```
  - Valida usuário e senha usando o mecanismo do Spring Security.
- **Codificação de senha:**
  ```java
  user.setPassword(passwordEncoder.encode(user.getPassword()));
  ```
  - Garante que senhas nunca sejam salvas em texto puro.
- **Busca de usuário logado:**
  ```java
  public Usuario obterUsuarioLogado(HttpServletRequest request) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null) {
          String jwt = authHeader.substring(7);
          String userEmail = jwtService.extractUsername(jwt);
          return findByUsername(userEmail);
      }
      return null;
  }
  ```
- **Cadastro de usuário:**
  - Salva o usuário e define como habilitado.
- **Implementação de UserDetailsService:**
  - Permite integração nativa com o Spring Security.

### Observações
- O serviço é anotado com `@Service`, tornando-o um bean gerenciado pelo Spring.
- Utiliza transações para garantir atomicidade nas operações de escrita.

---


## 4. <a name="clientes"></a>Gestão de Clientes
A API de clientes é responsável por todo o ciclo de vida do cliente, incluindo cadastro, consulta, atualização, exclusão lógica e gerenciamento de endereços.

### Cadastro de Cliente
- Recebe um `ClienteRequest` (DTO), converte para entidade e salva no banco.
- Associa perfis ao usuário (roles) e envia e-mail de boas-vindas:
  ```java
  usuarioService.save(cliente.getUsuario());
  emailService.enviarEmailConfirmacaoCadastroCliente(c);
  ```
- O método é anotado com `@Transactional` para garantir atomicidade.

### Listagem e Consulta
- `listarTodos()`: retorna todos os clientes ativos.
- `obterPorID(Long id)`: busca cliente pelo ID.

### Atualização e Exclusão Lógica
- Atualiza dados do cliente, mantendo histórico de modificações.
- Exclusão lógica: apenas desabilita o cliente, não remove do banco (soft delete):
  ```java
  cliente.setHabilitado(Boolean.FALSE);
  repository.save(cliente);
  ```

### Gerenciamento de Endereços
- Permite adicionar, atualizar e remover endereços associados ao cliente.
- Mantém integridade relacional e histórico de endereços.

### Observações
- Utiliza validação com anotações como `@Valid` e tratamento de erros para campos obrigatórios.
- O envio de e-mail é assíncrono, não bloqueando a requisição.

---


## 5. <a name="funcionarios"></a>Gestão de Funcionários
A API de funcionários gerencia o ciclo de vida dos funcionários, com diferenciação de perfis (admin/operador).

### Cadastro de Funcionário
- Recebe um `FuncionarioRequest`, converte para entidade.
- Define o perfil do funcionário conforme o tipo:
  ```java
  if (funcionarioNovo.getTipo().equals(TipoFuncionario.ADMINISTRADOR)) {
      funcionarioNovo.getUsuario().getRoles().add(new Perfil(Perfil.ROLE_FUNCIONARIO_ADMIN));
  }
  ```
- Salva o funcionário e seus perfis.

### Listagem, Consulta, Atualização e Exclusão
- Métodos similares ao cliente, com atualização de dados pessoais e endereço.
- Exclusão lógica: desabilita o funcionário, mantendo histórico.

### Observações
- Utiliza validação, transações e controle de permissões por perfil.

---


## 6. <a name="produtos"></a>Gestão de Produtos e Categorias
A API de produtos permite o gerenciamento completo de produtos e suas categorias.

### Cadastro de Produto
- Recebe um `ProdutoRequest`, converte para entidade.
- Valida valor mínimo do produto:
  ```java
  if (produto.getValorUnitario() < 10) {
      throw new ProdutoException(ProdutoException.MSG_VALOR_MINIMO_PRODUTO);
  }
  ```
- Associa o produto a uma categoria existente.

### Listagem, Consulta, Atualização e Exclusão
- Listagem de todos os produtos ativos.
- Consulta por ID.
- Atualização de dados e exclusão lógica (soft delete).

### Filtro de Produtos
- Permite filtrar produtos por código, título ou categoria, usando métodos específicos do repositório.

### Upload de Imagens
- Permite upload de imagens para produtos:
  ```java
  String imagemUpada = Util.fazerUploadImagem(imagem);
  produto.setImagem(imagemUpada);
  ```
- As imagens são salvas em diretório específico, com nome único baseado em data/hora.

### Observações
- Utiliza validação, tratamento de exceções customizadas e integrações com utilitários.

---


## 7. <a name="seguranca"></a>Segurança, Permissões e CORS
A segurança é configurada em `SecurityConfiguration`.

### Controle de Acesso
- Define quais endpoints são públicos e quais exigem autenticação.
- Usa perfis (roles) para restringir operações sensíveis:
  ```java
  .requestMatchers(HttpMethod.PUT, "/api/produto/*").hasAnyAuthority(
      Perfil.ROLE_FUNCIONARIO_ADMIN,
      Perfil.ROLE_FUNCIONARIO_USER)
  ```
- Exemplo: apenas administradores podem excluir produtos.

### Stateless e Sessão
- O sistema é stateless: não mantém sessão no servidor, apenas valida o JWT.

### CORS
- Configurado para permitir requisições do frontend (ex: React em localhost:3000):
  ```java
  configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
  ```

### Observações
- Utiliza `@EnableWebSecurity` e beans do Spring Security.
- Adiciona o filtro JWT antes do filtro padrão de autenticação.

---


## 8. <a name="swagger"></a>Documentação da API (Swagger/OpenAPI)
O projeto utiliza o Swagger (OpenAPI) para documentação automática dos endpoints.

### Funcionalidades
- Exposição dos endpoints `/api-docs` (JSON) e `/swagger-ui` (interface web interativa).
- Permite testar endpoints diretamente pelo navegador.
- Documentação gerada a partir de anotações como `@Operation` e `@Tag` nos controllers.

### Exemplo de anotação:
```java
@Operation(
  summary = "Serviço responsável por salvar um cliente no sistema.",
  description = "Endpoint para inserir um cliente."
)
```

### Configuração
- Definida em `SwaggerConfig`:
  ```java
  .info(new Info().title("OxeFood API").version("1.0").description("API do OxeFood"))
  ```

---


## 9. <a name="email"></a>Envio de E-mails
O `EmailService` é responsável por toda a comunicação por e-mail do sistema.

### Funcionalidades
- Envia e-mails de confirmação de cadastro para novos clientes.
- Utiliza templates HTML (Thymeleaf) para personalizar mensagens.
- Configuração flexível via propriedades do Spring (`application.properties`).
- Envio assíncrono para não bloquear requisições.

### Exemplo de envio:
```java
this.sendMailTemplate(
  "bem_vindo_cliente.html", 
  cliente.getUsuario().getUsername(), 
  assuntoEmail, 
  params);
```

### Configuração SMTP
- Parâmetros como host, porta, usuário e senha são configurados via propriedades.
- Suporte a TLS e autenticação.

### Observações
- O envio de e-mail pode ser expandido para outras notificações (ex: recuperação de senha).

---


## 10. <a name="erros"></a>Tratamento de Erros e Validações
O `TratadorErros` centraliza o tratamento de exceções e validações.

### Funcionalidades
- Captura exceções globais e retorna mensagens padronizadas.
- Para erros de validação, retorna lista de campos e mensagens:
  ```json
  {
    "errors": [
      { "fieldName": "email", "defaultMessage": "não pode estar em branco" }
    ]
  }
  ```
- Para erros internos, retorna mensagem genérica e status 500.

### Observações
- Utiliza `@RestControllerAdvice` e `@ExceptionHandler`.
- Facilita o consumo da API por frontends, que podem exibir mensagens amigáveis ao usuário.

---


## 11. <a name="util"></a>Utilitários e Helpers
A classe `Util` e outros utilitários fornecem funções auxiliares para o sistema.

### Upload de Imagens
- Salva arquivos em diretórios específicos, nomeando-os com data/hora para evitar conflitos:
  ```java
  public static String fazerUploadImagem(MultipartFile imagem) {
      // ...
      File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeArquivoComDataHora);
      stream.write(imagem.getBytes());
      // ...
  }
  ```
- Cria diretórios automaticamente se não existirem.
- Retorna o nome do arquivo salvo ou null em caso de erro.

### Outras funções possíveis
- Conversão de datas, manipulação de strings, geração de logs, etc.

---

## 12. Integração, Testes e Boas Práticas

### Integração
- O projeto pode ser facilmente integrado a frontends modernos (React, Angular, Vue) via REST.
- Suporte a CORS e autenticação JWT facilita integração com SPAs e mobile.

### Testes
- Estrutura para testes automatizados (JUnit/Spring Test) está presente.
- Recomenda-se criar testes para controllers, services e integrações.

### Boas Práticas Adotadas
- Uso de DTOs para entrada/saída de dados.
- Separação clara de camadas.
- Validação e tratamento de erros centralizados.
- Documentação automática.
- Soft delete para entidades.
- Uso de transações para consistência.

---

## 13. Exemplos de Requisições e Respostas

### Cadastro de Cliente (POST /api/cliente)
**Request:**
```json
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "dataNascimento": "1990-01-01",
  "usuario": {
    "username": "joao",
    "password": "senha123"
  }
}
```
**Response:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "usuario": {
    "username": "joao"
  }
}
```

### Login (POST /api/auth)
**Request:**
```json
{
  "username": "joao",
  "password": "senha123"
}
```
**Response:**
```json
{
  "username": "joao",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
  "tokenExpiresIn": 3600000
}
```

### Erro de Validação
**Response:**
```json
{
  "errors": [
    { "fieldName": "cpf", "defaultMessage": "CPF inválido" }
  ]
}
```

---

## 14. Observações Finais e Expansão
- O projeto está pronto para ser expandido com novos módulos (pedidos, pagamentos, etc).
- Pode ser facilmente adaptado para microserviços.
- O uso de padrões modernos garante escalabilidade e manutenção.

---

Se precisar de exemplos de uso dos endpoints, explicações de cada classe, ou detalhes de integração, basta pedir!

---

## Observações Finais
- O projeto segue boas práticas de arquitetura REST, segurança e organização de código.
- O uso de JWT, tratamento de erros, envio de e-mails e documentação Swagger tornam a API robusta e pronta para produção.

Se precisar de exemplos de uso dos endpoints ou explicações ainda mais detalhadas, basta pedir!
