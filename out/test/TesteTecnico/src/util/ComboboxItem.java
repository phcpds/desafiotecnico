package util;

public class ComboboxItem {
    private String code;
    private String descricao;

    public ComboboxItem(String code, String descricao) {
        this.code = code;
        this.descricao = descricao;
    }

    public String getCode() {

        return code;
    }

    public String getDescricao() {

        return descricao;
    }

    @Override
    public String toString() {

        return descricao;
    }
}
