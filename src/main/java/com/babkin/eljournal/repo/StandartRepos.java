package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Standart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandartRepos  extends JpaRepository<Standart, Long> {

    Standart findStandartByNamecourse(String nameCourse);

}
