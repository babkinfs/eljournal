package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Secondname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public interface SecondnameRepos extends JpaRepository<Secondname, Long> {
    //24.03.2021

    Secondname findByName(String name);
}
