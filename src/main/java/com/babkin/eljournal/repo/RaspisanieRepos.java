package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Raspisanie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RaspisanieRepos  extends JpaRepository<Raspisanie, Long> {
    List<Raspisanie> findRaspisanieByActiondateAndCall_IdAndCourse_Id(String actionDate, Long callId, Long courseid);
    @EntityGraph(attributePaths = { "course", "course.groupp", "course.groupp.subgroupp", "call", "theme" })
    List<Raspisanie> findRaspisaniesByActiondateAndCall_Id(String actionDate, Long callId);
    List<Raspisanie> findRaspisaniesByCall_Id(Long callId);

    @Override
    void delete(Raspisanie raspisanie);

    @Override
    <S extends Raspisanie> S saveAndFlush(S s);
    @EntityGraph(attributePaths = { "call", "theme" })
    Raspisanie findRaspisanieByTheme_Id(Long themeid);

    @EntityGraph(attributePaths = { "theme", "call" })
    List<Raspisanie> findAllByActiondateAndCourse_Id(String actiondate, Long courseid);
    @EntityGraph(attributePaths = { "theme", "call" })
    List<Raspisanie> findAllByCourse_Id(Long courseid);

    List<Raspisanie> findAllByCourse_IdAndCall_IdAndNumber(Long courseid, Long callid, int number);
}
