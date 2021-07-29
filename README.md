# 	:leaves: Desafio-Back-end-IBM
Desafio Técnico Back-end para implementar uma API para votação de pautas entre os associados.

## :hammer_and_wrench: Tecnologias utilizadas na aplicação

- **Spring Boot**
- **JPA**
- **Lombok**
- **Spring Security**
- **JUnit**
- **Bean Validation**

## :pencil:  Requisitos para rodar a aplicação
Certifique-se que você tenha instalado os seguintes softwares:
- *[MySQL Workbench](https://www.mysql.com/products/workbench/)*
- *[Postman ou Insomnia](https://www.postman.com/)*
- *[IntelliJ ou eclipse](https://www.jetbrains.com/pt-br/idea/download/)*
- *[Java 8 ou +](https://www.java.com/pt-BR/download/ie_manual.jsp?locale=pt_BR)*

Insira também seu usuário e senha do banco de dados em application.properties nos campos abaixo
```bash 
spring.datasource.username= "insira seu usuário"
spring.datasource.password= "insira sua senha"
```

- Após isso, execute os seguintes comandos no terminal.
```
git clone https://github.com/gschifer/Desafio-Back-end-IBM.git
```

- Acesse o diretório do projeto
```
cd Desafio-Back-end-IBM
```

- Inicie a aplicação
```
mvn spring-boot:run
```

- Você pode acessar para métodos GET via browser, mas para outros métodos é necessário que você utilize
o Postman para acessá-los.
```
Acesse http://localhost:8081/api/v1/associados
ou outras rotas conforme documentação do swagger fornecida abaixo
```

## :twisted_rightwards_arrows:		 Rotas da API
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/RotasAPI.png)

## Documentação de erros - Método GET
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosGet.png)
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosGet2.png)

## Documentação de erros - Método POST
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosPost.png)
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosPost2.png)

## Documentação de erros - Método DELETE
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosDelete.png)
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosDelete2.png)

## Documentação de erros - Método PUT
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosPut.png)
![](https://github.com/gschifer/Desafio-Back-end-IBM/blob/master/src/main/java/com/example/challenge/images/documentacaoDeErrosPut2.png)


