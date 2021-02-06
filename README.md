# Repita portal - Console

<div>
  <img width="200px" src="https://avatars0.githubusercontent.com/u/70340698?s=200&v=4" />
  <img width="200px" src="https://github.com/Hugo-Carvalho/repita-rpa-maker/blob/master/src/assets/title.PNG" />
</div>
<br />

### Console de gerenciamento para configuração de cronogramas de execução de aplicações JAVA

Desenvolvidor com <a href="https://spring.io" target="_blank">Spring</a> e Thymeleaf, utiliza de uma schedule que a cada 1 minuto verifica se algum cronograma cadastrado deve ser executado. Quando é localizado um cronograma, o console se conecta com o receptor vinculado e executa o JAR no mesmo.

Este console depende de um receptor (<a href="https://github.com/SikLabTech/repita-portal-receptor">click aqui para acessar o repositório</a>).

## Execução

O console depende da conexão com o banco de dados MySQL.

Realize o apontamento no ``application.properties``, conforme já configurado o exemplo:

```
spring.datasource.url=jdbc:mysql://localhost:3306/portalrpa
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=
spring.jpa.database-plataform=org.hibernate.dialect.MySQLDialect
```

Na primeira execução a linha ``spring.jpa.hibernate.ddl-auto=create`` ira ser responsavel por construir as tabelas necessárias no banco de dados. Após a primeira execução troque esta linha pela que esta comentada ``#spring.jpa.hibernate.ddl-auto=none`` para que o banco de dados não seja limpo sempre que a aplicação for executada.

Para um primeiro acesso crie um usuário no banco de dados com permissões de administrador, execute a query abaixo:

```
INSERT INTO `usuario`(`id`, `login`, `nome`, `senha`, `tipo`) VALUES (1,'admin','Administrador','admin','admin')
```

Tudo certo! O console esta sendo executado na porta 50080 (http://localhost:50080) e seu primeiro login é admin e senha admin (recomendamos trocar-los após o primeiro acesso).
