package com.babkin.eljournal.entity.temporaly;

import com.babkin.eljournal.entity.working.Dnevnik;
import com.babkin.eljournal.entity.working.Student;

public class StudentFull {
    private Student student;
    private boolean present;
    private Dnevnik ochnoZaochno;
    public StudentFull(Student student, boolean present, Dnevnik ochnoZaochno){
        this.student = student;
        this.present = present;
        this.ochnoZaochno = ochnoZaochno;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean  isOchnoZaochno() {
        if (ochnoZaochno == null){
            return false;
        }
        return ochnoZaochno.isOchno();
    }

    public void setOchnoZaochno(Dnevnik  ochnoZaochno) {
        this.ochnoZaochno = ochnoZaochno;
    }
    public Dnevnik getOchnoZaochno(){
        return this.ochnoZaochno;
    }
}
