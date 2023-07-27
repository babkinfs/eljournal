package com.babkin.eljournal.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Email(message = "В этом поле нужно вводить адрес электронной почты")
    @NotBlank(message = "Поле для ввода адреса электронной почты не может быть пустым")
    private String username;
    @NotBlank(message = "Поле пароля не может быть пустым")
    private String password;

    private boolean active;

    private String activationCode;

    //@Transient
    //@NotBlank(message = "Поле фамилии не может быть пустым")
    //private String lastname;
//
    //@Transient
    //@NotBlank(message = "Поле имени не может быть пустым")
    //private String firstname;
//
    //@Transient
    //@NotBlank(message = "Поле отчества не может быть пустым")
    //private String secondname;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "teacher_id")
    //private Set<Teacher> teachers = new HashSet<>();
//
    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "student_id")
    //private Set<Student> students = new HashSet<>();


    public Long getId() {
        return id;
    }

    public boolean isAdmin(){

        return roles.contains( Role.ADMIN );
    }
    public boolean isTeacher(){

        return roles.contains( Role.TEACHER );
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    public String getLastname() {
//        return lastname;
//    }
//    public void setLastname(String lastname) {
//        this.lastname = lastname;
//    }
//    public String getFirstname() {
//        return firstname;
//    }
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//    public String getSecondname() {
//        return secondname;
//    }
//    public void setSecondname(String secondname) {
//        this.secondname = secondname;
//    }
}
