package com.babkin.eljournal.entity.working;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Semestr {

    @Id
    @Column(name = "semestr_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // полное наименование
    @Column(name = "name", length = 30)
    @NotBlank(message = "Номер семестра не может быть пустым")
    private String name;



    public Semestr(){}

    public Semestr(String name){
        this.name = name;
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

}
