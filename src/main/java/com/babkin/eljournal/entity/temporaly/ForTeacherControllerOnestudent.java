package com.babkin.eljournal.entity.temporaly;

import java.util.List;

public class ForTeacherControllerOnestudent {
    private List<DnevnikFull> dnevnikFullList;
    private String predmet;
    private Long courseid;
    private String tpZ;
    private String gpg;

    public ForTeacherControllerOnestudent( String predmet, String tpZ, String gpg,
                                           List<DnevnikFull> dnevnikFullList, Long courseid ){
        this.dnevnikFullList = dnevnikFullList;
        this.tpZ = tpZ;
        this.gpg = gpg;
        this.predmet = predmet;
        this.courseid = courseid;
    }

    public List<DnevnikFull> getDnevnikFullList() {
        return dnevnikFullList;
    }

    public void setDnevnikFullList(List<DnevnikFull> dnevnikFullList) {
        this.dnevnikFullList = dnevnikFullList;
    }

    public String getPredmet() {
        return predmet;
    }

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getTpZ() {
        return tpZ;
    }

    public void setTpZ(String tpZ) {
        this.tpZ = tpZ;
    }

    public String getGpg() {
        return gpg;
    }

    public void setGpg(String gpg) {
        this.gpg = gpg;
    }
}
