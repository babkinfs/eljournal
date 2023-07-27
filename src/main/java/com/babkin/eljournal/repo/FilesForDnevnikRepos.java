package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.FilesForDnevnik;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@Transactional
public interface FilesForDnevnikRepos extends JpaRepository<FilesForDnevnik, Long> {
    FilesForDnevnik findFilesForDnevnikByDnevnik_IdAndKtocdalAndPathstudent(Long dnevnikid, String ktosdal, String path);

    List<FilesForDnevnik> findFilesForDnevnikByDnevnik_IdAndKtocdal(Long dnevnikid, String ktosdal);
    List<FilesForDnevnik> findFilesForDnevnikByKtocdalAndDatesdachiAndDnevnik_Id(String ktosdal, String datasdachi, Long dnevnikid);

    List<FilesForDnevnik> findAllByDnevnik_Id(Long dnevnikid);

    @EntityGraph(attributePaths = { "dnevnik", "dnevnik.raspisanie", "dnevnik.raspisanie.theme",
            "dnevnik.raspisanie.course", "dnevnik.raspisanie.course.groupp", "dnevnik.raspisanie.course.groupp.semestr", "dnevnik.student",
            "dnevnik.student.firstname", "dnevnik.student.secondname", "dnevnik.student.lastname" })
    Optional<FilesForDnevnik> findById(Long id);
}
