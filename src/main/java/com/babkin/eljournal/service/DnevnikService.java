package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.DnevnikRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DnevnikService {

    @Autowired
    private DnevnikRepos dnevnikRepos;

    public List<Dnevnik> findByStudent_IdAndPlan_Id(Student student, Plan plan){
        if ((plan == null) || (student == null)){
            return new ArrayList<>();
        }
        return null;//18 08 2022   dnevnikRepos.findByStudent_IdAndPlan_Id(student.getId(), plan.getId() );
    }

    public void save(Dnevnik dnevnik){
        if (dnevnik != null) {
            //18 08 2022  List<Dnevnik> dnevnikFromDb = findByStudent_IdAndPlan_Id( dnevnik.getStudent(), dnevnik.getPlan() );
            //18 08 2022  if (dnevnikFromDb.size() == 0) {
            //18 08 2022      dnevnikRepos.save( dnevnik );
            //18 08 2022  }
        }
    }

    public Dnevnik addDnevnik(boolean ispresent, Student student, Raspisanie raspisanie) {
        if (student == null || raspisanie == null) {
            return null;
        }
        Dnevnik dnevnikFromDB = dnevnikRepos.findDnevnikByStudent_IdAndRaspisanie_Id(
                student.getId(), raspisanie.getId());
        if (dnevnikFromDB == null) {
            dnevnikFromDB = dnevnikRepos.save(new Dnevnik(ispresent, student, raspisanie));
        }
        return dnevnikFromDB;
    }

    public void update(Dnevnik dnevnik){
        dnevnikRepos.save( dnevnik );
    }

    public List<Dnevnik> findDnevniksByPlanCourse_IdAndStudent_Id(Course course, Student student){
        if ((course == null) || (student == null)){
            return null;
        }
        return null;//18 08 2022// dnevnikRepos.findDnevniksByPlanCourse_IdAndStudent_Id( course.getId(), student.getId() );
    }

    public List<Dnevnik> findDnevniksByPlan_ActionAndPlan_CourseAndStudent_Id(String action, Course course, Student student){
        if ((course == null) || (student == null)){
            return null;
        }
        return null;//18 08 2022   dnevnikRepos.findDnevniksByPlan_ActionAndPlan_CourseAndStudent_Id( action, course.getId(), student.getId() );
    }
    public List<Dnevnik> findDnevniksByDateteacherAndPlan(String date, Plan plan){
        if (plan == null){
            return null;
        }
        return null;//18 08 2022  dnevnikRepos.findDnevniksByDateteacherAndPlan_Id( date, plan.getId() );
    }

    public List<Dnevnik> findAllByRaspisanie_AndStudent(Raspisanie respisanie, Student student){
        if (respisanie == null || student == null){
            return null;
        }
        return dnevnikRepos.findAllByRaspisanie_IdAndStudent_Id(respisanie.getId(), student.getId());
    }
    public List<Dnevnik> findAllBy_Less_Raspisanie_AndStudent(List<Raspisanie> raspisanieList, Student student){
        if (raspisanieList == null || student == null || raspisanieList.size() == 0){
            return null;
        }
        List<Dnevnik> dnevnikList = new ArrayList<>();
        for (Raspisanie raspisanie : raspisanieList ) {
            dnevnikList.addAll(dnevnikRepos.findAllByRaspisanie_IdAndStudent_Id(raspisanie.getId(), student.getId()));
        }
        return dnevnikList;
    }

    public List<Dnevnik> findAllByRaspisanie(Raspisanie raspisanie){
        if (raspisanie == null){
            return null;
        }
        return dnevnikRepos.findAllByRaspisanie_Id(raspisanie.getId());
    }
}
