package com.babkin.eljournal.entity.temporaly;


import com.babkin.eljournal.entity.working.Dnevnik;
import com.babkin.eljournal.entity.working.FilesForDnevnik;

public class DnevnikAndFilesForDnevnik {
    private Dnevnik dnevnik;
    private FilesForDnevnik filesForDnevnik;
    public DnevnikAndFilesForDnevnik(Dnevnik dnevnik, FilesForDnevnik filesForDnevnik){
        this.dnevnik = dnevnik;
        this.filesForDnevnik = filesForDnevnik;
    }

    public Dnevnik getDnevnik() {
        return dnevnik;
    }

    public FilesForDnevnik getFilesForDnevnik() {
        return filesForDnevnik;
    }
}
