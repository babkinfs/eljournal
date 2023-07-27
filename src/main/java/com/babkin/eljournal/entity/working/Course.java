package com.babkin.eljournal.entity.working;

import com.babkin.eljournal.controllers.UtilsController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

//один преподаватель - много курсов
//одна группа - много курсов
//один курс - много планов
//один курс - много лет
//-----------------------------------------------------один курс - много тем
@Entity
@Table(name="course")
public class Course{
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    // полное наименование
    @Column(name = "name_course_full", length = 100, nullable = false)
    @NotBlank(message = "Поле полного наименования дисциплины не может быть пустым")
    private String nameCourseFull;

    // краткое наименование
    @Column(name = "name_course_short", length = 20, nullable = false)
    @NotBlank(message = "Поле короткого наименования дисциплины не может быть пустым")
    private String nameCourseShort;

    @Column(name = "kolzan")
    private int kolzan;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "groupp_id")
    private Groupp groupp;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public String getTypeZn(String indexString){
        int index = Integer.parseInt(indexString);
        if (index > 1){
            return UtilsController.getTypeZbyIndex( 1 );
        }
        return UtilsController.getTypeZbyIndex( index );
    }


    public Course(String nameFull, String nameShort, int kolzan ,Groupp groupp, Teacher teacher) {
        this.nameCourseFull = nameFull;
        this.nameCourseShort = nameShort;
        this.kolzan = kolzan;
        this.groupp = groupp;
        this.teacher = teacher;
    }

    public Course() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCourseFull() {
        return nameCourseFull;
    }

    public void setNameCourseFull(String nameCourseFull) {
        this.nameCourseFull = nameCourseFull;
    }

    public String getNameCourseShort() {
        return nameCourseShort;
    }

    public void setNameCourseShort(String nameCourseShort) {
        this.nameCourseShort = nameCourseShort;
    }

    public Groupp getGroupp() {
        return groupp;
    }

    public void setGroupp(Groupp groupp) {
        this.groupp = groupp;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getKolzan() {
        return kolzan;
    }

    public void setKolzan(int kolzan) {
        this.kolzan = kolzan;
    }
}