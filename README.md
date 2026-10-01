# Houpper Common Library

Biblioteca compartilhada da plataforma Houpper destinada a centralizar funcionalidades comuns utilizadas por diferentes
serviços da plataforma.

O objetivo da biblioteca é evitar duplicação de código, padronizar comportamentos e fornecer componentes reutilizáveis
sem introduzir dependências desnecessárias nos serviços consumidores.

---

## 1. Objetivo

A `common-lib` concentra funcionalidades transversais da plataforma Houpper.

Entre seus objetivos estão:

* Padronizar comportamentos compartilhados.
* Reduzir duplicação de código.
* Centralizar contratos técnicos comuns.
* Padronizar tratamento de exceções.
* Padronizar respostas de erro.
* Integrar o tratamento de erros entre serviços através do OpenFeign.
* Permitir evolução modular da biblioteca.
* Evitar dependências transitivas desnecessárias.

A biblioteca não deve conter regras específicas de negócio de nenhum serviço.

---

## 2. Estrutura

A estrutura da biblioteca é organizada por módulos funcionais:

```text
br.com.houpper.common
├── exception
│   ├── configuration
│   ├── exceptions
│   ├── feign
│   └── model
│
└── validation_ex
```

Nem todos os módulos precisam existir desde o início.

Novas funcionalidades devem ser adicionadas de forma isolada para evitar acoplamento entre responsabilidades diferentes.

---

# 3. Módulo Exception

O módulo `exception` fornece mecanismos comuns para tratamento de erros entre os serviços Houpper.

Principais componentes:

* `BusinessException`
* Exceções HTTP padronizadas
* `GlobalExceptionHandler`
* `ProblemDetails`
* `ValidationProblemDetails`
* `FeignErrorDecoder`
* Auto-configurações Spring Boot

---

## 4. BusinessException

`BusinessException` é a classe base para exceções de negócio que possuem uma representação HTTP conhecida.

```java
public abstract class BusinessException extends RuntimeException {
    
    private final String title;
    private final HttpStatus httpStatus;
}
```

Exemplo:

```java
throw new BadRequestException("Schema inválido");
```

A exceção será convertida automaticamente em uma resposta HTTP padronizada.

---

# 5. Exceções disponíveis

O módulo possui exceções associadas aos principais códigos HTTP utilizados pela plataforma.

| Exceção                        | HTTP |
| ------------------------------ | ---: |
| `BadRequestException`          |  400 |
| `UnauthorizedException`        |  401 |
| `ForbiddenException`           |  403 |
| `NotFoundException`            |  404 |
| `ConflictException`            |  409 |
| `UnprocessableEntityException` |  422 |
| `InternalServerErrorException` |  500 |
| `ServiceUnavailableException`  |  503 |

Exemplo:

```java
throw new ConflictException(
    "O registro informado já existe."
);
```

---

# 6. Problem Details

As respostas de erro seguem o conceito de Problem Details for HTTP APIs.

Exemplo:

```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "O registro informado já existe.",
  "errorCode": "CONFLICT",
  "traceId": "8b1f...",
  "instance": "/api/users",
  "timestamp": "2026-09-29T21:00:00"
}
```

Os principais campos são:

| Campo       | Descrição                         |
| ----------- | --------------------------------- |
| `type`      | Identificador do tipo do problema |
| `title`     | Título resumido                   |
| `status`    | Código HTTP                       |
| `detail`    | Detalhes do erro                  |
| `errorCode` | Código utilizado pela plataforma  |
| `traceId`   | Identificador de rastreamento     |
| `instance`  | URI da requisição                 |
| `timestamp` | Momento da ocorrência             |

---

# 7. Erros de validação

Erros provenientes de `@Valid` são convertidos para `ValidationProblemDetails`.

Exemplo:

```json
{
  "type": "about:blank",
  "title": "Field validation error",
  "status": 400,
  "detail": "Field validation error",
  "errorCode": "BAD_REQUEST",
  "traceId": "8b1f...",
  "instance": "/api/users",
  "timestamp": "2026-09-29T21:00:00",
  "fields": {
    "name": "must not be blank",
    "email": "must be a well-formed email address"
  }
}
```

O campo `fields` permite que o frontend identifique diretamente quais propriedades da requisição possuem erro.

---

# 8. GlobalExceptionHandler

Aplicações Spring MVC recebem automaticamente um `GlobalExceptionHandler` através da auto-configuração:

```java
@AutoConfiguration
@ConditionalOnWebApplication(
    type = ConditionalOnWebApplication.Type.SERVLET
)
@ConditionalOnClass(RestControllerAdvice.class)
public class ExceptionAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
```

A configuração somente é ativada em aplicações Servlet.

Aplicações que não utilizam Spring MVC não recebem esse componente.

---

# 9. OpenFeign

A biblioteca também padroniza o tratamento de erros recebidos de outros serviços através do OpenFeign.

O `FeignErrorDecoder` converte respostas HTTP em exceções da plataforma.

```text
HTTP 400 → BadRequestException
HTTP 401 → UnauthorizedException
HTTP 403 → ForbiddenException
HTTP 404 → NotFoundException
HTTP 409 → ConflictException
HTTP 422 → UnprocessableEntityException
HTTP 500 → InternalServerErrorException
HTTP 503 → ServiceUnavailableException
```

Quando o serviço remoto retorna:

```json
{
  "detail": "Tenant não encontrado."
}
```

o decoder utilizará:

```text
Tenant não encontrado.
```

como mensagem da exceção.

---

