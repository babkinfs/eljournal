package com.babkin.eljournal.entity.working;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Facultat {

    @Id
    @Column(name = "facultat_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // полное наименование
    @Column(name = "name", length = 100)
    @NotBlank(message = "Наименование факультета не может быть пустым")
    private String name;

    // полное наименование
    @Column(name = "forma", length = 30)
    @NotBlank(message = "Наименование формы обучения не может быть пустым")
    private String forma;

    public String getNameForma() {
        return name + " " + forma;
    }


    public Facultat (){}

    public Facultat (String name, String forma){
        this.name = name;
        this.forma = forma;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

}
