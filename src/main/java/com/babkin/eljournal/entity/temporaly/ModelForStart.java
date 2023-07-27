package com.babkin.eljournal.entity.temporaly;

import com.babkin.eljournal.entity.working.*;

public class ModelForStart {
    int periodId;
    Year year;
    Semestr semestr;
    Facultat facultat;
    Groupp groupp;
    Course course;
    String result;
    Call call;
    String typeZ;

    public ModelForStart(){}
    public ModelForStart(int periodId, Year year, Semestr semestr, Facultat facultat, Groupp groupp, Course course, Call call, String typeZ) {
        this.periodId = periodId;
        this.year = year;
        this.semestr = semestr;
        this.facultat = facultat;
        this.groupp = groupp;
        this.course = course;
        this.call = call;
        this.typeZ = typeZ;
    }

    public int getPeriodId() {
        return periodId;
    }

    public Year getYear() {
        return year;
    }

    public Semestr getSemestr() {
        return semestr;
    }

    public Facultat getFacultat() {
        return facultat;
    }

    public Groupp getGroupp() {
        return groupp;
    }

    public Course getCourse() {
        return course;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Call getCall() {
        return call;
    }

    public String getTypeZ() {
        return typeZ;
    }
}
