package com.babkin.eljournal.controllers;

import com.babkin.eljournal.entity.Role;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.Verification;
import com.babkin.eljournal.entity.working.Plan;
import com.babkin.eljournal.entity.working.Student;
import com.babkin.eljournal.entity.working.Teacher;
import com.babkin.eljournal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.webflow.engine.model.Model;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private Verification verification;
    @Autowired
    private TeacherControllerService teacherControllerService;
    @Autowired
    private MainControllerService mainControllerService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PlanService planService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user){
        if (user != null) {
            Set<Role> roles = user.getRoles();
            if (roles.contains( Role.ADMIN ) ) {
                return "redirect:/main";
            }
            if (roles.contains( Role.TEACHER ) || roles.contains( Role.LECTOR )) {
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
    @GetMapping("/raspisanie")
    public String raspisanieZan(@AuthenticationPrincipal User user, Model model) throws ParseException {
        mainControllerService.makeRaspisanie(user, model, utilsController.getDataNow());
        model.addAttribute("aDateTime", utilsController.getDataNow(new SimpleDateFormat("dd-MM-yyyy HH:mm")));
        String tdate = utilsController.getDataNow();
        model.addAttribute( "tdate", tdate );
        if (user != null) {
            List<Plan> plans = new ArrayList<Plan>();
            Set<Role> roles = user.getRoles();
            if (roles.contains( Role.STUDENT )) {
                Student student = studentService.findByUser( user );
                model.addAttribute( "student", student );
                plans = planService.findByActionAndGroupp( tdate, student.getGroupp() );
            }
            if (roles.contains( Role.TEACHER ) || (roles.contains( Role.LECTOR ) )) {
                Teacher teacher = teacherService.findTeacherByUser( user );
                model.addAttribute( "student", teacher );
                plans = planService.findByActionAndTeacher_Id( tdate, teacher );
                model.addAttribute( "plans", plans );
                for (Plan plan : plans){

                }
                //teacherControllerService.makeNewModel( model, user, 0, Integer.parseInt( UtilsController.facultatInit ),
                //        Integer.parseInt( UtilsController.yearInit ),
                //        UtilsController.semestrInit, Integer.parseInt( UtilsController.groupInit ), UtilsController.courseInit,
                //        tdate, Integer.parseInt( UtilsController.callInit ), 1l, 2L,
                //        Integer.parseInt( UtilsController.periodInit ), Integer.parseInt( UtilsController.counterInit ),
                //        UtilsController.facultatInit, UtilsController.yearInit, UtilsController.semestrInit, UtilsController.groupInit,
                //        UtilsController.courseInit, UtilsController.callInit, UtilsController.periodInit, UtilsController.counterInit );

            }
            model.addAttribute( "plans", plans );
        }
//        List<Call> calls = callService.findAll();
//        model.addAttribute( "callid", 1 );
//        model.addAttribute( "calls", calls );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );
        model.addAttribute( "timeview", false );
        return "raspisanie";
    }

    @PostMapping("/raspisanie")
    public String raspisanieZan(@AuthenticationPrincipal User user){
        if (user != null) {
            Set<Role> roles = user.getRoles();
            if (roles.contains( Role.ADMIN ) ) {
                return "redirect:/main";
            }
            if (roles.contains( Role.TEACHER ) ) {
                return "redirect:/teacher";
            }
            if (roles.contains( Role.STUDENT ) ) {
                return "redirect:/student";
            }
        }
        //model.addAttribute( "aDateTime", utilsController.getDataNow() );
        return "raspisanie";
    }
    @GetMapping("/calraspisanie/{aDateTime}")
    public String calraspisanie(@PathVariable("aDateTime") String aDateTime,
                                Model model, @AuthenticationPrincipal User user){
        mainControllerService.makeRaspisanie(user, model, aDateTime);
        model.addAttribute("aDateTime", utilsController.getDataNow(new SimpleDateFormat("dd-MM-yyyy HH:mm")));
        //List<Plan> plans = null;
        //String studentName = null;
        //Student student = studentService.findByUser( user );
        //if (student == null){
        //    Teacher teacher = teacherService.findTeacherByUser( user );
        //    studentName = teacher.getFullFio();
        //    plans = planService.findByActionAndTeacher_Id( aDateTime, teacher );
        //} else {
        //    studentName = student.getFullFio();
        //    plans = planService.findByActionAndGroupp( aDateTime, student.getGroupp() );
        //}
        //model.addAttribute( "plans", plans );
        //model.addAttribute( "timeview", false );
        //model.addAttribute( "student", studentName );
        return "raspisanie";
    }
}
