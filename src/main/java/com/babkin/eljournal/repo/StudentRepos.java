package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface  StudentRepos extends JpaRepository<Student, Long> {
    Student findByFirstname_IdAndSecondname_IdAndLastname_IdAndGroupp_Id (
            Long firstname, Long secondname, Long lastname, Long groupid);

    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname", "user" })
    List<Student> findByGrouppId(Long groupid, Sort sort);

    @EntityGraph(attributePaths = {"groupp", "firstname", "secondname",
            "lastname", "user", "groupp.semestr", "groupp.year", "groupp.facultat", "groupp.subgroupp" })
    Student findByUser_Id(Long userid);
}