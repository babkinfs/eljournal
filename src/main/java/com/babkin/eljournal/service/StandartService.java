package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Standart;
import com.babkin.eljournal.repo.StandartRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandartService {
    @Autowired
    private StandartRepos standartRepos;

    public Standart save(String nameCourse) {
        if (nameCourse == null) {
            return null;
        }
        Standart fromDB = standartRepos.findStandartByNamecourse( nameCourse );
        if (fromDB == null) {
            return standartRepos.save( new Standart(nameCourse) );
        }
        return fromDB;
    }
    public Standart findStandartByNameCourse(String nameourse){
        return standartRepos.findStandartByNamecourse( nameourse );
    }
}
