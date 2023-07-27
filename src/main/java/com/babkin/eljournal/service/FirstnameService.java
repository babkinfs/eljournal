package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Firstname;
import com.babkin.eljournal.repo.FirstnameRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirstnameService {
    @Autowired
    private FirstnameRepos firstnameRepos;

    public Firstname saveIntoFirstname(String name) {
        Firstname firstnameFromDB = firstnameRepos.findByName( name );
        if (firstnameFromDB == null) {
            firstnameFromDB = firstnameRepos.save( new Firstname( name ) );
        }
        return firstnameFromDB;
    }

    public Firstname findFirstnameByName(String name){

        return  firstnameRepos.findByName( name );
    }
}
