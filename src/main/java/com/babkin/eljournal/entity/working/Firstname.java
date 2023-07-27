package com.babkin.eljournal.entity.working;

/*
Firstname = 1L;
Secondname = 2L;
Lastname = 3L;
Facultat = 4L;
Year = 5L;
Semestr = 6L;
Teacher = 7L;
Groupp = 8L;
Student = 9L;
Course = 10L;
Call = 11L;
Plan = 12L;
Dnevnik = 13L;
 */

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "firstname")//имя
public class Firstname {

    @Id
    @Column(name = "firstname_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "name", length = 100, nullable = false)
    @NotBlank(message = "Поле имя не может быть пустым")
    private String name;

    public String getIni(){ return name.substring( 0, 1 ) + "."; }


    public Firstname(){}

    public Firstname(String name){
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
