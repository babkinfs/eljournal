package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
public class Shabloncourse {
    @Id
    @Column(name = "shabloncourse_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // полное наименование
    @Column(name = "name", length = 2048)
    private String name;
    @Column(name = "typez", length = 10)
    private String typez;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "course_id")
    private Course course;


    public Shabloncourse(){}
    public Shabloncourse(String name, String typez, Course course) {
        this.name = name;
        this.course = course;
        this.typez = typez;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTypez() {
        return typez;
    }

    public void setTypez(String typez) {
        this.typez = typez;
    }
}