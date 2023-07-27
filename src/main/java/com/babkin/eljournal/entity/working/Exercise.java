package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name="exercise")
public class Exercise {
    @Id
    @Column(name = "exercise_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "key", length = 100, nullable = false)
    private String key;

    @Column(name = "body", length = 100, nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "startdata_id")
    private Startdata startdata;

    public Exercise(){}
    public Exercise(String key, String body, Theme theme, Startdata startdata) {
        this.key = key;
        this.body = body;
        this.theme = theme;
        this.startdata = startdata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Startdata getStartdata() {
        return startdata;
    }

    public void setStartdata(Startdata startdata) {
        this.startdata = startdata;
    }
}
