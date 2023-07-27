package com.babkin.eljournal.entity.working;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "secondname")//отчество
public class Secondname {

    @Id
    @Column(name="secondname_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name="name", length = 100, nullable = false)
    @NotBlank(message = "Поле отчество не может быть пустым")
    private String name;

    public String getIni(){ return name.substring( 0, 1 ) + "."; }


    //@OneToMany(mappedBy = "secondname", cascade = CascadeType.ALL)
    ////@JoinColumn(name = "teacher_id")
    //private List<Teacher> teachers = new ArrayList<>();
//
    //@OneToMany(mappedBy = "secondname", cascade = CascadeType.ALL)
    ////@JoinColumn(name = "startdata_id")
    //private List<Startdata> startdata = new ArrayList<>();
//
    //@OneToMany(mappedBy = "secondname", cascade = CascadeType.ALL)
    ////@JoinColumn(name = "student_id")
    //private List<Student> students = new ArrayList<>();


    public Secondname(){}
    public Secondname(String name){
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
