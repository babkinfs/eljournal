package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Year;
import com.babkin.eljournal.repo.YearRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YearService {
    @Autowired
    //private UtilsController utilsController;
    //24.03.2021

    private YearRepos yearRepos;
    public YearService(YearRepos yearRepos){
        this.yearRepos = yearRepos;
    }

    public Year findByFirstnameyearAndSecondnameyear(String firstnameyear, String secondnameyear){

        return  yearRepos.findByFirstnameyearAndSecondnameyear( firstnameyear, secondnameyear );
    }

    public Year save(String firstYearName, String secondYearName) {
        Year yearFromDB = yearRepos.findByFirstnameyearAndSecondnameyear( firstYearName, secondYearName );
        if (yearFromDB == null ) {
            return yearRepos.save( new Year(firstYearName, secondYearName) );
        }
        return yearFromDB;
    }

    public List<Year> findAll(){

        return  yearRepos.findAll();
    }
}
