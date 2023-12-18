package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.repo.PlanRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    @Autowired
    private PlanRepos planRepos;


    public void addPlan(Plan plan) {
        List<Plan> planList = planRepos.findPlanByActionAndCall_IdAndCourse_Id( plan.getAction(), plan.getCall().getId(), plan.getCourse().getId());
        if ((planList == null) || (planList.size() == 0)) {
            planRepos.saveAndFlush( plan );
        }
    }

    public void update(int numberoflab, String action, Long callid, Long courseid) {
        List<Plan> planFromDB = planRepos.findPlanByActionAndCall_IdAndCourse_Id( action, callid, courseid );
        if ((planFromDB != null) && (planFromDB.size() > 0)) {
            Plan plan = planFromDB.get( 0 );
            plan.setNumberoflab( numberoflab );
            planRepos.saveAndFlush( plan );
        }
    }

    public List<Plan> findPlanByCall_IdAndCourse_Id(Call call, Course course){
        if ((call == null) || (course == null)){
            return null;
        }
        //List<Plan> plans = planRepos.findAll();
        Long count = planRepos.count();
        if (count == 0){
            return null;
        }
        return planRepos.findPlanByCall_IdAndCourse_Id( call.getId(), course.getId() );
    }

    public  List<Plan>  findPlanByTypezAndCourse_IdAndGpg(Course course, int typez, int gpg){
        if (course == null){
            return null;
        }
        //return planRepos.findPlanByCourse_IdAndTypez( course.getId(), type );
        //return planRepos.findPlanByTypezAndCourse_Id( typez, course.getId() );
        //return planRepos.findPlanByTypezAndCourse_Id( typez, course.getId(), Sort.by("action") );
        return null;// planRepos.findPlanByTypezAndGpgAndCourse_Id( typez, gpg, course.getId() );
    }

    public Plan findPlanByActionAndCall_Id(String action, Call call){
        if (call == null) {
            return null;
        }
        return planRepos.findPlanByActionAndCall_Id( action, call.getId() );
    }


    public List<Plan> findByActionAndTeacher_Id(String action, Teacher teacher){
        if (teacher == null){
            return null;
        }
        return planRepos.findByActionAndTeacher_Id( action, teacher.getId() );
    }

    public List<Plan> findByActionAndGroupp(String action, Groupp groupp){
        if (groupp == null){
            return null;
        }
        if (planRepos.count() > 0) {
            List<Plan> res = planRepos.findPlanByActionAndGroupp_Id(action, groupp.getId());
            return res;
        }
        return null;
    }

    public Optional<Plan> findById(Long planid){
        return planRepos.findById( planid );
    }

    public List<Plan> findByCourse_Id(Course course){
        if (course == null){
            return null;
        }
        return planRepos.findByCourse_Id( course.getId() );
    }

    public List<Plan> findByCourse_Id(Long courseid){
        return planRepos.findByCourse_Id( courseid );
    }
}
