package controller;

import model.Criterios;
import model.Tarefa;
import util.ConnectionUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CriteriosDAO {
    private Connection conexao;

    public CriteriosDAO() {
        ConnectionUtils conn = new ConnectionUtils();
        conexao = conn.conectar();
    }

    public void adicionarCriterio(Criterios criterio) throws Exception {
        try {
            String sql = " INSERT INTO criterios(descricao, idtarefa, concluido) VALUES (?,?,?)";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1,criterio.getDescricao());
            statement.setInt(2,criterio.getIdTarefa());
            statement.setBoolean(3,criterio.isConcluida());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erro ao Inserir a Criterio.");
        }
    }

    public List<Criterios> carregarCriterios(int idTarefa) {

        List<Criterios> criterios = new ArrayList<>();

        try {
            String sql = " SELECT * FROM criterios WHERE idTarefa = ?";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, idTarefa);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Criterios criterio = new Criterios(
                        resultSet.getInt("id"),
                        resultSet.getString("descricao"),
                        resultSet.getInt("idTarefa"),
                        resultSet.getBoolean("concluido")
                );

                criterios.add(criterio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return criterios;
    }

    public void removerCriterio(int id) throws Exception {
        try {
            String sql = " DELETE FROM criterios WHERE id = ? ";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1,id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erro ao Remover o Criterio.");
        }
    }

    public void removerCriterioPorTarefa(int id) throws Exception {
        try {
            String sql = " DELETE FROM criterios WHERE idtarefa = ? ";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1,id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erro ao Remover o Criterio.");
        }
    }

    public void atualizarConclusaoCriterio(int id, boolean status) throws Exception {
        try {

            String sql = " UPDATE criterios SET concluido = ?  WHERE id = ?";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setBoolean(1,status);
            statement.setInt(2,id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erro ao Atualizar o status do Criterio.");
        }
    }

}
