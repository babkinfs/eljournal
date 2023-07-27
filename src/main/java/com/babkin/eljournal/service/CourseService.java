package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Groupp;
import com.babkin.eljournal.entity.working.Teacher;
import com.babkin.eljournal.repo.CourseRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

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
        if (teacher == null){
            return null;
        }
        return courseRepos.findByTeacher_Id( teacher.getId() );
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
