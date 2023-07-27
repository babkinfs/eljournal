package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name="filesfordnevnik")
public class FilesForDnevnik {
    @Id
    @Column(name = "filesfordnevnik_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "datesdachi", length = 100, nullable = false)
    private String datesdachi;

    //studentsdal, teacherprinyal, studentskachal
    @Column(name = "ktocdal", length = 100, nullable = false)
    private String ktocdal;

    @Column(name = "pathstudent", length = 200, nullable = false)
    private String pathstudent;

    @Column(name = "datecontrol", length = 100, nullable = false)
    private String datecontrol;

    @Column(name = "pathteacher", length = 200, nullable = false)
    private String pathteacher;
    @Column(name = "status", length = 100, nullable = false)
    private String status;
    @Column(name = "ocenka", length = 100, nullable = false)
    private String ocenka;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "dnevnik_id")
    private Dnevnik dnevnik;


    public FilesForDnevnik(String datesdachi, String ktocdal, String pathstudent,
                           String datecontrol, String pathteacher,
                           String status, String ocenka, Dnevnik dnevnik) {
        this.datesdachi = datesdachi;
        this.ktocdal = ktocdal;
        this.pathstudent = pathstudent;
        this.dnevnik = dnevnik;
        this.datecontrol = datecontrol;
        this.pathteacher = pathteacher;
        this.status = status;
        this.ocenka = ocenka;
    }
    public FilesForDnevnik(){}

    public Long getId() { return id; }
    public String getStringId() {
        if (id == null){
            return null;
        }
        return id.toString();
    }

    public void setId(Long id) { this.id = id; }

    public String getDatesdachi() {
        return datesdachi;
    }

    public void setDatesdachi(String datesdachi) {
        this.datesdachi = datesdachi;
    }

    public String getKtocdal() {
        return ktocdal;
    }

    public void setKtocdal(String ktocdal) {
        this.ktocdal = ktocdal;
    }

    public String getPathstudent() {
        return pathstudent;
    }

    public void setPathstudent(String pathstudent) {
        this.pathstudent = pathstudent;
    }

    public String getDatecontrol() {
        return datecontrol;
    }

    public void setDatecontrol(String datecontrol) {
        this.datecontrol = datecontrol;
    }

    public String getPathteacher() {
        return pathteacher;
    }

    public void setPathteacher(String pathteacher) {
        this.pathteacher = pathteacher;
    }

    public String getOcenka() {
        return ocenka;
    }

    public void setOcenka(String ocenka) {
        this.ocenka = ocenka;
    }

    public Dnevnik getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(Dnevnik dnevnik) {
        this.dnevnik = dnevnik;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
