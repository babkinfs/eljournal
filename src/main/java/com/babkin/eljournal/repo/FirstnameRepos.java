package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Firstname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface FirstnameRepos extends JpaRepository<Firstname, Long> {
    //24.03.2021

    Firstname findByName (String name);
}
