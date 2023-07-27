package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.StartdataRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartdataService {
    @Autowired
    private StartdataRepos startdataRepos;
    //@Autowired
    //private TeacherRepos teacherRepos;

    public Startdata save(Startdata startdata) {
        if (startdata != null) {
            if ((startdata.getFirstname() != null) && (startdata.getSecondname() != null)
                    && (startdata.getLastname() != null)) {
                String fname = startdata.getFirstname().getName();
                String sname = startdata.getSecondname().getName();
                String lname = startdata.getLastname().getName();
                Startdata startdataFromDB = startdataRepos.findByFirstname_NameAndSecondname_NameAndLastname_Name( fname, sname, lname );
                if (startdataFromDB == null) {
                    return startdataRepos.save( startdata );
                }
                return startdataFromDB;
            }
        }
        return startdata;
    }

    public Startdata findByFirstname_NameAndSecondname_NameAndLastname_Family(Firstname firstname, Secondname secondname, Lastname lastname){
        if ((firstname != null) && (secondname != null) && (lastname != null)){
            return startdataRepos.findByFirstname_NameAndSecondname_NameAndLastname_Name(
                    firstname.getName(), secondname.getName(), lastname.getName() );
        } else {
            return null;
        }
    }
    public List<Startdata> findStartdataByRole(String role){
        return startdataRepos.findStartdataByRole( role );
    }

    public List<Startdata> findStartdataByGroupp(Groupp groupp){
        return startdataRepos.findStartdataByGroupp_Id(groupp.getId());
    }

}
