package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Facultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface FacultatRepos extends JpaRepository<Facultat, Long> {
    //24.03.2021

    Facultat findByNameAndForma(String name, String forma);

    @Override
    List<Facultat> findAll();
}
