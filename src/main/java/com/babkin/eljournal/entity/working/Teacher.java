package com.babkin.eljournal.entity.working;

import com.babkin.eljournal.entity.User;

import javax.persistence.*;

//один преподаватель - много курсов
@Entity
@Table(name="teacher")
public class Teacher {

    @Id
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="role", length = 10, nullable = false )
    private String role;
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


    public String getFullFio() {
        return lastname.getName()+" "+firstname.getName()+" "+secondname.getName();
    }



    public Teacher() {
    }
    public Teacher(Firstname firstname, Secondname secondname, Lastname lastname, User user, String role) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.lastname = lastname;
        this.user = user;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}