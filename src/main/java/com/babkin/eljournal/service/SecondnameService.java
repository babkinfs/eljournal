package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Secondname;
import com.babkin.eljournal.repo.SecondnameRepos;
import org.springframework.stereotype.Service;

@Service
public class SecondnameService {
    private SecondnameRepos secondnameRepos;

    public SecondnameService(SecondnameRepos secondnameRepos){
        this.secondnameRepos = secondnameRepos;
    }

    public Secondname saveIntoSecondname(String name) {
        Secondname secondnameFromDb = secondnameRepos.findByName( name );
        if (secondnameFromDb == null) {
            secondnameFromDb = secondnameRepos.save( new Secondname( name ) );
        }
        return secondnameFromDb;
    }

    public Secondname findSecondnameByName(String name){

        return  secondnameRepos.findByName( name );
    }

}
