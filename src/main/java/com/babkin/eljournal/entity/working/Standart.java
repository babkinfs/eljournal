package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name = "standart")
public class Standart {
    @Id
    @Column(name = "standart_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "namecourse", length = 255)
    private String namecourse;

    public Standart(String namecourse){
        this.namecourse = namecourse;
    }
    public Standart(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamecourse() {
        return namecourse;
    }

    public void setNamecourse(String namecourse) {
        this.namecourse = namecourse;
    }
}
