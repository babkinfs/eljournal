package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Groupp;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface GroupRepos extends JpaRepository<Groupp, Long> {
    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    List<Groupp> findGrouppsByYear_Id(Long yearId);
    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    List<Groupp>  findGrouppsBySemestr_Id(Long semestrId);
    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    List<Groupp> findGrouppsByFacultat_IdAndSemestr_IdAndYear_Id(Long facultatId, Long semestrId, Long yearId);

    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    Groupp findGrouppBySemestr_IdAndYear_IdAndFacultat_IdAndSubgroupp_Id(
            Long semestrId, Long yearId, Long facultatId, Long subgrouppId);

    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    List<Groupp> findGrouppByFacultat_IdAndSemestr_IdAndYear_IdAndAndSubgroupp_Id(
            Long facultatid, Long semestrId, Long yearid, Long subgrouppid);

    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    List<Groupp> findAllByNamegroupp(String nameGroup);

    Groupp findGrouppBySubgroupp_NamesubgrouppAndFacultat_IdAndSemestr_IdAndYear_IdAndNamegroupp(
            String nameSubgroupp, Long facultatid, Long semestrId, Long yearid, String nameGroupp);

    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    Groupp findGrouppByNamegrouppAndFacultat_IdAndSemestr_IdAndYear_IdAndSubgroupp_Id(
            String nameGroup, Long facultatid, Long semestrId, Long yearid, Long nameSubGroupid);
    List<Groupp> findGrouppBySemestr_Id(Long semestrid);
    Groupp findGrouppById (Long id);
    @EntityGraph(attributePaths = { "facultat", "semestr", "year", "subgroupp" })
    @Override
    List<Groupp> findAll();

    Groupp findFirstByNamegrouppAndSubgroupp_Id(String name, Long subgrouppid);
}
