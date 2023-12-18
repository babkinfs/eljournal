package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Course;
import com.babkin.eljournal.entity.working.Raspisanie;
import com.babkin.eljournal.entity.working.Theme;
import com.babkin.eljournal.repo.ThemeRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepos themeRepos;

    public Theme save(String nametheme, String zadanie, int number, String typeZan,
                      String fileshablon, String fileforstudent, Course course){
        if (course == null){
            return null;
        }
//        Theme themeFromDb = themeRepos.findThemeByNametemeAndZadanieAndTypezanAndFileshablonAndFileforstudentAndCourse_Id(
//                nametheme, zadanie, typeZan, fileshablon, fileforstudent, course.getId());
//        List<Theme> themeFromDb = themeRepos.findThemesByCourse_IdAndTypezan(course.getId(), typeZan);
        List<Theme> themeFromDb = themeRepos.findThemesByCourse_IdAndTypezanAndNameteme(course.getId(), typeZan, nametheme);
        Theme res = null;
        if (themeFromDb.size()==0){
            res = new Theme(nametheme, zadanie, number, typeZan, fileshablon, fileforstudent, course);
            res = themeRepos.save( res );
        } else {
            res = themeFromDb.get(0);
        }
        return res;
    }

    public List<Theme> findAllByCourse(Course course){

        return themeRepos.findAllByCourse_Id(course.getId());
    }
    Theme findThemesByCourse_AndNumberAnd_Typezan(Course course, int number, String typez){
        if (course == null){
            return null;
        }
        return themeRepos.findThemesByCourse_IdAndNumberAndTypezan(course.getId(), number, typez);
    }
    public  List<Theme> findThemesByCourse_IdAndTypezan(Course course, String typezan){
        if (course == null){
            return null;
        }
        return themeRepos.findThemesByCourse_IdAndTypezan(course.getId(), typezan);
    }
    public Theme update(Theme theme){
        return themeRepos.saveAndFlush( theme );
    }
}
