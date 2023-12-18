package com.babkin.eljournal.service;

import com.babkin.eljournal.controllers.UtilsController;
import com.babkin.eljournal.entity.temporaly.QuickSort;
import com.babkin.eljournal.entity.working.Call;
import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Raspisanie;
import com.babkin.eljournal.entity.working.Theme;
import com.babkin.eljournal.repo.RaspisanieRepos;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RaspisanieService {
    @Autowired
    UtilsController utilsController;
    @Autowired
    private RaspisanieRepos raspisanieRepos;

    public Raspisanie save (String actiondate, int number, Call call, Theme theme, Course course) {
        if (call != null && course == null) {
            List<Raspisanie> raspisanieList = raspisanieRepos.findRaspisaniesByActiondateAndCall_Id(actiondate, call.getId());
            if (raspisanieList.size() == 0) {
                Raspisanie raspisanie = new Raspisanie(actiondate, number, "", call, theme, course);
                return raspisanieRepos.save(raspisanie);
            }
        }
        if (call != null && course != null && theme != null) {
        //if (call != null && course != null) {
            List<Raspisanie> raspisanieFromDB = raspisanieRepos.findRaspisaniesByActiondateAndCall_Id(actiondate, call.getId());
            if (raspisanieFromDB.size() > 0) {//Нужно заменить имеющуюся запись
                Raspisanie raspisanie = raspisanieFromDB.get(0);
                raspisanie.setNumber(number);
                raspisanie.setTheme(theme);
                raspisanie.setCourse(course);
                return raspisanieRepos.saveAndFlush( raspisanie );
            } else {
                raspisanieFromDB = raspisanieRepos.findRaspisanieByActiondateAndCall_IdAndCourse_Id(
                        actiondate, call.getId(), course.getId());
            }
            Raspisanie raspisanie = new Raspisanie(actiondate, number, "", call, theme, course);
            if (raspisanieFromDB.size() == 0) {
                return raspisanieRepos.save(raspisanie);
                //return raspisanie;
            }
            if ((raspisanieFromDB.get(0) == null)
                    || (raspisanieFromDB.get(0).getTheme() == null)) {// && theme != null)){
                if (theme != null) {
                    delete(raspisanieFromDB.get(0));
                    return raspisanieRepos.save(raspisanie);
                    //return raspisanie;
                }
                return raspisanieFromDB.get(0);
            }
            return raspisanieFromDB.get(0);
        }
        return null;
    }

    List<Raspisanie> findRaspisanieByTheme(Theme theme){
        return raspisanieRepos.findRaspisanieByTheme_Id(theme.getId());
    }
    //public Raspisanie findRaspisaniesByActiondateAndCall(String actionDate, Call call){
    //    if (call == null){
    //        return null;
    //    }
    //    List<Raspisanie> fromDB = raspisanieRepos.findRaspisaniesByActiondateAndCall_Id( actionDate, call.getId() );
    //    if (fromDB.size() == 0){
    //        return null;
    //    }
    //    return fromDB.get( 0 );
    //}
    public List<Raspisanie> findAllRaspisaniesByActiondateAndCall(String actionDate, Call call){
        if (call == null){
            return null;
        }
        List<Raspisanie> fromDB = raspisanieRepos.findRaspisaniesByActiondateAndCall_Id( actionDate, call.getId() );
        return fromDB;
    }

    public List<Raspisanie> findAllByActiondateAndCourse(String actiondate, Course course){
        if (course == null){
            return null;
        }
        return raspisanieRepos.findAllByActiondateAndCourse_Id(actiondate,
                course.getId());
    }
    public List<Raspisanie> findAllByCourse(Course course){
        if (course == null){
            return null;
        }
        Long index = course.getId();
        return raspisanieRepos.findAllByCourse_Id(index);
    }
    public List<Raspisanie> findByCourseAndLessActiondate(Course course, String actiondate) throws ParseException {
        List<Raspisanie> raspisanieList = findAllByCourse(course);
        if (raspisanieList != null){
            List<Raspisanie> raspisanieTemp = new ArrayList<>();
            for (Raspisanie rasp : raspisanieList){
                String current = rasp.getActiondate();
                if (utilsController.compareDate(current, actiondate) <= 0){
                    raspisanieTemp.add(rasp);
                }
            }
            QuickSort.quickSort(raspisanieTemp, 0, raspisanieTemp.size()-1);
            return raspisanieTemp;
        }
        return null;
    }

    public void delete(Raspisanie raspisanie){//удалить сущестующие связи

        raspisanieRepos.delete( raspisanie );
    }

    public Raspisanie update(Raspisanie raspisanie){
        return raspisanieRepos.saveAndFlush( raspisanie );
    }
    public List<Raspisanie> findAllByCourse_IdAndCall_IdAndNumber(
            Course course, Call call, int number){
        if (course == null || call == null){
            return null;
        }
        return raspisanieRepos.findAllByCourse_IdAndCall_IdAndNumber(
                course.getId(), call.getId(), number);
    }
    public long count(){
        return raspisanieRepos.count();
    }
    public List<Raspisanie> findRaspisaniesByActiondateAndCall(String actionDate, Call call) {
        if (call == null){
            return null;
        }
        return raspisanieRepos.findRaspisaniesByActiondateAndCall_Id(actionDate, call.getId());
    }
    public Raspisanie findByActiondateAndCall(String actionDate, Call call){
        Raspisanie raspisanie = null;
        List<Raspisanie> raspisanies = findRaspisaniesByActiondateAndCall(actionDate, call);
        if (raspisanies.size()>0){
            raspisanie = raspisanies.get( 0 );
        }
        return null;
    }

    public List<Raspisanie> findAllByCallAndCourseEmpty(Call call){
        if (call == null){
            return null;
        }
        return raspisanieRepos.findAllByCall_Id(call.getId());
    }
    public List<Raspisanie> findAll(){
        return raspisanieRepos.findAll();
    }
}
