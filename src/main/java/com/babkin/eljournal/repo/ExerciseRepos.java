package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepos extends JpaRepository<Exercise, Long> {
    Exercise findExerciseByKeyAndBodyAndTheme_IdAndStartdata_Id(String key, String body, Long themeid, Long startdataid);
    List<Exercise> findAllByTheme_IdAndStartdata_Id(Long themeid, Long startdataid);

    List<Exercise>  findExerciseByKeyAndStartdata_IdAndTheme_Id(String key, Long startdataid, Long themeid);
}
