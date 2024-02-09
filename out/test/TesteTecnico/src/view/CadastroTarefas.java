package view;

import controller.CriteriosDAO;
import controller.TarefaDAO;
import model.Criterios;
import model.Tarefa;
import util.ComboboxItem;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class CadastroTarefas extends JFrame {

    private JPanel pnlPrincipal;
    private JTextField txtNome;
    private JTextField txtDescricao;
    private JFormattedTextField txtData;
    private JComboBox cbxCategoria;
    private JComboBox cbxStatus;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnRemoverCriterio;
    private JButton btnAddCriterio;
    private JTextArea txtDescricaoCriterios;
    private JTable tabelaCriterios;
    private JPanel panelTelaCadastro;
    private TarefaDAO dao = new TarefaDAO();
    private List<Criterios> criterios;
    private DefaultTableModel modeloTabela;

    public CadastroTarefas(int idTarefa) {

        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Carregua a interface gráfica
        carregarUI();

        // Define as colunas da tabela
        modeloTabela = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        modeloTabela.addColumn("");
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Descrição");

        tabelaCriterios.setModel(modeloTabela);
        tabelaCriterios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabelaCriterios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaCriterios.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabelaCriterios.getColumnModel().getColumn(2).setPreferredWidth(300);

        tabelaCriterios.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int rowIndex = tabelaCriterios.getSelectedRow();
                if (rowIndex > -1) {
                    boolean concluida = (boolean) tabelaCriterios.getValueAt(rowIndex, 0);
                    Object idCriterio = tabelaCriterios.getValueAt(rowIndex, 1);
                    Object dsCriterio = tabelaCriterios.getValueAt(rowIndex, 2);

                    for (Criterios criterio : criterios) {
                        if ((int) idCriterio > 0)  {
                            if (criterio.getId() == (int) idCriterio) {
                                criterio.setConcluida(concluida);
                            }
                        } else {
                            if (criterio.getDescricao().equals(dsCriterio)) {
                                criterio.setConcluida(concluida);
                            }
                        }
                    }

                    if (!concluida) {
                        cbxStatus.setSelectedIndex(1);
                    }
                }
            }
        });

        // Adiciona itens ao JComboBox
        cbxStatus.addItem(new ComboboxItem("N", "Novo"));
        cbxStatus.addItem(new ComboboxItem("A", "Em Andamento"));
        cbxStatus.addItem(new ComboboxItem("C", "Concluído"));

        cbxCategoria.addItem(new ComboboxItem("S", "Solicitação"));
        cbxCategoria.addItem(new ComboboxItem("I", "Incidente"));
        cbxCategoria.addItem(new ComboboxItem("C", "Compra"));

        //Limita o tamanho de caracteres do campo de criterios
        txtDescricaoCriterios.setDocument(new LimitadorDocumento(100));

        // Cria um MaskFormatter para formatar a entrada da data
        MaskFormatter maskFormatter = null;
        try {
            maskFormatter = new MaskFormatter("####-##-##"); // Define o formato da máscara
            maskFormatter.setPlaceholderCharacter('_'); // Caractere a ser exibido para espaços em branco
            maskFormatter.setAllowsInvalid(false); // Permite apenas entradas válidas
            txtData.setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (idTarefa > 0) {
            try {
                Tarefa tarefa = dao.carregarTarefaPoId(idTarefa);

                txtNome.setText(tarefa.getNome());
                txtDescricao.setText(tarefa.getDescricao());
                txtData.setText(tarefa.getDataCriacao());
                cbxStatus.getModel().setSelectedItem(new ComboboxItem(tarefa.getStatus(),
                        (tarefa.getStatus().equals("N")) ? "Novo" : (tarefa.getStatus().equals("A")) ? "Em Andamento" : "Concluído"));
                cbxCategoria.getModel().setSelectedItem(new ComboboxItem(tarefa.getCategoria(),
                        (tarefa.getCategoria().equals("S")) ? "Solicitação" : (tarefa.getCategoria().equals("I")) ? "Incidente" : "Compra"));
                criterios = tarefa.getCriterios();

                carregarCriterioGrid(modeloTabela, criterios);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            criterios = new ArrayList<>();
        }

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ComboboxItem categoria = (ComboboxItem) cbxCategoria.getSelectedItem();
                    ComboboxItem status = (ComboboxItem) cbxStatus.getSelectedItem();

                    String statusTarefa = validaConclusaoTarefa() ? "C" : status.getCode();

                    Tarefa tarefa = new Tarefa(idTarefa, txtNome.getText(),txtDescricao.getText(),
                            categoria.getCode(), txtData.getText(), statusTarefa);
                    tarefa.setCriterios(criterios);

                    if (idTarefa == 0) {
                        dao.adicionarTarefa(tarefa);
                    } else {
                        dao.atualizarTarefa(tarefa);
                    }

                    ListaTarefas lista = new ListaTarefas();
                    lista.carregarTarefas(lista.getModeloTabela());

                    JOptionPane.showMessageDialog(null,"Tarefa Cadastrada com sucesso.",
                            "Sucesso",JOptionPane.OK_OPTION);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Erro ao cadastrar a tarefa.",
                            "Erro",JOptionPane.OK_OPTION);
                }
                dispose();
            }
        });

        btnAddCriterio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarCriterioGrid(modeloTabela, txtDescricaoCriterios.getText(), idTarefa);
            }
        });

        btnRemoverCriterio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = tabelaCriterios.getSelectedRow();

                if (rowIndex != -1) {

                    int opcao = JOptionPane.showConfirmDialog(null,
                            "Deseja excluir o item selecionado?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

                    // Se o usuário confirmar a exclusão
                    if (opcao == JOptionPane.YES_OPTION) {
                        Object idCriterio = tabelaCriterios.getValueAt(rowIndex, 1);
                        Object dsCriterio = tabelaCriterios.getValueAt(rowIndex, 2);

                        if ((int) idCriterio > 0) {
                            try {
                                CriteriosDAO dao = new CriteriosDAO();

                                dao.removerCriterio((int) idCriterio);
                                modeloTabela.removeRow(rowIndex);

                                for (Criterios criterio : criterios){
                                    if (criterio.getId() == (int) idCriterio){
                                        criterios.remove(criterio);
                                    }
                                }

                            } catch (Exception ex) {
                                JOptionPane.showConfirmDialog(null,"Erro ao excluir a tarefa.",
                                        "Erro",JOptionPane.OK_OPTION);
                            }
                        } else {
                            modeloTabela.removeRow(rowIndex);

                            for (Criterios criterio : criterios){
                                if (criterio.getDescricao().equals(dsCriterio)){
                                    criterios.remove(criterio);
                                }
                            }
                        }

                    }

                } else {
                    JOptionPane.showConfirmDialog(null,"Selecione uma linha para Excluir.",
                            "Erro", JOptionPane.OK_OPTION);
                }
            }
        });
    }
    private void carregarUI() {
        if (panelTelaCadastro == null) {
            panelTelaCadastro = this.pnlPrincipal;
            add(panelTelaCadastro);
        }
    }

    class LimitadorDocumento extends PlainDocument {
        private int maxCaracteres;

        LimitadorDocumento(int maxCaracteres) {
            this.maxCaracteres = maxCaracteres;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= maxCaracteres) {
                super.insertString(offset, str, attr);
            }
        }
    }

    public void carregarCriteriosGrid(DefaultTableModel modeloTabela, List<Criterios> criterios) {
        modeloTabela.setRowCount(0);

        for (Criterios criterio : criterios ) {
            Object[] linha = {
                    criterio.getId(),
                    criterio.getDescricao(),
                    criterio.isConcluida()
            };

            modeloTabela.addRow(linha);
        }

    }

    public List<Criterios> carregarCriterios(int idTarefa) {
        CriteriosDAO dao = new CriteriosDAO();

        modeloTabela.setRowCount(0);
        List<Criterios> criterios =  dao.carregarCriterios(idTarefa);

        return criterios;

    }

    public void adicionarCriterioGrid(DefaultTableModel modeloTabela, String descricao, int idTarefa) {
        Object[] linha = {
                false,
                0,
                descricao
        };
        modeloTabela.addRow(linha);

        Criterios criterio = new Criterios(0,descricao,idTarefa, false);
        criterios.add(criterio);
    }

    public void carregarCriterioGrid(DefaultTableModel modeloTabela, List<Criterios> criterios) {
        modeloTabela.setRowCount(0);

        for (Criterios criterio : criterios ) {
            Object[] linha = {
                    criterio.isConcluida(),
                    criterio.getId(),
                    criterio.getDescricao()
            };
            modeloTabela.addRow(linha);
        }


    }

    private boolean validaConclusaoTarefa() {
        boolean concluido = true;

        for (Criterios criterio : criterios){
            if (!criterio.isConcluida()) {
                concluido = false;
            }
        }

        return concluido;
    }
}
