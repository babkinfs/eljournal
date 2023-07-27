package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Semestr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface SemestrRepos   extends JpaRepository<Semestr, Long> {
    Semestr findSemestrByName(String namesemestr);

    @Override
    List<Semestr> findAll();
}
