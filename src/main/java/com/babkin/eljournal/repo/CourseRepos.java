package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//@Transactional
public interface CourseRepos extends JpaRepository<Course, Long> {

    @EntityGraph(attributePaths = { "groupp", "teacher" })
    Course findByGroupp_IdAndTeacher_Id(Long groupid, Long teacherid);
    @EntityGraph(attributePaths = { "groupp", "teacher" })
    Course findByNameCourseFullAndGroupp_IdAndTeacher_Id(String namefull, Long groupid, Long teacherid);

    //    @Query("SELECT e  FROM Course e WHERE e.groupp.id = ?1 AND e.groupp.namesubgroupp > 0 AND e.teacher.id = ?2")
    @EntityGraph(attributePaths = { "groupp", "teacher" })
    List<Course> findAllByGrouppIdAndTeacher_Id(Long groupid, Long teacherid);


    @EntityGraph(attributePaths = { "groupp", "teacher" })
    List<Course> findByTeacher_Id(Long id);

//    List<Course> findAllByTeacherNot_AndNameCourseFull_AndGroupp_Id(
//            Teacher teacher, String name, Long grouppid);
//    List<Course> findAllByTeacherNot_AndNameCourseFull_AndGroupp_Id(
//            Teacher teacher, String name, Long grouppid);
//    List<Course> findAllByTeacherNotAndNameCourseFullAndGroupp_Id(
//            Teacher teacher, String name, Long grouppid);
    List<Course> findAllByTeacher_IdNotAndGroupp_Namegroupp(Long id, String nameGr);

    @EntityGraph(attributePaths = { "teacher", "groupp" })
    List<Course> findByGroupp_Id(Long grouppid);

    @EntityGraph(attributePaths = { "groupp", "teacher" })
    @Query("SELECT e  FROM Course e order by e.nameCourseFull")
    @Override
    List<Course> findAll();

    @EntityGraph(attributePaths = { "groupp", "teacher" })
    Course findCourseById(Long courseid);

}
