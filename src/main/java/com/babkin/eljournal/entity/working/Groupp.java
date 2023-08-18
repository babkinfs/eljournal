package com.babkin.eljournal.entity.working;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Groupp {
    //@Autowired
    //private UtilsController utilsController;

    @Id
    @Column(name = "groupp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // полное наименование
    @Column(name="namegroupp", length = 100, nullable = false)
    @NotBlank(message = "Поле полного наименования группы не может быть пустым")
    private String namegroupp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "semestr_id")
    private Semestr semestr;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "year_id")
    private Year year ;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "facultat_id")
    private Facultat facultat;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "subgroupp_id")
    private Subgroupp subgroupp;

    public String getNamegrouppOunly() {
        return this.namegroupp;
    }

    public Groupp(){}
    public Groupp(String name, Facultat facultat, Semestr semestr, Year year, Subgroupp subgroupp) {
        this.namegroupp = name;
        this.semestr = semestr;
        this.year = year;
        this.facultat = facultat;
        this.subgroupp = subgroupp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Facultat getFacultat() {
        return facultat;
    }

    public void setFacultat(Facultat facultat) {
        this.facultat = facultat;
    }

    public Semestr getSemestr() {
        return semestr;
    }

    public void setSemestr(Semestr semestr) {
        this.semestr = semestr;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getNamegroupp() {
        if (subgroupp.getNamesubgroupp().equals("0")) {
            return namegroupp;
        } else {
            return namegroupp + "." + subgroupp.getNamesubgroupp();
        }
    }
    public String getOnlynamegroupp(){
        return namegroupp;
    }

    public void setNamegroupp(String namegroupp) {
        this.namegroupp = namegroupp;
    }

    public Subgroupp getSubgroupp() {
        return subgroupp;
    }

    public void setSubgroupp(Subgroupp subgroupp) {
        this.subgroupp = subgroupp;
    }
}
