package com.babkin.eljournal.repo;

import com.babkin.eljournal.entity.working.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Transactional
public interface ThemeRepos extends JpaRepository<Theme, Long> {

    Theme findThemeByNametemeAndZadanieAndTypezanAndFileshablonAndFileforstudentAndCourse_Id(
            String nametheme, String zadanie, String typez, String namefile, String fileforstudent, Long courseId);

    //Theme findThemeByNumberAndTypezan(int number, String typez);
    Theme findThemesByCourse_IdAndNumberAndTypezan(Long courseid, int number, String typez);

    List<Theme> findAllByCourse_Id(Long courseid);

    List<Theme> findThemesByCourse_IdAndTypezan(Long courseid, String typezan);
    List<Theme> findThemesByCourse_IdAndTypezanAndNameteme(Long courseid, String typezan, String nameTheme);
}
