package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Subgroupp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubgrouppRepos  extends JpaRepository<Subgroupp, Long> {
    //Subgroupp findSubgrouppByNamesubgroupp(String namesubgroupp);
    //Subgroupp findSubgrouppsById(Long id);
    Subgroupp findByNamesubgroupp(String namesubgroupp);
}
