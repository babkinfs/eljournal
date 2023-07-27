package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Plan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@Transactional
public interface PlanRepos extends JpaRepository<Plan, Long> {
    //24.03.2021

    List<Plan> findPlanByActionAndCall_IdAndCourse_Id(String action, Long callid, Long courseid);

    //@EntityGraph(attributePaths = { "course" })
    @EntityGraph(attributePaths = { "groupp", "course", "theme", "call" })
    Plan findPlanByActionAndCall_Id(String action, Long callid);

    List<Plan> findPlanByCall_IdAndCourse_Id(Long callid, Long courseid);

    //@Query("SELECT MAX(numberoflab) AS numberoflab FROM Plan")
    @EntityGraph(attributePaths = { "groupp", "course", "theme", "call" })
    List<Plan> findPlanByTypezAndCourse_Id(int typez, Long courseId);
    //List<Plan> findPlanByTypezAndCourse_IdAndGpg(int typez, Long courseId, int gpg);
    @EntityGraph(attributePaths = { "groupp", "course", "theme", "call" })
    List<Plan> findPlanByTypezAndGpgAndCourse_Id(int typez, int gpg, Long courseId);

    //List<User> findByNameContainingOrderByNameAsc(String str);
    //List<Plan> findPlanByTypezAndCourse_IdAndActionContainingOrderByActionAsc(int typez, Long courseId);

    @EntityGraph(attributePaths = { "groupp", "course", "theme", "call" })
        //List<Plan> findByActionAndTeacher_Id(String action, Long teacherid);
    List<Plan> findByActionAndTeacher_Id(String action, Long teacherid);

    //24.08.2021 @EntityGraph(attributePaths = { "groupp", "groupp.year", "course", "course.semestr", "call", "teacher", "teacher.firstname", "teacher.secondname", "teacher.lastname" })
    //24.08.2021 List<Plan> findByActionAndGroupp_Id(String action, Long grouppid);
    @EntityGraph(attributePaths = { "groupp", "theme", "course", "course.semestr", "call", "teacher", "teacher.firstname", "teacher.secondname", "teacher.lastname" })
    List<Plan> findPlanByActionAndGroupp_Id(String action, Long grouppid);

    @EntityGraph(attributePaths = { "teacher", "teacher.firstname", "teacher.secondname", "teacher.lastname", "groupp", "course", "call" })
    Optional<Plan> findById(Long planid);

    @EntityGraph(attributePaths = { "teacher", "groupp", "course", "call" })
    List<Plan> findByCourse_Id(Long courseid);

}