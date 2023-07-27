package com.babkin.eljournal.entity.working;

import com.babkin.eljournal.entity.User;

import javax.persistence.*;

//одна группа - много студентов
//один студент - много планов удалил
@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "groupp_id")
    private Groupp groupp;




    public String getName(){
        return getLastname().getName()
                + " " + getFirstname().getName().substring( 0, 1 )
                + ". " + getSecondname().getName().substring( 0, 1 ) + ".";
    }
    public String getShortName(){
        return getLastname().getName()
                + " " + getFirstname().getName();
    }

    public String getFullFio() {
        return lastname.getName()+" "+firstname.getName()+" "+secondname.getName();
    }

    public String getFN() {
        return lastname.getName()+" "+firstname.getName();
    }

    public Student() {
    }

    public Student(Firstname firstname, Secondname secondname, Lastname lastname, User user,
                   Groupp groupp) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.lastname = lastname;
        this.user = user;
        this.groupp = groupp;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Groupp getGroupp() {
        return groupp;
    }

    public void setGroupp(Groupp groupp) {
        this.groupp = groupp;
    }

}
