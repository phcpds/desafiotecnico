package model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tarefa {

    private int id;
    private String nome;
    private String descricao;
    private String categoria;
    private String dataCriacao;

    private String status;

    private List<Criterios> criterios;

    // Construtor, getters e setters

    public Tarefa(int id, String nome, String descricao, String categoria, String dataCriacao, String status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dataCriacao = dataCriacao;
        this.status = status;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getDescricao() {

        return descricao;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;
    }

    public String getCategoria() {

        return categoria;
    }

    public void setCategoria(String categoria) {

        this.categoria = categoria;
    }

    public String getDataCriacao() {

        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {

        this.dataCriacao = dataCriacao;
    }

    public List<Criterios> getCriterios() {

        return criterios;
    }

    public void setCriterios(List<Criterios> criterios) {

        this.criterios = criterios;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }
}
