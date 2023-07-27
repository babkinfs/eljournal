package com.babkin.eljournal.entity.temporaly;

public class ForLekLab {
    private String template;
    private String typeZan;
    private int number;
    private String nameFile;
    private String theme;
    private String zadanie;

    public ForLekLab(String typeZan, int number, String nameFile, String theme, String zadanie, String template) {
        this.typeZan = typeZan;
        this.number = number;
        this.nameFile = nameFile;
        this.theme = theme;
        this.zadanie = zadanie;
        this.template = template;
    }

    public String getTypeZan() {
        return typeZan;
    }

    public int getNumber() {
        return number;
    }

    public String getNameFile() {
        return nameFile;
    }

    public String getTheme() {
        return theme;
    }

    public String getZadanie() {
        return zadanie;
    }

    public String getTemplate() {
        return template;
    }
}
