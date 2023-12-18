package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Groupp;
import com.babkin.eljournal.entity.working.Teacher;
import com.babkin.eljournal.repo.CourseRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private GroupService grouppService;

    private final CourseRepos courseRepos;

    public CourseService(CourseRepos courseRepos){
        this.courseRepos = courseRepos;
    }

    public Course save(Course course) {
        if ((course != null) && (course.getTeacher() != null)) {
            Course course1 = courseRepos.findByNameCourseFullAndGroupp_IdAndTeacher_Id(course.getNameCourseFull(),
                    course.getGroupp().getId(), course.getTeacher().getId() );
            if (course1 == null) {
                return courseRepos.save( course );
            }
            return course1;
        }
        return null;
    }
    public void updateKolTem(Course course){
        Course fromDb = findCourseById(course.getId());
        fromDb.setKoltem(course.getKoltem());
        courseRepos.saveAndFlush(fromDb);
    }
    public List<Course> findByGrouppAndTeacher(Groupp group, Teacher teacher){
        if ((group == null) || (teacher == null)){
            return courseRepos.findAll();
        } else if (group == null){
            return courseRepos.findByTeacher_Id( teacher.getId() );
        } else if (teacher == null) {
            return courseRepos.findByGroupp_Id( group.getId() );
        } else
            return courseRepos.findAllByGrouppIdAndTeacher_Id( group.getId(), teacher.getId() );
    }

    public List<Course> findByTeacher(Teacher teacher){
        //Выбрать все курсы как преподавателя, так и руковадителя дисциплины
        if (teacher == null){
            return null;
        }
        List<Course> temp = courseRepos.findByTeacher_Id( teacher.getId() );
        List<Course> temporaly = new ArrayList<>();
        if (teacher.getRole().equals("LECTOR")) {
            for (Course c : temp) {
                //Выбираю совокупность группы и подгрупп, отсутствующих у преподавателя и по ним нахожу курсы
                List<Groupp> listGroupp = grouppService.findGrouppByNameGroupp(
                        c.getGroupp().getNamegroupp(), c.getGroupp().getSemestr());
                for (Groupp gr : listGroupp) {
                    List<Course> fff = courseRepos.findAllByTeacher_IdAndGroupp_Subgroupp_IdAndNameCourseFull(
                            teacher.getId(), c.getGroupp().getSubgroupp().getId(), c.getNameCourseFull());
                    //teacher, c.getNameCourseFull(), c.getGroupp().getId());
                    if (fff.size() > 0) {
                        temporaly.addAll(fff);
                    }
                }
            }
            temp.addAll(temporaly);
        }
        return temp;
    }

    public List<Course> findByGroupp(Groupp groupp){
        if (groupp == null){
            return null;
        }
        return courseRepos.findByGroupp_Id( groupp.getId() );
    }
    public Course findCourseById(Long id){
        return courseRepos.findCourseById( id );
    }

    public List<Course> findAll(){
        return courseRepos.findAll();
    }
}
