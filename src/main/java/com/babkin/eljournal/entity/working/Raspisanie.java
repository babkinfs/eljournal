package com.babkin.eljournal.entity.working;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="raspisanie")
public class Raspisanie {//implements Comparable {

    @Id
    @Column(name = "raspisanie_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "actiondate", length = 100, nullable = false)
    private String actiondate;//Дата

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "template", length = 256)
    private String template;


    @OneToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "call_id")
    private Call call;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "course_id")
    private Course course;


    public static int compare(Raspisanie a, Raspisanie b) throws ParseException {
        int cmpDate = compareDate(a.getActiondate(), b.getActiondate());
        if (cmpDate == 0){
            return Call.compareCall(a.getCall(), b.getCall());
        }
        return cmpDate;
    }

    public static int compareDate(String first, String last) throws ParseException {//res = 0 if == ; >0 if first>last
        Date firstDate = parseDate( first, "dd-MM-yyyy");
        Date lastDate = parseDate( last, "dd-MM-yyyy");
        if (first.equals( last)){
            return 0;
        }
        if (firstDate.getTime() > lastDate.getTime()){
            return 1;
        } else {
            return -1;
        }
    }
    private static Date parseDate(String date, String format) throws ParseException  {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

    public Raspisanie(){}
    public Raspisanie(String actiondate, int number, String template, Call call, Theme theme, Course course) {
        this.actiondate = actiondate;
        this.number = number;
        this.template = template;
        this.call = call;
        this.theme = theme;
        this.course = course;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActiondate() {
        return actiondate;
    }

    public void setActiondate(String actiondate) {
        this.actiondate = actiondate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
