package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection conectar() {
        Connection conexao = null;
        try {
            // Carregando o driver JDBC do PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Estabelecendo a conexão com o banco de dados PostgreSQL
            String url = "jdbc:postgresql://localhost:5432/testetecnico";
            String usuario = "postgres";
            String senha = "vaqueiro";

            conexao = DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao se conectar ao banco de dados."+ e.getMessage());
//            System.out.println("Erro ao conectar ao PostgreSQL: " + e.getMessage());
        }
        return conexao;
    }
}
