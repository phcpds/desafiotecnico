package controller;

import model.Criterios;
import model.Tarefa;
import util.ConnectionUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TarefaDAO {
    private Connection conexao;
    private CriteriosDAO criteriosDAO = new CriteriosDAO();

    public TarefaDAO() {
        ConnectionUtils conn = new ConnectionUtils();
        conexao = conn.conectar();
    }

    // Métodos CRUD
    public void adicionarTarefa(Tarefa tarefa) throws Exception {
        try {
            LocalDate date = LocalDate.parse(tarefa.getDataCriacao());
            int idGerado = 0;

            String sql = " INSERT INTO tarefas(nome,descricao,dt_criacao,status,categoria) VALUES (?,?,?,?,?)";

            PreparedStatement statement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,tarefa.getNome());
            statement.setString(2,tarefa.getDescricao());
            statement.setDate(3, Date.valueOf(date));
            statement.setString(4,tarefa.getStatus());
            statement.setString(5,tarefa.getCategoria());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Recupera as chaves geradas
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1); // Obtém o valor do ID gerado
                }

                if (tarefa.getCriterios().size() > 0) {
                    for (Criterios criterio : tarefa.getCriterios()) {
                        Criterios obj = new Criterios(0, criterio.getDescricao(), idGerado, false );

                        criteriosDAO.adicionarCriterio(obj);

                    }
                }
            }

        } catch (Exception e) {
            throw new Exception("Erro ao Inserir a Tarefa.");
        }
    }

    public List<Tarefa> listarTarefas(String pNome, String pData) {

        List<Tarefa> tarefas = new ArrayList<>();

        try {
            String sql = " SELECT * FROM tarefas WHERE 1 = 1 ";

            if (!Objects.isNull(pNome)) {
                sql = sql.concat("AND nome like '%").concat(pNome).concat("%'");
            }

            if (!Objects.isNull(pData)) {

            }

            PreparedStatement statement = conexao.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Tarefa tarefa = new Tarefa(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getString("categoria"),
                        resultSet.getDate("dt_criacao").toString(),
                        resultSet.getString("status")
                        );

                tarefas.add(tarefa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarefas;
    }

    public void removerTarefa(int id) throws Exception {
        try {

            criteriosDAO.removerCriterioPorTarefa(id);

            String sql = " DELETE FROM tarefas WHERE id = ? ";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1,id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erro ao Remover a Tarefa.");
        }
    }

    public Tarefa carregarTarefaPoId(int idTarefa) throws Exception {
        Tarefa tarefa = null;

        try {
            String sql = " SELECT * FROM tarefas WHERE id = ? ";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1,idTarefa);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tarefa = new Tarefa(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getString("categoria"),
                        resultSet.getDate("dt_criacao").toString(),
                        resultSet.getString("status")
                );
            }

            CriteriosDAO criteriosDAO = new CriteriosDAO();
            List<Criterios> criterios = criteriosDAO.carregarCriterios(idTarefa);
            tarefa.setCriterios(criterios);

            return tarefa;

        } catch (Exception e) {
            throw new Exception("Erro ao Consultar a Tarefa.");
        }
    }

    public void atualizarTarefa(Tarefa tarefa) throws Exception {
        try {
            LocalDate date = LocalDate.parse(tarefa.getDataCriacao());

            String sql = " UPDATE tarefas SET nome = ? , descricao = ? ,dt_criacao = ? ,status = ?, categoria = ? WHERE id = ?";

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1,tarefa.getNome());
            statement.setString(2,tarefa.getDescricao());
            statement.setDate(3, Date.valueOf(date));
            statement.setString(4,tarefa.getStatus());
            statement.setString(5,tarefa.getCategoria());
            statement.setInt(6,tarefa.getId());

            statement.executeUpdate();

            if (tarefa.getCriterios().size() > 0) {
                criteriosDAO.removerCriterioPorTarefa(tarefa.getId());

                for (Criterios criterio : tarefa.getCriterios()) {
                    Criterios obj = new Criterios(0, criterio.getDescricao(), tarefa.getId(), criterio.isConcluida() );

                    criteriosDAO.adicionarCriterio(obj);

                }
            }

        } catch (Exception e) {
            throw new Exception("Erro ao ATualizar a Tarefa.");
        }
    }
}

