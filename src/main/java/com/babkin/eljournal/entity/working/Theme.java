package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name="theme")
public class Theme {
    @Id
    @Column(name = "theme_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nameteme", length = 1024, nullable = false)
    private String nameteme;

    @Column(name = "zadanie", length = 1024, nullable = false)
    private String zadanie;//Общее задание на данную пару

    @Column(name = "number")
    private int number;

    @Column(name = "typezan", length = 20, nullable = false)
    private String typezan;

    @Column(name = "fileshablon", length = 256, nullable = false)
    private String fileshablon;

    @Column(name = "fileforstudent", length = 256, nullable = false)
    private String fileforstudent;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "course_id")
    private Course course;


    public Theme(){}
    public Theme(String nameteme, String zadanie, int number, String typezan, String fileshablon, String fileforstudent, Course course) {
        this.nameteme = nameteme;
        this.zadanie = zadanie;
        this.number = number;
        this.typezan = typezan;
        this.fileshablon = fileshablon;
        this.fileforstudent = fileforstudent;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameteme() {
        return nameteme;
    }

    public void setNameteme(String nameteme) {
        this.nameteme = nameteme;
    }

    public String getZadanie() {
        return zadanie;
    }

    public void setZadanie(String zadanie) {
        this.zadanie = zadanie;
    }

    public String getTypezan() {
        return typezan;
    }

    public void setTypezan(String typezan) {
        this.typezan = typezan;
    }

    public String getFileshablon() {
        return fileshablon;
    }

    public void setFileshablon(String fileshablon) {
        this.fileshablon = fileshablon;
    }

    public String getFileforstudent() {
        return fileforstudent;
    }

    public void setFileforstudent(String fileforstudent) {
        this.fileforstudent = fileforstudent;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
