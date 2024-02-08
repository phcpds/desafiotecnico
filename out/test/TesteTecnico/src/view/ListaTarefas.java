package view;

import controller.TarefaDAO;
import model.Tarefa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ListaTarefas extends JDialog {

    private JPanel pnlPrincipal;
    private JTable tableTarefas;
    private JButton btnSair;
    private JButton btnIncluir;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JTextField txtNome;
    private JFormattedTextField txtData;
    private JButton pesquisarButton;
    private DefaultTableModel modeloTabela = new DefaultTableModel();

    public DefaultTableModel getModeloTabela() {
        return modeloTabela;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ListaTarefas");
        frame.setContentPane(new ListaTarefas().pnlPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo();
        frame.pack();
        frame.setVisible(true);

    }

    public ListaTarefas() {

        setSize(600, 500);
        // Define as colunas da tabela
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Descrição");
        modeloTabela.addColumn("Status");

        tableTarefas.setModel(modeloTabela);

        tableTarefas.setAutoCreateRowSorter(true);
        tableTarefas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableTarefas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableTarefas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableTarefas.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableTarefas.getColumnModel().getColumn(3).setPreferredWidth(100);

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTarefas(modeloTabela);
            }
        });
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela
                System.exit(0); // Encerra a aplicação
            }
        });
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = tableTarefas.getSelectedRow();

                if (rowIndex != -1) {

                    Object idTarefa = tableTarefas.getValueAt(rowIndex, 0);
                    Object descricaotarefa = tableTarefas.getValueAt(rowIndex, 2);
                    Object status = tableTarefas.getValueAt(rowIndex, 3);

                    if (status.equals("Concluído")) {
                        JOptionPane.showMessageDialog(null,"Tarefa Concluída não pode ser excluída.",
                                "Erro",JOptionPane.OK_OPTION);
                    } else {
                        int opcao = JOptionPane.showConfirmDialog(null,
                                "Deseja excluir o item selecionado?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

                        // Se o usuário confirmar a exclusão
                        if (opcao == JOptionPane.YES_OPTION) {

                            try {
                                TarefaDAO dao = new TarefaDAO();

                                dao.removerTarefa((int) idTarefa);
                                carregarTarefas(modeloTabela);
                            } catch (Exception ex) {
                                JOptionPane.showConfirmDialog(null,"Erro ao excluir a tarefa.",
                                        "Erro",JOptionPane.OK_OPTION);
                            }

                        }
                    }
                } else {
                    JOptionPane.showConfirmDialog(null,"Selecione uma linha para Excluir.",
                            "Erro", JOptionPane.OK_OPTION);
                }
            }
        });
        btnIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroTarefas cadastro = new CadastroTarefas(0);
                cadastro.setVisible(true);

                carregarTarefas(modeloTabela);
            }
        });
        btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = tableTarefas.getSelectedRow();

                if (rowIndex != -1) {
                    Object idTarefa = tableTarefas.getValueAt(rowIndex, 0);
                    CadastroTarefas cadastro = new CadastroTarefas((int) idTarefa);
                    cadastro.setVisible(true);

                    carregarTarefas(modeloTabela);
                } else {
                    JOptionPane.showConfirmDialog(null,"Selecione uma linha para Alterar.",
                            "Erro", JOptionPane.OK_OPTION);
                }

            }
        });
    }

    public void carregarTarefas(DefaultTableModel modeloTabela) {
        TarefaDAO dao = new TarefaDAO();

        modeloTabela.setRowCount(0);
        List<Tarefa> tarefas =  dao.listarTarefas(txtNome.getText(),txtData.getText());

        for (Tarefa tarefa : tarefas ) {
            Object[] linha = {
                    tarefa.getId(),
                    tarefa.getNome(),
                    tarefa.getDescricao(),
                    (tarefa.getStatus().equals("N")) ? "Novo" : (tarefa.getStatus().equals("A")) ? "Em Andamento" : "Concluído"
            };

            modeloTabela.addRow(linha);
        }

    }

}
