package com.babkin.eljournal.entity.working;

import com.babkin.eljournal.controllers.UtilsController;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//один курс - много планов
//один дневник - один план
//------------------------------------------------------один студент - много планов удалил
//одино расписание - много планов
@Entity
@Table(name="plan")
public class Plan {

    //@Transient
    //private List<String> StringTypeZ = new ArrayList<String>();

    @Id
    @Column(name = "plan_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "action", length = 100, nullable = false)
    private String action;//Дата

    @Column(name = "typez", nullable = false)
    private int typez;

    @Column(name = "numberoflab", nullable = false)
    private int numberoflab;

    @Column(name = "gpg", nullable = false)
    private int gpg;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "groupp_id")
    private Groupp groupp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "call_id")
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public String getTypezString() {
        return UtilsController.getTypeZbyIndex( typez, numberoflab );
    }

    public String getGrpString() {
        return UtilsController.getGrpbyIndex( gpg );
    }


    public Plan() {
    }

    public Plan(String action, Course course, Call call,
                Teacher teacher,
                Groupp groupp,
                int typez, int gpg){//, int orderCourse) {
        this.action = action;
        this.course = course;
        this.call = call;
        this.teacher = teacher;
        this.groupp = groupp;
        this.typez = typez;
        this.gpg = gpg;
        //this.theme = theme;
        //this.orderCourse = orderCourse;
        //StringTypeZ.add( "лабораторная" );
        //StringTypeZ.add( "лекция" );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Groupp getGroupp() {
        return groupp;
    }

    public void setGroupp(Groupp groupp) {
        this.groupp = groupp;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public int getTypez() {
        return typez ;
    }

    public void setTypez(int typez) {
        this.typez = typez;
    }

    public int getNumberoflab() {
        return numberoflab;
    }

    public void setNumberoflab(int numberoflab) {
        this.numberoflab = numberoflab;
    }

    public int getGpg() {
        return gpg;
    }

    public void setGpg(int gpg) {
        this.gpg = gpg;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
