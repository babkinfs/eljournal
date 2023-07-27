package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Subgroupp;
import com.babkin.eljournal.repo.SubgrouppRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubgrouppService {
    @Autowired
    private SubgrouppRepos subgrouppRepos;

    public Subgroupp saveSubgroupp(String nameSubgroupp) {
        Subgroupp subgrouppFromDB = subgrouppRepos.findByNamesubgroupp( nameSubgroupp );
        if (subgrouppFromDB == null) {
            subgrouppFromDB = new Subgroupp( nameSubgroupp );
            return subgrouppRepos.save( subgrouppFromDB );
        }
        return subgrouppFromDB;
    }
    public Subgroupp findSubgrouppByNamesubgroupp(String namesubgroupp){
        return subgrouppRepos.findByNamesubgroupp( namesubgroupp );
    }

    public int getCount(){
        List<Subgroupp> list = subgrouppRepos.findAll();
        return list.size();
    }
}
