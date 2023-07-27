package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Lastname;
import com.babkin.eljournal.repo.LastnameRepos;
import org.springframework.stereotype.Service;

@Service
public class LastnameService {
    private LastnameRepos lastnameRepos;

    public LastnameService(LastnameRepos lastnameRepos){
        this.lastnameRepos = lastnameRepos;
    }

    public Lastname saveIntoLastname(String name) {
        Lastname lastnameFromDB = lastnameRepos.findByName( name );
        if (lastnameFromDB == null) {
            lastnameFromDB = lastnameRepos.save( new Lastname( name ) );
        }
        return lastnameFromDB;
    }
    public Lastname findLastnameByFamily(String family){
        return lastnameRepos.findByName( family );
    }

}
