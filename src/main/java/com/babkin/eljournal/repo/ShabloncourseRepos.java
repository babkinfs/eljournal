package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Shabloncourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShabloncourseRepos extends JpaRepository<Shabloncourse, Long> {
    Shabloncourse findShabloncourseByNameAndTypezAndCourse_Id(String name, String typez, Long courseid);
}
