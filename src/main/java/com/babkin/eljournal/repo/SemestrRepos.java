package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Semestr;
import net.bytebuddy.TypeCache;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.MethodSortMatcher;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface SemestrRepos   extends JpaRepository<Semestr, Long> {
    Semestr findSemestrByName(String namesemestr);
    //Semestr findSemestrById(Long id);

    //@Override
    //List<Semestr> findAll(Sort sort);

    //@Override
    List<Semestr> findByOrderByNameAsc();
}
