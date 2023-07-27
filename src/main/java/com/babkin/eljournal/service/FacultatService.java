package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Facultat;
import com.babkin.eljournal.repo.FacultatRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultatService {

    @Autowired
    private FacultatRepos facultatRepos;


    public Facultat save(String name, String forma) {
        Facultat fromDB = facultatRepos.findByNameAndForma( name, forma );
        if (fromDB == null) {
            fromDB = facultatRepos.save( new Facultat( name, forma ) );
        }
        return fromDB;
    }

    public Facultat findByNameAndForma(String name, String forma){

        return  facultatRepos.findByNameAndForma( name, forma );
    }

    public List<Facultat> findAll(){

        return  facultatRepos.findAll();
    }
}
