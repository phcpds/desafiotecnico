package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JPanel pnlPrincipal;
    private JPanel panelMain;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Gestão de Atividades");
        frame.setContentPane(new Main().pnlPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Cadastro");

        JMenuItem tarefas = new JMenuItem("Tarefas");
        JMenuItem relatorio = new JMenuItem("Relatório");
        JMenuItem sair = new JMenuItem("Saír");

        menu.add(tarefas);
        menu.add(relatorio);
        menu.add(sair);

        menuBar.add(menu);

        tarefas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaTarefas tarefas = new ListaTarefas();
                tarefas.setLocationRelativeTo(null);
                tarefas.setVisible(true);
            }
        });

        relatorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RelatorioArvore relatorio = new RelatorioArvore();
                relatorio.setLocationRelativeTo(null);
                relatorio.setVisible(true);
            }
        });

        sair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }

    public Main() {

    }

    private void carregarUI() {
        if (panelMain == null) {
            panelMain = this.pnlPrincipal;
            add(panelMain);
        }
    }
}
