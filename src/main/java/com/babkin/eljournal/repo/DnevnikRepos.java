package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Dnevnik;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface DnevnikRepos extends JpaRepository<Dnevnik, Long> {

    @EntityGraph(attributePaths = { "student", "raspisanie" })
    @Override
    List<Dnevnik> findAll();

    List<Dnevnik> findAllByRaspisanie_IdAndStudent_Id(Long respisanie_id, Long student_id);


    @EntityGraph(attributePaths = { "student", "raspisanie" })
    Dnevnik findDnevnikByStudent_IdAndRaspisanie_Id(Long studentid, Long raspisanieid);

    @EntityGraph(attributePaths = { "student", "raspisanie" })
    List<Dnevnik> findAllByRaspisanie_Id(Long raspisanieid);
}










