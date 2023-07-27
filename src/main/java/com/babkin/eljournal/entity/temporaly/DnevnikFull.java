package com.babkin.eljournal.entity.temporaly;


import com.babkin.eljournal.entity.working.Dnevnik;

public class DnevnikFull {
    private String prefx;
    private String theme;
    private String zadanie;
    private Dnevnik dnevnik;

    public DnevnikFull( Dnevnik dnevnik, String prefx, String theme, String zadanie ){
        this.dnevnik = dnevnik;
        this.prefx = prefx;
        this.theme = theme;
        this.zadanie = zadanie;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) { this.theme = theme; }

    public String getZadanie() {
        return zadanie;
    }

    public void setZadanie(String zadanie) { this.zadanie = zadanie; }

    public Dnevnik getDnevnik() { return dnevnik; }

    public String getPrefx() {
        return prefx;
    }
}