# 10. Fallback do Feign

Códigos HTTP não tratados explicitamente são delegados ao decoder padrão do Feign.

```java
default -> DEFAULT_ERROR_DECODER.decode(methodKey, response);
```

Isso evita que a biblioteca tente assumir responsabilidade sobre todos os comportamentos possíveis do OpenFeign.

---

# 11. Auto-configuração

A biblioteca utiliza as auto-configurações do Spring Boot.

Arquivo:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Conteúdo:

```text
br.com.houpper.common.exception.configuration.ExceptionAutoConfiguration
br.com.houpper.common.exception.configuration.FeignAutoConfiguration
```

As funcionalidades são ativadas somente quando suas dependências e condições estão disponíveis.

---

# 12. Dependências opcionais

A biblioteca utiliza `compileOnly` para funcionalidades específicas.

Exemplo:

```groovy
compileOnly 'org.springframework.boot:spring-boot-starter-webmvc'
compileOnly 'org.springframework.security:spring-security-core'
compileOnly 'org.springframework.cloud:spring-cloud-starter-openfeign'
compileOnly 'org.springframework.boot:spring-boot-autoconfigure'
```

A intenção é evitar que a utilização da `common-lib` introduza automaticamente dependências desnecessárias nos serviços
consumidores.

---

# 13. Personalização

Os beans fornecidos pela biblioteca utilizam `@ConditionalOnMissingBean`.

Isso permite que uma aplicação substitua o comportamento padrão.

Exemplo:

```java
@Bean
ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
    return new CustomErrorDecoder(objectMapper);
}
```

Nesse caso, o `FeignErrorDecoder` fornecido pela biblioteca não será registrado.

---

# 14. Instalação

Adicione a biblioteca ao serviço consumidor:

```groovy
dependencies {
    implementation 'br.com.houpper:common-lib:1.0.0'
}
```

---

# 15. Publicação local

Para publicar no Maven Local:

```bash
./gradlew publishToMavenLocal
```

Depois:

```groovy
repositories {
    mavenLocal()
}
```

e:

```groovy
dependencies {
    implementation 'br.com.houpper:common-lib:1.0.0-SNAPSHOT'
}
```

---

# 16. Publicação

A biblioteca utiliza `maven-publish` para geração e publicação dos artefatos Maven.

A publicação possui:

* JAR principal;
* Sources JAR;
* Javadoc JAR.

Configuração:

```groovy
publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
}
```

---

# 17. GitHub Packages

A `common-lib` é publicada como um pacote Maven no **GitHub Packages**, utilizando o repositório da organização Houpper.

Repositório:

```text
https://maven.pkg.github.com/houpper/common-lib
```

O pacote é identificado pelo seguinte coordenada Maven:

```text
br.com.houpper:common-lib
```

Exemplo de dependência:

```groovy
dependencies {
    implementation 'br.com.houpper:common-lib:1.0.0'
}
```

Para autenticação no GitHub Packages, o ambiente de desenvolvimento deve fornecer:

```bash
export GITHUB_ACTOR="seu-usuario"
export GITHUB_TOKEN="seu-token"
```

A configuração do repositório Maven utiliza essas variáveis:

```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/houpper/common-lib")

        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
```

O token utilizado deve possuir as permissões necessárias para acesso aos pacotes da organização.

O token não deve ser armazenado no código-fonte, versionado no Git ou incluído diretamente nos arquivos de configuração do projeto.

---

# 18. Publicação local

Para publicar no Maven Local:

```bash
./gradlew publishToMavenLocal
```

Depois:

```groovy
repositories {
    mavenLocal()
}
```

E:

```groovy
dependencies {
    implementation 'br.com.houpper:common-lib:1.0.0-SNAPSHOT'
}
```

A publicação local deve ser utilizada principalmente durante o desenvolvimento e validação da biblioteca.

---

# 19. Publicação via Gradle

A publicação no GitHub Packages pode ser executada através da task `publish`:

```bash
./gradlew publish
```

O Gradle utiliza as credenciais fornecidas pelas variáveis de ambiente:

```text
GITHUB_ACTOR
GITHUB_TOKEN
```

O processo atual de publicação é manual e permite que uma versão específica da biblioteca seja publicada no GitHub Packages após sua validação.

---

# 20. CI/CD

> **TODO:** Implementar pipeline de CI/CD para automatizar o processo de validação, versionamento e publicação da `common-lib`.

A implementação futura deverá avaliar um fluxo semelhante a:

```text
Commit
   ↓
Build
   ↓
Testes
   ↓
Validação
   ↓
Criação da versão
   ↓
GitHub Packages
```

O pipeline deverá, preferencialmente:

* Executar os testes automatizados.
* Validar o build da biblioteca.
* Gerar JAR, Sources JAR e Javadoc JAR.
* Publicar automaticamente versões aprovadas.
* Utilizar as credenciais fornecidas pelo GitHub Actions.
* Evitar armazenamento de tokens no código-fonte.
* Permitir rastreabilidade entre uma versão publicada e o commit correspondente.
* Avaliar publicação baseada em Git tags ou GitHub Releases.
* Definir uma estratégia para versões `SNAPSHOT` e versões estáveis.

### Possível fluxo futuro

```text
Pull Request
     ↓
Build + Testes
     ↓
Merge
     ↓
Git Tag
     ↓
GitHub Actions
     ↓
Build
     ↓
Testes
     ↓
Publish
     ↓
GitHub Packages
```

A definição final do processo de CI/CD será documentada quando a automação for implementada.
