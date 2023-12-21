package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Raspisanie;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import net.bytebuddy.pool.TypePool;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RaspisanieRepos  extends JpaRepository<Raspisanie, Long> {
    List<Raspisanie> findRaspisanieByActiondateAndCall_IdAndCourse_Id(String actionDate, Long callId, Long courseid);
    @EntityGraph(attributePaths = { "course", "course.groupp", "course.groupp.subgroupp", "course.teacher", "call", "theme" })
    List<Raspisanie> findRaspisaniesByActiondateAndCall_Id(String actionDate, Long callId);

    List<Raspisanie> findRaspisaniesByCall_Id(Long callId);

    @Override
    void delete(Raspisanie raspisanie);

    @Override
    <S extends Raspisanie> S saveAndFlush(S s);
    @EntityGraph(attributePaths = { "call", "theme" })
    List<Raspisanie> findRaspisanieByTheme_Id(Long themeid);

    @EntityGraph(attributePaths = { "theme", "call"})//, "groupp" , "groupp.subgroupp" })
    //List<Raspisanie> findAllByActiondateAndCourse_Id(String actiondate, Long courseid);
    List<Raspisanie> findByActiondateAndCourse_Id(String actiondate, Long courseid);
    //@Query("SELECT e  FROM Raspisanie e order by e.number")
    @EntityGraph(attributePaths = { "theme", "call" })
    List<Raspisanie> findAllByCourse_Id(Long courseid);

    //@Override
    //long count();

    List<Raspisanie> findAllByCourse_IdAndCall_IdAndNumber(Long courseid, Long callid, int number);
    List<Raspisanie> findAllByCall_Id(Long callid);
}
