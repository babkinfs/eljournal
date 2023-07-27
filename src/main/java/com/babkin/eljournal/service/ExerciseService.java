package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Exercise;
import com.babkin.eljournal.entity.working.Startdata;
import com.babkin.eljournal.entity.working.Theme;
import com.babkin.eljournal.repo.ExerciseRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepos exerciseRepos;

    public Exercise save(String key, String body, Theme theme, Startdata startdata){
        if ((theme == null) || (startdata == null)){
            return null;
        }
        Exercise exercise = exerciseRepos.findExerciseByKeyAndBodyAndTheme_IdAndStartdata_Id(
                key, body, theme.getId(), startdata.getId());
        if (exercise == null){
            exercise = exerciseRepos.save(new Exercise(key, body, theme, startdata));
        }
        return exercise;
    }
    public List<Exercise> findAllByTheme_AndStartdata(Theme theme, Startdata startdata){
        return exerciseRepos.findAllByTheme_IdAndStartdata_Id(theme.getId(), startdata.getId());
    }

    public Exercise findExerciseByKeyAndStartdata_AndTheme_(String key, Startdata startdata, Theme theme){
        if (startdata == null || theme == null){
            return null;
        }
        List<Exercise> rsr = exerciseRepos.findExerciseByKeyAndStartdata_IdAndTheme_Id(key, startdata.getId(), theme.getId());
        if (rsr.size() == 0){
            return null;
        }
        return exerciseRepos.findExerciseByKeyAndStartdata_IdAndTheme_Id(key, startdata.getId(), theme.getId()).get(0);
    }
}
