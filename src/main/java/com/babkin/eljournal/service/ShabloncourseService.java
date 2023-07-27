package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Shabloncourse;
import com.babkin.eljournal.repo.ShabloncourseRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShabloncourseService {
    @Autowired
    private ShabloncourseRepos shablonCourseRepos;

    public Shabloncourse save(String name, String typez, Course course){
        if (course == null){
            return null;
        }
        Long id = course.getId();
        Shabloncourse shablonCourse = shablonCourseRepos.findShabloncourseByNameAndTypezAndCourse_Id(name, typez.substring(0, 3), course.getId());
        if (shablonCourse == null){
            shablonCourse = shablonCourseRepos.save(new Shabloncourse(name, typez.substring(0, 3), course));
        }
        return shablonCourse;
    }
}
