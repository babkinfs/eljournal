package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface YearRepos   extends JpaRepository<Year, Long> {
    Year findByFirstnameyearAndSecondnameyear(String firstnameyear, String secondnameyear);

    @Override
    List<Year> findAll();
}
