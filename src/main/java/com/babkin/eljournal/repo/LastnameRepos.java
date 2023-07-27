package com.babkin.eljournal.repo;


import com.babkin.eljournal.entity.working.Lastname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface LastnameRepos extends JpaRepository<Lastname, Long> {
    //24.03.2021

    Lastname findByName(String family);
}
