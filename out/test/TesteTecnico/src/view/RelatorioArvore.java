package view;

import controller.TarefaDAO;
import model.Criterios;
import model.Tarefa;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class RelatorioArvore extends JFrame {
    private JPanel pnlPrincipal;
    private JPanel panelRelatorio;
    private JTree treeTarefas;
    private JButton btnFechar;

    private TarefaDAO dao = new TarefaDAO();

    public RelatorioArvore() {

        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        carregarUI();

        try {
            List<Tarefa> tarefas = dao.carregarRelatorioTarefas();
            String rootNodeDescription = null;
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Listagem");
            DefaultMutableTreeNode nodeCategoria = null;
            DefaultMutableTreeNode nodeTarefa = null;

            for (Tarefa tarefa : tarefas) {

              if (Objects.isNull(nodeCategoria)) {
                  nodeCategoria = new DefaultMutableTreeNode(tarefa.getCategoria().equals("I") ? "Incidente" :
                          tarefa.getCategoria().equals("C") ? "Compra" : "Solicitação");
                  rootNodeDescription = tarefa.getCategoria();
                  nodeTarefa = new DefaultMutableTreeNode(tarefa.getDescricao());

                  for (Criterios criterio : tarefa.getCriterios()){
                      nodeTarefa.add(new DefaultMutableTreeNode(criterio.getDescricao()));
                  }

                  nodeCategoria.add(nodeTarefa);

              } else {
                  if (tarefa.getCategoria().equals(rootNodeDescription)) {
                      rootNodeDescription = tarefa.getCategoria();
                      nodeTarefa = new DefaultMutableTreeNode(tarefa.getDescricao());
                      for (Criterios criterio : tarefa.getCriterios()){
                          nodeTarefa.add(new DefaultMutableTreeNode(criterio.getDescricao()));
                      }
                      nodeCategoria.add(nodeTarefa);

                  } else {
                      rootNode.add(nodeCategoria);
                      nodeCategoria = new DefaultMutableTreeNode(tarefa.getCategoria().equals("I") ? "Incidente" :
                              tarefa.getCategoria().equals("C") ? "Compra" : "Solicitação");
                      rootNodeDescription = tarefa.getCategoria();
                      nodeTarefa = new DefaultMutableTreeNode(tarefa.getDescricao());
                      for (Criterios criterio : tarefa.getCriterios()){
                          nodeTarefa.add(new DefaultMutableTreeNode(criterio.getDescricao()));
                      }
                      nodeCategoria.add(nodeTarefa);
                  }
              }

            }
            rootNode.add(nodeCategoria);

            DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
            treeTarefas.setModel(treeModel);

            expandAllNodes(treeTarefas);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void carregarUI() {
        if (panelRelatorio == null) {
            panelRelatorio = this.pnlPrincipal;
            add(panelRelatorio);
        }
    }

    private static void expandAllNodes(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }
}
