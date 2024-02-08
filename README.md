Configurações

Versão do JAVA : 13
Banco de dados: Postgres

Por conta de dificuldades na instalação da versao 11 e do banco SQLite, precisei utilizar a versão e banco mencionados acima, porém não barra a alteração do banco a que será feita a comunicação.
Para ajustar a base que será conectada, basta alterar as informações na classe ConnectionUtils. Nesta, conseguiremos configurar qualquer banco que seja(Lembrando que para isso, precisaremos incluir a LIB do banco que será trabalhado).

A aplicação tem como form principal a view MAIN. Se observar, nela temos o metodo main que instancia toda a aplicação, portanto, para inicia-la, so basta executar esta classe.
