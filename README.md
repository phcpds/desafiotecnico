Configurações

Versão do JAVA : 11
Banco de dados: Postgres

Por conta de dificuldades na instalação do banco SQLite, precisei utilizar o banco mencionado acima, porém não barra a alteração do banco ja que será feita a comunicação atraves de classe de configuracao
Para ajustar a base que será conectada, basta alterar as informações na classe ConnectionUtils. Nesta, conseguiremos configurar qualquer banco que seja(Lembrando que para isso, precisaremos incluir a LIB do banco que será trabalhado).

A aplicação tem como form principal a view MAIN. Se observar, nela temos o metodo main que instancia toda a aplicação, portanto, para inicia-la, so basta executar esta classe.

Script de criação das tabelas disponivel na pasta scripts
