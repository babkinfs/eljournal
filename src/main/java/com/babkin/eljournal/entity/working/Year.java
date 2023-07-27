package com.babkin.eljournal.entity.working;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

//один год - много групп
// один год - много семестров

@Entity
public class Year {

    @Id
    @Column(name = "year_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstnameyear", length = 30)
    @NotBlank(message = "Наименование года не может быть пустым")
    private String firstnameyear;

    @Column(name = "secondnameyear", length = 30)
    @NotBlank(message = "Наименование года не может быть пустым")
    private String secondnameyear;



    public Year(String firstnameyear, String secondnameyear){

        this.firstnameyear = firstnameyear;
        this.secondnameyear = secondnameyear;
    }

    public Year() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNameyear() {
        return firstnameyear + " - " + secondnameyear;
    }

    public String getFirstnameyear() {
        return firstnameyear;
    }

    public void setFirstnameyear(String firstnameyear) {
        this.firstnameyear = firstnameyear;
    }

    public String getSecondnameyear() {
        return secondnameyear;
    }

    public void setSecondnameyear(String secondnameyear) {
        this.secondnameyear = secondnameyear;
    }
}
