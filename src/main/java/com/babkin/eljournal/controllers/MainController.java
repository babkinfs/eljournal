package com.babkin.eljournal.controllers;

import com.babkin.eljournal.entity.Role;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.Verification;
import com.babkin.eljournal.service.TeacherControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.webflow.engine.model.Model;
import org.springframework.ui.Model;

import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private Verification verification;
    @Autowired
    private TeacherControllerService teacherControllerService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user){
        if (user != null) {
            Set<Role> roles = user.getRoles();
            if (roles.contains( Role.ADMIN ) ) {
                return "redirect:/main";
            }
            if (roles.contains( Role.TEACHER ) ) {
                return "redirect:/teacher1";
            }
            if (roles.contains( Role.STUDENT ) ) {
                return "redirect:/student";
            }
        }
        return "redirect:/login";
    }
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user){

        return "main";
    }
    @PreAuthorize( "hasAuthority('ADMIN')" )
    @GetMapping("/generdb")
    public String genericDb(Model model) throws Exception {
        String[] res = new String[11];
        res[0] = "Make: Сохранение содержимого файла Standart в БД "
                + verification.makeStandart(teacherControllerService.getPath(utilsController.getNameOfFile( 0 )));
        res[1] = "Make: Сохранение содержимого файла control в памяти yearsDop, groupsDop, facsDop, frmsDop, semDop "
                + verification.makeControl(teacherControllerService.getPath(utilsController.getNameOfFile( 1 )));
        res[2] = "Make: Сохранение содержимого файла lexema в памяти lexemaDop "
                + verification.makeLexema(teacherControllerService.getPath(utilsController.getNameOfFile( 2 )));

        String pathTeacher = teacherControllerService.getPath(utilsController.getNameOfFile( 3 ));
        String pathStudent = teacherControllerService.getPath(utilsController.getNameOfFile( 4 ));
        String result = verification.verificateFile(pathStudent, model, "generdb");
        res[3] = "Make: Верификация файла " + pathStudent + " на соответствие формату";
        if (result != null){
            return result;
        }
        res[4] = "Make: Сохранение содержимого файла " + pathStudent + " в памяти groupsReal, yearsReal, facsReal, frmsReal, semReal" + verification.makeStudent(pathStudent);
        result = verification.verificateFile( pathTeacher, model, "generdb");
        res[5] = "Make: Верификация файла " + pathTeacher + " на соответствие формату";
        if (result != null){
            return result;
        }
        String coursePath = teacherControllerService.getPath(utilsController.getNameOfFile( 5 ));
        result = verification.verificateFile(teacherControllerService.getPath(coursePath), model, "generdb");
        res[6] = "Make: Верификация файла " + coursePath + " на соответствие формату";
        if (result != null){
            return result;
        }
        res[7] = "Make: Сохранение содержимого файла \" + coursePath + \" в памяти yearsReal, groupsReal, semReal, facsReal, frmsReal" + verification.makeCourse(coursePath);
        verification.clearMailDop();
        result = verification.saveStudentsTeashers(pathTeacher, model, "generdb");
        if (result != null){
            return result;
        } else {
            res[8] = "Save: Сохранение содержимого файла \"" + pathTeacher + " \" в БД \"";
        }
        result = verification.makeTeacher(pathTeacher, model, "generdb");
        if (result != null){
            return result;
        } else {
            res[9] = "Save: Сохранение содержимого файла \"" + pathTeacher + " \" в БД \"";
        }
        verification.clearMailDop();
        result = verification.saveStudentsTeashers(pathStudent, model, "generdb");
        if (result != null){
            return result;
        } else {
            res[10] = "Save: " + pathStudent;
        }
        model.addAttribute("results", res);
        return "generdb";
    }

}
