package com.babkin.eljournal.entity.working;

import javax.persistence.*;

@Entity
@Table(name = "startdata")
public class Startdata {

    @Id
    @Column(name = "startdata_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "role", length = 30)
    private String role;

    @Column(name = "email", length = 30)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "firstname_id")
    private Firstname firstname;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "secondname_id")
    private Secondname secondname;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "lastname_id")
    private Lastname lastname;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "groupp_id")
    private Groupp groupp;

    public Startdata() {
    }

    public Startdata(String role, Firstname firstname, Secondname secondname, Lastname lastname, Groupp groupp,
                     String email) {
        this.role = role;
        this.email = email;
        this.firstname = firstname;
        this.secondname = secondname;
        this.lastname = lastname;
        this.groupp = groupp;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Firstname getFirstname() {
        return firstname;
    }

    public void setFirstname(Firstname firstname) {
        this.firstname = firstname;
    }

    public Secondname getSecondname() {
        return secondname;
    }

    public void setSecondname(Secondname secondname) {
        this.secondname = secondname;
    }

    public Lastname getLastname() {
        return lastname;
    }

    public void setLastname(Lastname lastname) {
        this.lastname = lastname;
    }

    public Groupp getGroupp() {
        return groupp;
    }

    public void setGroupp(Groupp groupp) {
        this.groupp = groupp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}