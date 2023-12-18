package com.babkin.eljournal.entity.working;

import javax.persistence.*;

//один дневник - много расписаний
//один дневник - один план
@Entity
@Table(name="dnevnik")
public class Dnevnik {

    @Id
    @Column(name = "dnevnik_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    //присутствует
    @Column(name = "ispresent")
    private boolean ispresent;
    @Column(name = "ochno")
    private boolean ochno;
    @Transient
    @Column(name = "visiblebuttons")
    private boolean visiblebuttons;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "raspisanie_id")
    private Raspisanie raspisanie;

    public String getPropuski(){
        if (ispresent){
            if (ochno) {
                return "посещено очно";
            }else {
                return "посещено заочно";
            }
        } else {
            return "пропущено";
        }
    }

    public Dnevnik() {
    }

    public Dnevnik( boolean ispresent,
                    Student student, Raspisanie raspisanie) {
        this.student = student;
        this.raspisanie = raspisanie;
        this.ispresent = ispresent;
    }

    //public Dnevnik(Dnevnik dnevnik){
    //    this.plan = dnevnik.getPlan();
    //}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Raspisanie getRaspisanie() {
        return raspisanie;
    }

    public void setRaspisanie(Raspisanie raspisanie) {
        this.raspisanie = raspisanie;
    }

    public boolean isIspresent() {
        return ispresent;
    }

    public void setIspresent(boolean ispresent) {
        this.ispresent = ispresent;
    }

    public boolean isOchno() {
        return ochno;
    }

    public void setOchno(boolean ochno) {
        this.ochno = ochno;
    }

    public boolean isVisiblebuttons() {
        String fileForStudent = raspisanie.getTheme().getFileforstudent().substring(0,1);
        if (!fileForStudent.equals("п") && !fileForStudent.equals("д")){
            return true;
        }
        return false;
    }

    public void setVisiblebuttons(boolean visiblebuttons) {
        this.visiblebuttons = visiblebuttons;
    }
}