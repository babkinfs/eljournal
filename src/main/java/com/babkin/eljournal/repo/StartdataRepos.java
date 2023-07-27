package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Startdata;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface StartdataRepos extends JpaRepository<Startdata, Long> {
    //24.03.2021

    //Startdata findByFirstname_NameAndSecondname_NameAndLastname_Family(String firstname, String secondname, String lastname);
    @EntityGraph(attributePaths = { "groupp" })
    Startdata findByFirstname_NameAndSecondname_NameAndLastname_Name(String firstname, String secondname, String lastname);

    @Override
    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname" })
    List<Startdata> findAll();//временно для saveStudents() в TeacherControllerService
    @EntityGraph(attributePaths = { "firstname", "secondname", "lastname", "groupp", })
    List<Startdata> findStartdataByRole(String role);

    List<Startdata> findStartdataByGroupp_Id(Long grouppid);
}
