package model;

public class Criterios {

    private int id;
    private String descricao;
    private int idTarefa;
    private boolean concluida;

    public Criterios(int id, String descricao, int idTarefa, boolean concluida) {
        this.id = id;
        this.descricao = descricao;
        this.idTarefa = idTarefa;
        this.concluida = concluida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
