package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Semestr;
import com.babkin.eljournal.repo.SemestrRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemestrService {
    //24.03.2021

    private SemestrRepos semestrRepos;

    public SemestrService(SemestrRepos semestrRepos){
        this.semestrRepos = semestrRepos;
    }

    public Semestr findByName(String namesemestr){
        return semestrRepos.findSemestrByName( namesemestr );
    }

    public Semestr save(String semestrName){
        Semestr semestrFromDB = semestrRepos.findSemestrByName( semestrName );
        if ( semestrFromDB == null ){
            return semestrRepos.save( new Semestr(semestrName) );
        } else
            return semestrFromDB;
    }

    public List<Semestr> findAll(){
        return semestrRepos.findAll();
    }
}
