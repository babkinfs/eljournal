package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name="subgroupp")
public class Subgroupp {

    @Id
    @Column(name = "subgroupp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // полное наименование
    @Column(name="namesubgroupp", length = 10, nullable = false )
    private String namesubgroupp;


    public Subgroupp(String namesubgroupp){
        this.namesubgroupp = namesubgroupp;
    }
    public Subgroupp(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamesubgroupp() {
        return namesubgroupp;
    }

    public void setNamesubgroupp(String namesubgroupp) {
        this.namesubgroupp = namesubgroupp;
    }
}
