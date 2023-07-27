package com.babkin.eljournal.entity.temporaly;

import com.babkin.eljournal.entity.working.*;

import java.util.List;

public class ForTeacherController {
    private Teacher teacher;
    private Facultat facultat;
    private Year year;
    private Semestr semestr;
    private Groupp groupp;
    private Course course;
    private Call call;
    private int counter;
    private List<String> dates;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Facultat getFacultat() {
        return facultat;
    }

    public void setFacultat(Facultat facultat) {
        this.facultat = facultat;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Semestr getSemestr() {
        return semestr;
    }

    public void setSemestr(Semestr semestr) {
        this.semestr = semestr;
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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
