package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.GroupRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    //24.03.2021

    private GroupRepos groupRepos;

    public GroupService(GroupRepos groupRepos){
        this.groupRepos = groupRepos;
    }


    Groupp findGrouppBy_NamesubgrouppAndSemestrAndYearAndFacultat(Semestr semestr, Year year, Facultat facultat, Subgroupp subgroupp){
        if ((semestr != null) && (year != null) && (facultat != null)) {
            return groupRepos.findGrouppBySemestr_IdAndYear_IdAndFacultat_IdAndSubgroupp_Id(
                    //.findGrouppByNamesubgrouppAndSemestr_IdAndYear_IdAndFacultat_Id(
                    semestr.getId(), year.getId(), facultat.getId(), subgroupp.getId() );
        }
        return null;
    }

    public List<Groupp> findGrouppByNameGroupp(String nameGroup){
        return groupRepos.findAllByNamegroupp( nameGroup );
    }

    public Groupp findGrouppById(Long id){
        return groupRepos.findGrouppById( id );
    }

    public Groupp save(String name, Facultat facultat, Semestr semestr, Year year, Subgroupp subgroupp ) {
        if (facultat == null || semestr == null || year == null || subgroupp == null) {
            return null;
        }
        Groupp fromDB = groupRepos.findGrouppByNamegrouppAndFacultat_IdAndSemestr_IdAndYear_IdAndSubgroupp_Id(
                name, facultat.getId(), semestr.getId(), year.getId(), subgroupp.getId() );
        if (fromDB == null) {
            fromDB = new Groupp(name, facultat, semestr, year, subgroupp);
            fromDB = groupRepos.save(fromDB);
        }
        return fromDB;
    }

    public List<Groupp> findAll(){
        return  groupRepos.findAll();
    }

    public List<Groupp> findGrouppsByYear(Year year){
        return groupRepos.findGrouppsByYear_Id( year.getId() );
    }
    public List<Groupp>  findGrouppsBySemestr(Semestr semestr){
        return groupRepos.findGrouppsBySemestr_Id( semestr.getId() );
    }
    public List<Groupp> findGrouppsByFacultat_AndSemestr_AndYear(Facultat facultat, Semestr semestr, Year year){
        return groupRepos.findGrouppsByFacultat_IdAndSemestr_IdAndYear_Id( facultat.getId(), semestr.getId(), year.getId() );
    }

    public List<Groupp> getDistinctGrouppByNamegroupp(String nameGroupp){
        return null;// groupRepos.getDistinctGrouppByNamegroupp(nameGroupp);
    }

    public Groupp findGrouppBySemestr_AndYear_AndFacultat_AndSubgroupp(
            Facultat facultat, Semestr semestr, Year year, Subgroupp subgroupp){
        if (facultat == null || semestr == null || year == null || subgroupp == null) {
            return null;
        }
        return groupRepos.findGrouppBySemestr_IdAndYear_IdAndFacultat_IdAndSubgroupp_Id(
                semestr.getId(), year.getId(), facultat.getId(), subgroupp.getId());
    }

    public Groupp findGrouppByNamegrouppAndFacultat_AndSemestr_AndYear_AndSubgroupp(
            String nameGroupp, Facultat facultat, Semestr semestr, Year year, Subgroupp subgroupp) {
        if (facultat == null || semestr == null || year == null || subgroupp == null) {
            return null;
        }
        return groupRepos.findGrouppByNamegrouppAndFacultat_IdAndSemestr_IdAndYear_IdAndSubgroupp_Id(
                nameGroupp, facultat.getId(), semestr.getId(), year.getId(), subgroupp.getId());
    }
    public  Groupp findGrouppBySubgroupp_NamesubgrouppAndFacultat_AndSemestr_AndYear_AndNamegroupp(
            String nameSubgroupp, Facultat facultat, Semestr semestr, Year year, String nameGroupp){
        if (facultat == null || semestr == null || year == null){
            return null;
        }
        return groupRepos.findGrouppBySubgroupp_NamesubgrouppAndFacultat_IdAndSemestr_IdAndYear_IdAndNamegroupp(
                nameSubgroupp, facultat.getId(), semestr.getId(), year.getId(), nameGroupp);
    }
}
