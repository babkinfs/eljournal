package com.babkin.eljournal.controllers;

import com.babkin.eljournal.entity.Period;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.DoubleString;
import com.babkin.eljournal.entity.temporaly.ModelForStart;
import com.babkin.eljournal.entity.temporaly.StudentFull;
import com.babkin.eljournal.entity.temporaly.Verification;
import com.babkin.eljournal.entity.temporaly.docword.UpdateDocument;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher1")
@PreAuthorize( "hasAuthority('LECTOR') || hasAuthority('TEACHER')" )
public class TeacherController1 {
    @Value( "${upload.path}" )
    private String uploadPath;
    @Value("${base.path}")
    private String basepath;
    @Value("${path.antwer}")
    private String pathantwer;

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherControllerService teacherControllerService;
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private CallService callService;
    @Autowired
    private RaspisanieService raspisanieService;
    @Autowired
    private Verification verification;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentControllerService studentControllerService;
    @Autowired
    private UserService userService;
    @Autowired
    private DnevnikService dnevnikService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private FilesForDnevnikService filesForDnevnikService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UpdateDocument updateDocument;

    //public static final String PATH_ANSWER = "d:\\Answers\\";
    @GetMapping
    public String baseTeacher(HttpSession session, @AuthenticationPrincipal User user, Model model) throws ParseException {

        Teacher teacher = teacherService.findTeacherByUser(user);
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanes",
                0,
                UtilsController.periodidInit,
                UtilsController.yearIdInit,
                UtilsController.semestrIdInit,
                UtilsController.facultatIdInit,
                UtilsController.typezIdInit,
                UtilsController.groupIdInit,
                UtilsController.courseIdInit,
                UtilsController.callIdInit,
                utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")),
                false, teacher, model);
        List<String> galka = (List<String>) session.getAttribute("galka");
        if (galka == null ){
            galka = new ArrayList<>();
            session.setAttribute("galka", galka);
        }
        model.addAttribute("visibleKeep", galka.size()>0);
//        model.addAttribute("visibleKeep", true);

        String dateNow = utilsController.getDataNow(new SimpleDateFormat("dd-MM-yyyy hh:mm"));
        String[] dateNowArr = dateNow.split(" ");
        model.addAttribute("razdel", "Стартовый");
        model.addAttribute("todir", "baseteacher");
        model.addAttribute("viewcall", true);
        model.addAttribute("viewdate", true);
        model.addAttribute("viewdateUdalit", false);
        model.addAttribute("viewbuttonzagruzit", false);
        model.addAttribute("viewbuttonsohranit", true);
        model.addAttribute("activSohranit", true);
        model.addAttribute("aDateTime", dateNow);
        //String value = (String) session.getAttribute("baseteachersave");
        //if (value!=null && value.equals("true")){
        //        model.addAttribute("baseteachersave", true);
        //    } else {
        //    model.addAttribute("baseteachersave", false);
        //}
        dateNow = dateNowArr[0]; //utilsController.getDataNow();

        Call callNow = callService.findByName(utilsController.findeCurrentCall(dateNowArr[1]));
        List<Raspisanie> raspisanieList = raspisanieService.findAllRaspisaniesByActiondateAndCall(dateNow, callNow);

        //Предварительно определить расписание
        Raspisanie currentRaspisanie = raspisanieList.get(0);
        Groupp groupp = currentRaspisanie.getCourse().getGroupp(); //modelForStart.getGroupp();
        List<Student> studentList = studentControllerService.getStudents(groupp);
        String time = utilsController.getTimeNow();
        model.addAttribute("time", time);
        if (studentList.size() == 0){
            model.addAttribute("currentRaspisanie", null);
            return "baseteacher";
        }
        //18 08 2023 List<Raspisanie> raspisanieList = studentControllerService.getRaspisaniebYGroupp(dateNow, groupp, teacher);
        //18 08 2023 Raspisanie currentRaspisanie = studentControllerService.getCurrentRaspisanie(raspisanieList, time);
        //if (currentRaspisanie != null) {
        //    teacherControllerService1.fillDnevnik(user, model, currentRaspisanie, studentList);
        //}
        model.addAttribute("currentRaspisanie", currentRaspisanie);
        List<Dnevnik> dnevnikList = new ArrayList<>();
        List<StudentFull> studentFulls = new ArrayList<>();
        if (currentRaspisanie != null) {
            Groupp currentGroupp = currentRaspisanie.getCourse().getGroupp();
            teacherControllerService.fillDnevnik(user, model, currentRaspisanie, studentList);
            int indexStudentFull = 1;
            for (Student student : studentList) {
                boolean ok = false;
                List<Dnevnik> temp = dnevnikService.findAllByRaspisanie_AndStudent(currentRaspisanie, student);
                if ((student.getGroupp().getSubgroupp().getNamesubgroupp().equals(currentGroupp.getSubgroupp().getNamesubgroupp()))
                        || (currentGroupp.getSubgroupp().getNamesubgroupp().equals("0"))){
                    //|| (student.getGroupp().getSubgroupp().getNamesubgroupp().indexOf(".")<0)) {
                    if (!student.getGroupp().getSubgroupp().getNamesubgroupp().equals("0")) {//удаляем студентов изначально в 0 подгруппе
                        boolean ochnoZaochno = galka.contains(Integer.toString(indexStudentFull));
                        Dnevnik tempDnevnik = new Dnevnik();
                        //tempDnevnik.setOchno(ochnoZaochno);
                        if (temp.size() > 0) {
                            dnevnikList.addAll(temp);
                            tempDnevnik = temp.get(0);
                            if (!tempDnevnik.isOchno()) {
                                tempDnevnik.setOchno(ochnoZaochno);
                            }
                            ok = true;
                            studentFulls.add(new StudentFull(student, ok, tempDnevnik));
                        } else {
                            studentFulls.add(new StudentFull(student, ok, null));
                        }
                        //studentFulls.add(new StudentFull(student, ok, tempDnevnik));
                        indexStudentFull++;
                    }
                }
            }
        }
        if (studentFulls.size() == 0){
            studentFulls  = null;
        } else {
            raspisanieList = null;
        }
        model.addAttribute("raspisanieList", raspisanieList);
        session.setAttribute("studentFulls", studentFulls);
        return "baseteacher";
    }


    @PostMapping("/checking")
    public String checking(HttpSession session, Model model,
                           //@RequestParam String calcstd,
                           @RequestParam String nameCheckValue
    ) {
        //String value = (String) session.getAttribute("startProverka");
        //session.setAttribute("startProverkaNamefile", filenameOut);
        //String nameCheckValue ="";
        List<String> galka = (List<String>)session.getAttribute("galka");
        String[] indexesString = nameCheckValue.split(",");
        List<Integer> indexesInt = new ArrayList<Integer>();
        //for (String st : indexesString) {
        //    if (!st.equals("")) {
        //        indexesInt.add(Integer.parseInt(st));
        //    }
        //}
        for (String st : galka) {
            if (!st.equals("")) {
                indexesInt.add(Integer.parseInt(st));
            }
        }

        List<StudentFull> studentFulls = (List<StudentFull>) session.getAttribute("studentFulls");
        for (int i = 0; i < studentFulls.size(); i++) {
            for (int j = 0; j < indexesInt.size(); j++) {
                int tempInt = indexesInt.get(j) - 1;
                if (i == tempInt) {
                    Dnevnik dnevnik = studentFulls.get(i).getOchnoZaochno();
                    if (dnevnik != null) {
                        dnevnik.setOchno(true);
                        dnevnikService.update(dnevnik);
                    }
                    studentFulls.get(i).setOchnoZaochno(dnevnik);
                }
            }
        }
        //model.addAttribute("baseteachersave", true);
        session.setAttribute("baseteachersave", "true");
        session.setAttribute("galka", new ArrayList<>());

        return "redirect:/";
//        model.addAttribute("aDateTime", utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")));//??calcstd
//        String time = utilsController.getTimeNow();
//        model.addAttribute("time", time);
//        model.addAttribute( "razdel", "??? 175" );
//        return "baseteacher";
    }

    @PostMapping
    public String baseTeacher(Model model,
                              //@RequestParam(name = "plan") Long planid,
                              @RequestParam String theme, @RequestParam String zadanie,
                              @RequestParam String calcstd,
                              @RequestParam int search_call,
                              @RequestParam String nameCheckValue
    ) {
        model.addAttribute( "students1", null );
        model.addAttribute( "students2", null );
        model.addAttribute( "aDateTime", calcstd );//??
        model.addAttribute( "calcstd", calcstd );
        model.addAttribute( "timeview", true );

        List<Call> calls = callService.findAll();
        model.addAttribute( "calls", calls );
        Long callid = 0L;
        model.addAttribute( "callid", callid );
        return "baseteacher";
    }

    @GetMapping("/courses")
    public String courses(@AuthenticationPrincipal User user, Model model) {

        Teacher teacher = teacherService.findTeacherByUser( user );
        //List<Course> courses = courseService.findByTeacher( teacher );
        List<Course> courses = courseService.findAll();
        model.addAttribute( "courses", courses );
        return "courses";
    }

    @PostMapping("/courses")
    public String courses(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        //String orig = file.getOriginalFilename();
        //String nm = file.getName();
        //String ct = file.getContentType();

        String namefile = teacherControllerService.getPath( file.getOriginalFilename() );
        String result = verification.makeLexema( teacherControllerService.getPath( utilsController.getNameOfFile( 2 ) ) );
        result = verification.verificateFile( namefile, model, "courses" );
        if (result != null) {
            return result;
        }
        //Reader reader = new FileReader( UtilsController.getPath() + "//" + file.getOriginalFilename() );
        Reader reader = new FileReader( namefile );
        BufferedReader buffReader = new BufferedReader( reader );
        String lineStart = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        if (!lineStart.equals( "# Количество пар~Полное наименование~Краткое наим.r~год~группа~подгруппа~семестр~факультет~форма обуч.~ФИО" )) {
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "TeacherController/courses : Файл " + file.getOriginalFilename() + " содержит ошибки!" );
            return "courses";
        }
        lineStart = buffReader.readLine();
        while (buffReader.ready()) {

            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (!line.equals( "" )) {
                String[] st = line.split( "~" );
                result = teacherControllerService.fillCourse( st );
                if (result != null) {
                    model.addAttribute( "messageType", "danger" );
                    model.addAttribute( "message", result );
                }
            }
        }
        Teacher teacher = teacherService.findTeacherByUser( user );
        //List<Course> courses = courseService.findByTeacher( teacher );
        List<Course> courses = courseService.findByTeacher(teacher);
        model.addAttribute( "courses", courses );

        //teacherControllerService.saveStudents();
        //teacherControllerService.savePlans(teacher);

        return "courses";
    }

    @GetMapping("/variantes")
    public String variantes(Model model, @AuthenticationPrincipal User user){
        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanes",
                0,
                UtilsController.periodidInit,
                UtilsController.yearIdInit,
                UtilsController.semestrIdInit,
                UtilsController.facultatIdInit,
                UtilsController.typezIdInit,
                UtilsController.groupIdInit,
                UtilsController.courseIdInit,
                UtilsController.callIdInit,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "Варианты лаб." );
        model.addAttribute( "viewcall", false );
        model.addAttribute( "viewdate", false );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", true );
        model.addAttribute( "viewbuttonsohranit", false );
        model.addAttribute( "activSohranit", true );
        model.addAttribute("todir", "nplanesdoubl");
        model.addAttribute( "dirteacher", "nplanesdoubl" );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );
        return "variantes";
    }

    @PostMapping("/variantes")
    public String variantes(
            @RequestParam int period,
            @RequestParam int year,
            @RequestParam int semestr,
            @RequestParam int facultat,
            @RequestParam int typez,
            @RequestParam int groupp,
            @RequestParam int course,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Model model){
        Teacher teacher = teacherService.findTeacherByUser( user );

        model.addAttribute( "razdel", "Варианты лаб." );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", true );
        model.addAttribute( "todir", "zadaniya" );
        ModelForStart modelForStart = teacherControllerService.prepareStart( "zadainya", 0, period, year,
                semestr, facultat, typez, groupp, course, 0,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "viewcall", false );
        model.addAttribute( "viewdate", false );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        return "variantes";
    }

    @GetMapping("/nplanes")
    public String nplanes(Model model, @AuthenticationPrincipal User user) throws ParseException, IOException {


        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanes",
                0,
                UtilsController.periodidInit,
                UtilsController.yearIdInit,
                UtilsController.semestrIdInit,
                UtilsController.facultatIdInit,
                UtilsController.typezIdInit,
                UtilsController.groupIdInit,
                UtilsController.courseIdInit,
                UtilsController.callIdInit,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "Планы" );
        model.addAttribute( "viewcall", true );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute("DateFill", new SimpleDateFormat("E MMM dd yyyy H:m:s zzzz").format(new Date()));
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        String result = teacherControllerService.calcRasp( model,
                "nplanes", 0, modelForStart.getCourse(), modelForStart.getCall(),
                UtilsController.periodidInit, UtilsController.getTypeZbyIndex(UtilsController.typezIdInit, 0), "",
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ), false );
        model.addAttribute( "todir", "nplanesdoubl" );
        model.addAttribute( "disabled", true );
        //model.addAttribute( "calc_fysgc", utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ) );
        // 27 07 2022 return "nplanesdoubl";
        return "nplanes";
    }

    @PostMapping("nplanesdoubl")
    public String nplanesdoubl(
            Model model, @AuthenticationPrincipal User user,
            @RequestParam String btn_out,
            @RequestParam int period,
            @RequestParam int year,
            @RequestParam int semestr,
            @RequestParam int facultat,
            @RequestParam int typez,
            @RequestParam int groupp,
            @RequestParam int course,
            @RequestParam int call,
            @RequestParam String calc_fysgc) throws ParseException, IOException {
        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanesdoubl",
                0,
                period,
                year,
                semestr,
                facultat,
                typez,
                groupp,
                course,
                call,
                calc_fysgc,
                false,
                teacher,
                model );
        model.addAttribute( "razdel", "nplanesdoubl" );
        model.addAttribute( "viewcall", true );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        String result = teacherControllerService.calcRasp( model,
                "nplanesdoubl", 0, modelForStart.getCourse(), modelForStart.getCall(),
                period, UtilsController.getTypeZbyIndex(typez, 0), btn_out, calc_fysgc, false );
        model.addAttribute( "todir", "nplanesdoubl" );
        model.addAttribute( "disabled", true );
        return "nplanesdoubl";
    }
//09 07 2022        System.out.println("String nplanes");
//09 07 2022        Teacher teacher = teacherService.findTeacherByUser( user );
//09 07 2022        teacherControllerService.prepareForView(
//09 07 2022                model,
//09 07 2022                "",
//09 07 2022                0,
//09 07 2022                UtilsController.periodidInit,
//09 07 2022                UtilsController.yearIdInit,
//09 07 2022                UtilsController.semestrIdInit,
//09 07 2022                UtilsController.facultatIdInit,
//09 07 2022                UtilsController.groupIdInit,
//09 07 2022                UtilsController.typezIdInit,
//09 07 2022                UtilsController.courseIdInit,
//09 07 2022                UtilsController.callIdInit,
//09 07 2022                //utilsController.getDataNow( new SimpleDateFormat( "dd MMMM yyyy, EEEE" ) ),
//09 07 2022                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
//09 07 2022                "",
//09 07 2022                teacher);
//
//        if (!teacherControllerService.ViewsFacYearSemGroupCourseFacDatePeriodTypezChastBtn(
//                model, UtilsController.facultatIdInit, UtilsController.yearIdInit,
//                UtilsController.semestrIdInit, UtilsController.groupIdInit, UtilsController.courseIdInit,
//                UtilsController.callIdInit, UtilsController.periodidInit, UtilsController.typezIdInit,
//                UtilsController.gpgidInit, UtilsController.dtStartDtEndVisibleInit, teacher)){
//            return "message";
//        }
////25 05 2022        model.addAttribute( "nview", "nplanes" );
////25 05 2022        //return "nplanes";
////25 05 2022
////25 05 2022        model.addAttribute( "namefac", "facultat" );
////25 05 2022        System.out.println("GetMapping nplanes : lastplanes");
////25 05 2022        model.addAttribute( "dates1", new ArrayList<DoubleString>() );
////25 05 2022        model.addAttribute( "dates2", new ArrayList<DoubleString>() );
////25 05 2022        model.addAttribute( "dates3", new ArrayList<DoubleString>() );
////25 05 2022        model.addAttribute( "dates4", new ArrayList<DoubleString>() );
///        model.addAttribute( "todir", "zadaniya" );
///        model.addAttribute( "viewbuttonzagruzit", true );
///        return "zadaniya";
//        model.addAttribute( "todir", "nplanes" );
//        return "nplanes";
//    }

    @GetMapping("/nplanesdoubl/{number}/{name}/{periodId}/{yearId}/{semestrId}/{facultatId}/{typezId}/{grouppId}/{courseId}/{callId}/{calc_fysgc}/{blockdate}/{razdel}")
    public String nplanesdoubl(
            @PathVariable("number") int number,
            @PathVariable("name") String name,
            @PathVariable("periodId") int periodId,
            @PathVariable("yearId") int yearId,
            @PathVariable("semestrId") int semestrId,
            @PathVariable("facultatId") int facultatId,
            @PathVariable("typezId") int typezId,
            @PathVariable("grouppId") int grouppId,
            @PathVariable("courseId") int courseId,
            @PathVariable("callId") int callId,
            @PathVariable("calc_fysgc") String calc_fysgc,
            @PathVariable("blockdate") boolean blockdate,
            @PathVariable("razdel") String razdel,
            Model model, @AuthenticationPrincipal User user) throws ParseException, IOException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                name,
                number,
                periodId,
                yearId,
                semestrId,
                facultatId,
                typezId,
                grouppId,
                courseId,
                callId,
                calc_fysgc,
                blockdate,
                teacher, model );
        model.addAttribute( "razdel", razdel );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );
        if (modelForStart.getCourse()==null){
            List<Course> courses = courseService.findByGroupp(modelForStart.getGroupp());
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "Эту подгруппу распределяет "
                    + courses.get(0).getTeacher().getLastname().getName() + " "
                    + courses.get(0).getTeacher().getFirstname().getName()  + " "
                    + courses.get(0).getTeacher().getSecondname().getName());
            //return "errors";
            model.addAttribute( "viewbuttonzagruzit", true );
            model.addAttribute( "viewbuttonsohranit", false );
            model.addAttribute( "viewdateUdalit", false );
            model.addAttribute( "viewcall", false );
            model.addAttribute( "viewdate", false );
            model.addAttribute( "dirteacher", "nvarlabs" );
            model.addAttribute( "todir", "nplanesdoubl" );
            model.addAttribute("exerciselist", null);
            return "nvarlabs";
        }
        if (razdel.equals( "Задания" )){
            if (modelForStart.getTypeZ().equals("лекция")){
                model.addAttribute( "viewbuttonsohranit", false );
                model.addAttribute( "viewdateUdalit", false );
            } else {
                model.addAttribute("viewbuttonsohranit", true);//false
                model.addAttribute( "viewdateUdalit", true );
                model.addAttribute("text", "Варианты заданий для студентов");
            }

            model.addAttribute( "viewbuttonzagruzit", false );//true
            //model.addAttribute( "viewbuttonsohranit", false );
            model.addAttribute( "viewcall", false );
            model.addAttribute( "viewdate", false );
            //model.addAttribute( "aDateTime", utilsController.getDataNow() );
            model.addAttribute( "dirteacher", "nzadaniyas" );
            //model.addAttribute( "dirteacher", "nplanesdoubl" );
            model.addAttribute( "todir", "nplanesdoubl" );
            //teacherControllerService.findRaspisanie(modelForStart.getCourse());
        }
        if (razdel.equals( "Планы" )) {
            model.addAttribute( "viewcall", true );
            model.addAttribute( "viewdate", true );
            model.addAttribute( "viewbuttonzagruzit", false );
            model.addAttribute( "viewbuttonsohranit", true );
            model.addAttribute( "viewdateUdalit", true );
            model.addAttribute( "dirteacher", "nplanes" );
            model.addAttribute( "todir", "nplanesdoubl" );

            if (name.equals("callId")){
                int lenPeriod = Period.getSemestr(periodId);
                int kolzan = modelForStart.getCourse().getKolzan();
                boolean ok = false;
                if (kolzan <= Period.getSemestr(Period.getListWeekLength()-1)){
                    if (kolzan == lenPeriod) {
                        ok = true;
                    }
                } else {
                    for (int i = 0; i < Period.getListWeekLength(); i++) {
                        int temp = Period.getSemestr(i);
                        if (kolzan == lenPeriod + temp) {
                            ok = true;
                        }
                    }
                }
                if (!ok){
                    model.addAttribute( "messageType", "danger" );
                    model.addAttribute( "message", "Количество пар не совпадает с выбором периода!" );
                    model.addAttribute( "dates1", new ArrayList<DoubleString>() );
                    model.addAttribute( "dates2", new ArrayList<DoubleString>() );
                    model.addAttribute( "dates3", new ArrayList<DoubleString>() );
                    model.addAttribute( "dates4", new ArrayList<DoubleString>() );
                    return "nplanes";
                }
            }
            //List<Raspisanie> raspisanies = teacherControllerService1.findRaspisanie(modelForStart.getCourse()); //null;// raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() );
            //List<DoubleString> dates = raspisanies.stream().map( rasp -> new DoubleString( rasp.getActiondate(), rasp.getCall().getName() ) )
            //        .collect( Collectors.toList() );
            //teacherControllerService.fillDates( dates, model );
            teacherControllerService.calcRasp( model, name, number, modelForStart.getCourse(), modelForStart.getCall(),
                    modelForStart.getPeriodId(), UtilsController.getTypeZbyIndex(typezId, 0), "", calc_fysgc, blockdate );
            return "nplanes";
        }
        if (razdel.equals( "Варианты лаб." )){

            //model.addAttribute( "viewcall", false );
            //model.addAttribute( "viewdate", false );
            //model.addAttribute( "viewdateUdalit", false );
            //model.addAttribute( "viewbuttonzagruzit", true );
            //model.addAttribute( "viewbuttonsohranit", false );
            //model.addAttribute( "activSohranit", true );
            //model.addAttribute("todir", "nplanesdoubl");
            //model.addAttribute( "dirteacher", "nplanesdoubl" );
            //model.addAttribute( "aDateTime", utilsController.getDataNow() );




            model.addAttribute( "viewbuttonzagruzit", true );
            model.addAttribute( "viewbuttonsohranit", false );
            model.addAttribute( "viewdateUdalit", false );
            model.addAttribute( "viewcall", false );
            model.addAttribute( "viewdate", false );
            model.addAttribute( "dirteacher", "nvarlabs" );
            model.addAttribute( "todir", "nplanesdoubl" );
            List<Exercise> exerciseList = verification.showVarLab(modelForStart);
            model.addAttribute("exerciselist", exerciseList);
            return "nvarlabs";
        }
        if (modelForStart.getResult() != null) {
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", modelForStart.getResult() );
            return "nplanesdoubl";
        }
        if (calc_fysgc.equals("undefined")){
            calc_fysgc = utilsController.getDataNow();
        }
        teacherControllerService.calcRasp( model, name, number, modelForStart.getCourse(), modelForStart.getCall(),
                modelForStart.getPeriodId(), UtilsController.getTypeZbyIndex(typezId, 0), "", calc_fysgc, blockdate );
        //model.addAttribute( "tdatas", raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() ) );
        return "nplanesdoubl";
    }

    @GetMapping("/nvarlabs")
    public String nvarlabsget(@AuthenticationPrincipal User user,
                              Model model){
        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanes",
                0,
                UtilsController.periodidInit,
                UtilsController.yearIdInit,
                UtilsController.semestrIdInit,
                UtilsController.facultatIdInit,
                UtilsController.typezIdInit,
                UtilsController.groupIdInit,
                UtilsController.courseIdInit,
                UtilsController.callIdInit,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "Варианты лаб." );
        model.addAttribute( "viewcall", false );
        model.addAttribute( "viewdate", false );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", true );
        model.addAttribute( "viewbuttonsohranit", false );
        model.addAttribute( "activSohranit", true );
        model.addAttribute("todir", "nplanesdoubl");
        model.addAttribute( "dirteacher", "nplanesdoubl" );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );
        List<Exercise> exerciseList = verification.showVarLab(modelForStart);
        model.addAttribute("exerciselist", exerciseList);
        return "nvarlabs";
    }

    @PostMapping("/nvarlabs")
    public String nvarlabs(@AuthenticationPrincipal User user,
                           Model model,
                           //@RequestParam String btn_out,
                           @RequestParam int period,
                           @RequestParam int year,
                           @RequestParam int semestr,
                           @RequestParam int facultat,
                           @RequestParam int typez,
                           @RequestParam int groupp,
                           @RequestParam int course,
                           //@RequestParam int call,
                           @RequestParam("file") MultipartFile file) throws IOException {
        try {
            Teacher teacher = teacherService.findTeacherByUser(user);
            ModelForStart modelForStart = teacherControllerService.prepareStart("nvarlabs", 0, period, year,
                    semestr, facultat, typez, groupp, course, 0,
                    utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")),
                    false, teacher, model);
            model.addAttribute("razdel", "Варианты лаб.");
            model.addAttribute("viewcall", false);
            model.addAttribute("viewdate", true);
            model.addAttribute("viewdateUdalit", false);
            model.addAttribute("viewbuttonzagruzit", true);
            model.addAttribute("viewbuttonsohranit", false);
            model.addAttribute("activSohranit", true);
            model.addAttribute("todir", "nplanesdoubl");
            model.addAttribute("dirteacher", "nplanesdoubl");
            model.addAttribute("aDateTime", utilsController.getDataNow());
            if (file.getOriginalFilename().equals("")) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "Не выбрано имя файла!");
                return "nvarlabs";
            }
            if (((file.getOriginalFilename().contains("лек")) && typez != 0)
                    || ((file.getOriginalFilename().contains("лаб")) && typez != 1)) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "Выбранный файл не соответствует типу занятий!");
                model.addAttribute("exerciselist", new ArrayList<>());
                return "nvarlabs";
            }
            String nameGroupp = modelForStart.getGroupp().getNamegroupp();
            int indexGrp = nameGroupp.indexOf(".");
            if (indexGrp >= 0) {
                nameGroupp = nameGroupp.substring(0, indexGrp);
            }
            if (!file.getOriginalFilename().contains(nameGroupp)) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "Выбранный файл \"" + file.getOriginalFilename() + "\" не не для той группы!");
                model.addAttribute("exerciselist", new ArrayList<>());
                return "nvarlabs";
            }
            String fullname = teacherControllerService.getPath(file.getOriginalFilename());
            //Проверка файла на корректность
            String result = verification.readDataFromLekLab(fullname);
            if (result != null) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", result);
                return "nvarlabs";
            }

            verification.makeVarLab(fullname, modelForStart);
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Файл \"" + fullname + "\" полностью обработан");
            List<Exercise> exerciseList = verification.showVarLab(modelForStart);
            model.addAttribute("exerciselist", exerciseList);

            return "nvarlabs";
        } catch (Exception e){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", e.getMessage());
            return "errors";
        }
    }

    @PostMapping("/nzadaniyas")
    public String nzadaniyas(@AuthenticationPrincipal User user,
                             Model model,
                             @RequestParam int period,
                             @RequestParam int year,
                             @RequestParam int semestr,
                             @RequestParam int facultat,
                             @RequestParam int typez,
                             @RequestParam int groupp,
                             @RequestParam int course, MultipartFile file) throws IOException {
        //@RequestParam int call,
        //@RequestParam("file") MultipartFile file) throws IOException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        //model.addAttribute( "todir", "nplanesdoubl" );
        ModelForStart modelForStart = teacherControllerService.prepareStart( "nzadaniyas", 0, period, year,
                semestr, facultat, typez, groupp, course, 0,//call,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "zadaniya" );
        model.addAttribute( "viewcall", false );
        model.addAttribute( "viewdate", false );//true
        if (modelForStart.getTypeZ().equals("лекция")){
            model.addAttribute( "viewbuttonsohranit", false );
            model.addAttribute( "viewdateUdalit", false );
        } else {
            model.addAttribute("viewbuttonsohranit", true);//false
            model.addAttribute( "viewdateUdalit", true );
            model.addAttribute("text", "Варианты заданий для студентов");
        }

        //model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );//true
        //model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        //String path = teacherControllerService1.getPath( file.getOriginalFilename() );
        //27 08 2022 String result = teacherControllerService.readDataFromLekLab( path );//, modelForStart );
        //27 08 2022 if (result != null) {
        //27 08 2022     model.addAttribute( "dates", new ArrayList<>() );
        //27 08 2022     model.addAttribute( "messageType", "danger" );
        //27 08 2022     model.addAttribute( "message", result );
        //27 08 2022 } else {
        //27 08 2022     teacherControllerService.fillDataFromLekLab( path, modelForStart, model );
        //27 08 2022 }
        String res = teacherControllerService.fillZadaniya(themeService.findThemesByCourse_IdAndTypezan(modelForStart.getCourse(),
                modelForStart.getTypeZ().substring(0, 3)));
        model.addAttribute( "dirteacher", "nzadaniyas" );
        model.addAttribute( "todir", "nzadaniyas" );
        //model.addAttribute( "tdatas", raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() ) );
        List<Raspisanie> raspisanieList = teacherControllerService.findRaspisanie( modelForStart.getCourse(), null, null );
        model.addAttribute( "tdatas",  raspisanieList);
        if (res != null){
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", res );
        } else {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Сохранение завершено!");
        }
        return "nzadaniyas";
    }
    @PostMapping("/nplanes")
    public String nplanes(@AuthenticationPrincipal User user,
                          Model model,
                          @RequestParam String btn_out,
                          @RequestParam int period,
                          @RequestParam int year,
                          @RequestParam int semestr,
                          @RequestParam int facultat,
                          @RequestParam int typez,
                          @RequestParam int groupp,
                          @RequestParam int course,
                          @RequestParam int call,
                          @RequestParam String calc_fysgc) throws ParseException, IOException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanes",
                0,
                period,
                year,
                semestr,
                facultat,
                typez,
                groupp,
                course,
                call,
                calc_fysgc,
                false,
                teacher,
                model );
        model.addAttribute( "razdel", "Планы" );
        model.addAttribute( "viewcall", true );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "viewdateUdalit", true );
        if (btn_out.equals("save")){
            model.addAttribute("activSohranit", false);
        } else{
            model.addAttribute("activSohranit", true);
        }
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        String result = teacherControllerService.calcRasp( model,
                "nplanes", 0, modelForStart.getCourse(), modelForStart.getCall(),
                period, utilsController.getTypeZbyIndex(typez, 0), btn_out, calc_fysgc, false );
        model.addAttribute( "todir", "nplanesdoubl" );
        //model.addAttribute( "disabled", true );

        return "nplanes";
    }

    private void calculate(@AuthenticationPrincipal User user,
                           Model model,
                           int period,
                           int year,
                           int semestr,
                           int facultat,
                           int typez,
                           int groupp,
                           int course,
                           int call,
                           String calc_fysgc) throws ParseException {
//        Year year1 = teacherControllerService.getYear( model, year );
//        Semestr semestr1 = teacherControllerService.getSemestr1( model, semestr );
//        Facultat facultat1 = teacherControllerService.getFacultat1( model,facultat );
//        teacherControllerService.getTypeZ1( model, typez );

        Teacher teacher = teacherService.findTeacherByUser( user );
        teacherControllerService.prepareForView( model, "", 0, period, year,
                semestr, facultat, typez, groupp, course, call, calc_fysgc, "", teacher );

    }

    @GetMapping("/datescalls/{parm}/{periodId}/{yearId}/{semestrId}/{facultatId}/{typezId}/{grouppId}/{courseId}/{callId}/{calc_fysgc}")
    public String datescalls(
            @PathVariable("parm") String parm,
            @PathVariable("periodId") int periodId,
            @PathVariable("yearId") int yearId,
            @PathVariable("semestrId") int semestrId,
            @PathVariable("facultatId") int facultatId,
            @PathVariable("typezId") int typezId,
            @PathVariable("grouppId") int grouppId,
            @PathVariable("courseId") int courseId,
            @PathVariable("callId") int callId,
            @PathVariable("calc_fysgc") String calc_fysgc,
            Model model, @AuthenticationPrincipal User user) throws ParseException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "nplanesdoubl",
                0,
                periodId,
                yearId,
                semestrId,
                facultatId,
                typezId,
                grouppId,
                courseId,
                callId,
                calc_fysgc,
                false,
                teacher,
                model );
        model.addAttribute( "razdel", "datescalls" );
        model.addAttribute( "viewcall", true );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", false );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

//        String result = teacherControllerService1.calcRasp(model,
//                "nplanesdoubl", 0, modelForStart.getCourse(), modelForStart.getCall(),
//                periodId, "delete", calc_fysgc, false);
//        model.addAttribute( "viewbuttonzagruzit", false );
//        model.addAttribute( "viewdatecall", true );
//        model.addAttribute( "todir", "zadaniya" );
//        model.addAttribute( "disabled", true );

//13 07 2022        String res = teacherControllerService.prepareForView( model, "dates", 0, periodId, yearId,
//13 07 2022                semestrId, facultatId, typezId, grouppId, courseId, callId, calc_fysgc, "", teacher );
//13 07 2022        String ppp = "'1  20-05-2022  11:20-12:50'";
//13 07 2022        String ppp2 = "1    20-05-2022   11:20-12:50";

        //String[] res = parm.trim().replaceAll("\\s", " ").split( " " );
        //String res1 = parm.replaceAll("\\s","");
        //String res2 = parm.replaceAll("[\\xA0]+", " ");
        //String res3 = res2.replaceAll("\\^ +| +|( ) +$","$1");
        //String res4 = res2.replaceAll("\\^ +| +| +$"," ");
        //String[] res = res4.split( " " );

        String[] fromView = parm.replaceAll( "[\\xA0]+", " " )
                .replaceAll( "//^ +| +| +$", " " ).split( " " );
        String newdate = fromView[2];
        String newcall = fromView[3];
        if (newdate.length() > 5 && newcall.length() > 5) {
            List<Call> calls = callService.findAll();
            Call call = callService.findByName( newcall );
            int indexcall = calls.indexOf( call );
            model.addAttribute( "callId", indexcall );
            model.addAttribute( "tdate", newdate );
            List<Raspisanie> raspisanies = null;// raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() );
            List<DoubleString> dates = raspisanies.stream().map( rasp -> new DoubleString( rasp.getActiondate(), rasp.getCall().getName() ) )
                    .collect( Collectors.toList() );
            teacherControllerService.fillDates( dates, model );
        }
        model.addAttribute( "todir", "delete" );
        model.addAttribute( "disabled", true );
        return "zadaniya";
    }

    @GetMapping("/next/{number}/{name}/{periodId}/{yearId}/{semestrId}/{facultatId}/{typezId}/{grouppId}/{courseId}/{callId}/{calc_fysgc}")
    public String next(
            @PathVariable("number") int number,
            @PathVariable("name") String name,
            @PathVariable("periodId") int periodId,
            @PathVariable("yearId") int yearId,
            @PathVariable("semestrId") int semestrId,
            @PathVariable("facultatId") int facultatId,
            @PathVariable("typezId") int typezId,
            @PathVariable("grouppId") int grouppId,
            @PathVariable("courseId") int courseId,
            @PathVariable("callId") int callId,
            @PathVariable("calc_fysgc") String calc_fysgc,
            Model model, @AuthenticationPrincipal User user) throws ParseException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        System.out.println( "start  String next" );
        System.out.println( " name=" + name + " number=" + number + " periodId=" + periodId + " yearId=" + yearId
                + " semestrId=" + semestrId + " facultatId=" + facultatId + " typezId=" + typezId
                + " grouppId=" + grouppId + " courseId=" + courseId );
        String res = teacherControllerService.prepareForView( model, name, number, periodId, yearId,
                semestrId, facultatId, typezId, grouppId, courseId, callId, calc_fysgc, "", teacher );
        System.out.println( "PostMapping next : lastplanes" );
        if (res != null) {
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", res );
        }

        return "zadaniya";
    }

    @GetMapping("/zadaniya")
    public String zadaniya(@AuthenticationPrincipal User user, Model model) {
        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart(
                "",
                0,
                UtilsController.periodidInit,
                UtilsController.yearIdInit,
                UtilsController.semestrIdInit,
                UtilsController.facultatIdInit,
                UtilsController.typezIdInit,
                UtilsController.groupIdInit,
                UtilsController.courseIdInit,
                UtilsController.callIdInit,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "Задания" );
        model.addAttribute( "viewcall", false );//true
        model.addAttribute( "viewdate", false );//true

        model.addAttribute( "viewbuttonzagruzit", false );//true
        if (modelForStart.getTypeZ().equals("лекция")){
            model.addAttribute( "viewbuttonsohranit", false );
            model.addAttribute( "viewdateUdalit", false );
        } else {
            model.addAttribute("viewbuttonsohranit", true);//false
            model.addAttribute( "viewdateUdalit", true );
        }
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        List<Raspisanie> raspisanies = //teacherControllerService1.findRaspisanie(modelForStart.getCourse(), null, null);
                raspisanieService.findAllByCourse(modelForStart.getCourse());
        //raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() );

        if (raspisanies == null) {
            raspisanies = new ArrayList<>();
        }
        model.addAttribute( "tdatas", raspisanies );
        model.addAttribute( "todir", "nplanesdoubl" );
        model.addAttribute( "dirteacher", "zadaniya" );
        //22 07b2022 model.addAttribute( "todir", "nzadaniyas" );
        //teacherControllerService1.getDatesFromDB( modelForStart.getCourse(), model );
        //teacherControllerService.fillDates(new ArrayList<>(), model);
        return "zadaniya";
    }

    @PostMapping("delete")
    public String delete(
            @RequestParam int period,
            @RequestParam int year,
            @RequestParam int semestr,
            @RequestParam int facultat,
            @RequestParam int typez,
            @RequestParam int groupp,
            @RequestParam int course,
            @RequestParam int call,
            @RequestParam String calc_fysgc,
            //@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file, Model model
            @AuthenticationPrincipal User user, Model model

    ) throws IOException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        ModelForStart modelForStart = teacherControllerService.prepareStart( "zadainya", 0, period, year,
                semestr, facultat, typez, groupp, course, call,
                calc_fysgc,
                false, teacher, model );
        model.addAttribute( "razdel", "delete" );
        model.addAttribute( "viewcall", true );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        teacherControllerService.calcRasp( model, "", 0, modelForStart.getCourse(), modelForStart.getCall(),
                period, UtilsController.getTypeZbyIndex(typez, 0), "delete", calc_fysgc, false );
        model.addAttribute( "todir", "nplanesdoubl" );
        return "zadaniya";
    }

    @PostMapping("/zadaniya")
    public String zadainya(
            @RequestParam int period,
            @RequestParam int year,
            @RequestParam int semestr,
            @RequestParam int facultat,
            @RequestParam int typez,
            @RequestParam int groupp,
            @RequestParam int course,
            @RequestParam("file") MultipartFile file,
            //@RequestParam String btn_out,
            @AuthenticationPrincipal User user, Model model
            //@AuthenticationPrincipal User user, Model model

    ) throws IOException {

        Teacher teacher = teacherService.findTeacherByUser( user );

        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", true );
        //model.addAttribute( "todir", "nplanesdoubl" );
        model.addAttribute( "todir", "zadaniya" );
        ModelForStart modelForStart = teacherControllerService.prepareStart( "zadainya", 0, period, year,
                semestr, facultat, typez, groupp, course, 0,
                utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ),
                false, teacher, model );
        model.addAttribute( "razdel", "zadaniya" );
        model.addAttribute( "viewcall", false );
        model.addAttribute( "viewdate", true );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "viewbuttonsohranit", true );
        model.addAttribute( "activSohranit", true );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );

        if (!file.getOriginalFilename().equals( "" )) {
            String path = teacherControllerService.getPath( file.getOriginalFilename() );

            //Проверка файла на корректность
            //String result = teacherControllerService.readDataFromLekLab( path, modelForStart );
            //if (result != null) {
            //    model.addAttribute( "dates", new ArrayList<>() );
            //    model.addAttribute( "messageType", "danger" );
            //    model.addAttribute( "message", result );
            //} else {
            //    //Заготовка для формирования заданий
            //    //teacherControllerService.fillDataFromLekLab( path, modelForStart, model );
            //}
        } else {//Сохранить данные в базу

        }
        model.addAttribute( "dirteacher", "zadaniya" );

        model.addAttribute( "disabled", false );
        return "zadaniya";
    }



    @GetMapping("/nextZadaniya/{number}/{name}/{periodId}/{yearId}/{semestrId}/{facultatId}/{typezId}/{grouppId}/{courseId}/{callId}/{calc_fysgc}")
    public String nextZadaniya(
            @PathVariable("number") int number,
            @PathVariable("name") String name,
            @PathVariable("periodId") int periodId,
            @PathVariable("yearId") int yearId,
            @PathVariable("semestrId") int semestrId,
            @PathVariable("facultatId") int facultatId,
            @PathVariable("typezId") int typezId,
            @PathVariable("grouppId") int grouppId,
            @PathVariable("courseId") int courseId,
            @PathVariable("callId") int callId,
            @PathVariable("calc_fysgc") String calc_fysgc,
            Model model, @AuthenticationPrincipal User user) throws ParseException {

        Teacher teacher = teacherService.findTeacherByUser( user );
        model.addAttribute( "viewdateUdalit", false );
        model.addAttribute( "viewbuttonzagruzit", false );
        model.addAttribute( "todir", "nextZadaniya" );
        ModelForStart res = teacherControllerService.prepareStart( name, number, periodId, yearId,
                semestrId, facultatId, typezId, grouppId, courseId, callId, calc_fysgc,
                false, teacher, model );
        return "zadaniya";
    }

    @GetMapping("/students")
    public String students(Model model, @AuthenticationPrincipal User user) throws ParseException {

        Teacher teacher = teacherService.findTeacherByUser(user);
        List<Startdata> startdataList = startdataService.findStartdataByRole("STUDENT");
        int index = 0;
        for (Startdata startdata :startdataList) {
            User user1 = new User();
            user1.setUsername(startdata.getEmail());
            user1.setPassword("Q!123456q");
            if (index == 22){
                int iiu = 0;
            }
            index++;
            userService.addUser(user1, startdata, true);
            Student student = studentService.saveIntoStudent(startdata.getFirstname(), startdata.getSecondname(), startdata.getLastname(),
                    startdata.getGroupp(),user1);
        }
        return "students";
    }

    @GetMapping("/proverka")
    public String proverka(HttpSession session, Model model, @AuthenticationPrincipal User user//, @RequestParam String pathstudent
    ) throws ParseException, IOException {
        //
        String forProverka = "forProverka";
        String value = (String) session.getAttribute("startProverka");
        if (value != null && value.equals("true")){
            String startProverkaNamefile = (String) session.getAttribute("startProverkaNamefile");
            model.addAttribute( "messageType", "success" );
            if (startProverkaNamefile != null){
                model.addAttribute( "message", "Ответ преподавателя в файле " + startProverkaNamefile );
            } else {
                forProverka = "Ответ преподавателя готов к отправке";
                model.addAttribute("message", forProverka);
            }
        }

        if (value != null && value.equals("occupied")){
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "Существуют недоредактированные файлы в папке \"" + pathantwer + "\"" );
        }
        String dttm = utilsController.getDataNow(new SimpleDateFormat("dd-MM-yyyy HH:mm") );
        model.addAttribute("aDateTime", dttm);
        teacherControllerService.proverka(model, user, dttm, forProverka);
        File uploadDir = new File(pathantwer);
        String[] list = uploadDir.list();
        //Проверить в рабочей папке PATH_ANSWER один или два файла
        //Если файл один, отправить его на сервер для отправки пользователю и очистить папку
        //Если два файла - значит редактирование не закончено
        if (list.length == 2) {
            model.addAttribute("buttonvisible", "false");
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "Существуют недоредактированные файлы в папке \"" + pathantwer + "\"" );
        } else {
            model.addAttribute("buttonvisible", "true");
            if (list.length == 1) {
                sendClear(list[0], session);
                //filesForDnevnik.setDatecontrol(tempDate);
                //filesForDnevnikService.
            }
        }
        return "proverka";
    }
    //отправить файл на сервер для предоставления пользователю и очистить папку
    private void sendClear(String parm, HttpSession session) throws IOException {
        String[] list = parm.replaceAll("\\s+", " ").split(" ");
        Long index = Long.parseLong(list[0]);
        long lenOld = Long.parseLong(list[1]);
        String filename = pathantwer + list[2];
        File control = new File(pathantwer + parm);
        long lenNew = control.length();
        //Отправить файл на сервер
        //21 12 22 String filenameOut = uploadPath + "\\Answers\\" + parm;
        String filenameOut = uploadPath + "\\Answers\\" + list[0] + " " + list[2] + ".docx";
        FileUtils.copyFile(control, new File(filenameOut));// filenameOut == куда
        control.delete();
        session.setAttribute("startProverkaNamefile", filenameOut);
        FilesForDnevnik filesForDnevnik = filesForDnevnikService.findById(index);
        if (lenOld == lenNew){
            //Ошибок больше нет, отчет принят
            filesForDnevnik.setOcenka("Проверено, замечаний нет.");
            filesForDnevnik.setStatus("отправил студенту");
        } else {
            filesForDnevnik.setOcenka("");
            filesForDnevnikService.update(filesForDnevnik);
        }
        String date = utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ));
        filesForDnevnik.setDatecontrol(date);
        filesForDnevnik.setPathteacher(filenameOut);
        filesForDnevnikService.update(filesForDnevnik);
        String nameForFinde = filesForDnevnik.getPathstudent();
        int first = nameForFinde.lastIndexOf("~")+1;
        int second = nameForFinde.lastIndexOf(".docx");
        String tempForFinde1 = nameForFinde.substring(first, second);
        String tempForFinde = nameForFinde.substring(nameForFinde.lastIndexOf("~")+1, nameForFinde.lastIndexOf(".docx"));
        int tempForFindeInt = Integer.parseInt(tempForFinde);
        List<FilesForDnevnik> forDnevnikList = filesForDnevnikService.findFilesForDnevnikByKtocdalAndDatesdachiAndDnevnik(filesForDnevnik.getKtocdal(),
                date, filesForDnevnik.getDnevnik());
        for (FilesForDnevnik temp : forDnevnikList){
            nameForFinde = temp.getPathstudent();
            String str = nameForFinde.substring(nameForFinde.lastIndexOf("~")+1, nameForFinde.lastIndexOf(".docx"));
            int strNumb = Integer.parseInt(str);
            if (strNumb < tempForFindeInt){
                temp.setOcenka("Не отвечал");
                filesForDnevnikService.update(temp);
            }
        }
        int ooo=0;
    }

    @PostMapping("/proverka")
    public String proverka(HttpSession session, Model model,
                           @RequestParam String[] pathstudent,
                           @RequestParam String[] course,
                           @RequestParam String[] theme,
                           @RequestParam String[] filesfordnevnik,
                           @RequestParam int btn_out) throws Exception {
        //Проверить в рабочей папке PATH_ANSWER один или два файла
        //Если файл один, отправить его на сервер для отправки пользователю и очистить папку
        //Если два файла - значит редактирование не закончено
        //File uploadDir = new File(PATH_ANSWER);
        //String[] list = uploadDir.list();
        //if (list.length > 0){
        //    session.setAttribute("startProverka", "occupied");
        //    return "redirect:/teacher1/proverka";
        //}
        Long index =  Long.parseLong(filesfordnevnik[btn_out]);
        FilesForDnevnik filesForDnevnik = filesForDnevnikService.findById(index);
        String sem = filesForDnevnik.getDnevnik().getRaspisanie().getCourse().getGroupp().getSemestr().getName();
        String semestr = "Vesna";
        if ("13579".indexOf(sem) >= 0){
            semestr = "Osen";
        }
        String grp = filesForDnevnik.getDnevnik().getRaspisanie().getCourse().getGroupp().getOnlynamegroupp();
        switch (grp) {
            case "310" : grp = "\\3 к ИВТ\\1 sem lab Sety"; break;
            case "391" : grp = "\\3 к ИБ\\1 sem lab Sety"; break;
            case "291" : grp = "\\2 к ИБ\\4 sem VychSety\\4 sem lab VychSety"; break;
        }
        Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
                filesForDnevnik.getDnevnik().getStudent().getFirstname(),
                filesForDnevnik.getDnevnik().getStudent().getSecondname(),
                filesForDnevnik.getDnevnik().getStudent().getLastname()
        );
        List<Exercise> exerciseList = exerciseService.findAllByTheme_AndStartdata(
                filesForDnevnik.getDnevnik().getRaspisanie().getTheme(), startdata);
        String leklab = pathstudent[btn_out].substring(pathstudent[btn_out].lastIndexOf("\\")+1, pathstudent[btn_out].indexOf("~"));
        session.setAttribute("startProverka", "true");
        String nameFile = pathstudent[btn_out].substring(pathstudent[btn_out].indexOf("Upload")+7);
        String zadanie = uploadPath.substring(0, uploadPath.lastIndexOf("\\")) + "\\" + basepath + "\\"
                + semestr + grp + "\\"
                + filesForDnevnik.getDnevnik().getRaspisanie().getCourse().getGroupp().getOnlynamegroupp()
                + " exercise_seti_lab"//nameFile.substring(0, nameFile.indexOf("\\")) + " seti_lab"
                + leklab.substring(1) + ".txt";
        //String zadanie1 = nameFile.substring(0, nameFile.indexOf("\\")) + " seti_lab" + leklab.substring(1) + ".txt";

        Reader reader1 = new FileReader( zadanie );
        BufferedReader buffReader1 = new BufferedReader( reader1 );

        String answ = nameFile;
        String filenameOut = pathantwer + answ.replaceAll("\\\\", "   ");
        //Пересылаем первый файл
        FileUtils.copyFile(new File(pathstudent[btn_out]), new File(filenameOut));// filenameOut == куда
        nameFile = nameFile.substring(0, nameFile.lastIndexOf("\\")).replaceAll("\\\\", "        ")+ leklab.substring(1);
        answ = answ.replaceAll("\\\\", "_");
        String namefileFound = filesfordnevnik[btn_out] + " " + answ.substring(0, answ.indexOf("~")) + ".docx";
        File uploadDir = new File(uploadPath + "\\Answers");
        String[] list = uploadDir.list();
        boolean found = false;
        for (String st : list){
            if (st.equals(namefileFound)){
                found = true;
                break;
            }
        }
        int indexAddMyParagr = 0;
        answ = pathantwer + filesfordnevnik[btn_out] + " " + answ.substring(0, answ.indexOf("~")) + ".docx";
        String inpUot = answ;// pathantwer + filesfordnevnik[btn_out] + " " + nameFile + ".docx";
        if (found){
            inpUot = uploadDir + "\\" + namefileFound;
            indexAddMyParagr = updateDocument.getparagraphs(inpUot) - 1;
        } else {
            String pth = uploadPath + "\\N_Empty.docx";
            updateDocument.addMyParagr(pth, inpUot, new DoubleString(nameFile.replaceAll("_", " "), "10,CENTER"), 0);
            updateDocument.addMyParagr(inpUot, inpUot, new DoubleString("Замечания", "16,CENTER"), 1);
            updateDocument.addMyParagr(inpUot, inpUot, new DoubleString("По теме: \"" + theme[btn_out] + "\"", "12,BOTH"), 2);
            updateDocument.addMyParagr(inpUot, inpUot, new DoubleString("Курса: \"" + course[btn_out] + "\"", "12,BOTH"), 3);
            updateDocument.addMyParagr(inpUot, inpUot, new DoubleString("Задание:", "14,CENTER"), 4);
            indexAddMyParagr = 5;

            List<DoubleString> outText = new ArrayList<>();
            outText.add(new DoubleString(nameFile.replaceAll("_", " "), "10,center"));
            outText.add(new DoubleString("Замечания", "20,center"));
            outText.add(new DoubleString("По теме: \"" + theme[btn_out] + "\"", "18,center"));
            outText.add(new DoubleString("Курса: \"" + course[btn_out] + "\"", "18,center"));
            outText.add(new DoubleString("Задание:", "16,start"));
            while (buffReader1.ready()) {
                String line = buffReader1.readLine();
                String res = "";
                int indexWhile = 0;
                while ((indexWhile = line.indexOf("${")) >= 0) {
                    res = res + line.substring(0, indexWhile);
                    line = line.substring(indexWhile);
                    String key = line.substring(2, line.indexOf("}"));
                    String body = "";
                    for (Exercise exercise : exerciseList) {
                        if (exercise.getKey().equals(key)) {
                            body = exercise.getBody();
                        }
                    }
                    res = res + body;
                    //outText.add(new DoubleString(body, "16,start"));
                    updateDocument.addMyParagr(inpUot, inpUot, new DoubleString(body, "12,BOTH"), indexAddMyParagr);
                    indexAddMyParagr++;
                    line = line.substring(line.indexOf("}") + 1);
                    if (line.indexOf("${") < 0) {
                        res = res + line;
                        line = "";
                    }
                }
                //outText.add(new DoubleString(res, "16,start"));
            }
        }
        updateDocument.addMyParagr(inpUot, inpUot, new DoubleString(
                utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")), "12,BOTH"), indexAddMyParagr);
        indexAddMyParagr++;
        updateDocument.addMyParagr(inpUot, inpUot, new DoubleString("", "12,BOTH"), indexAddMyParagr);
        if (found){
            //Пересылаем файл
            FileUtils.copyFile(new File(inpUot), new File(answ));// answ == куда
        }
        //String tempDate = utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) );
        //outText.add(new DoubleString(tempDate, "14,start"));
        //outText.add(new DoubleString("", "14,start"));
        //filesForDnevnik.setDatecontrol(tempDate);
        //filesForDnevnikService.
//        answ = answ.replaceAll("\\\\", "_");
//        //answ = PATH_ANSWER + filesfordnevnik + " " + len + " " + answ.substring(0, answ.indexOf("~")) + ".docx";
//        answ = pathantwer + filesfordnevnik[btn_out] + " " + answ.substring(0, answ.indexOf("~")) + ".docx";
////////        CreateDocument.start(answ, outText);
        File control1 = new File(answ);
        long len1 = control1.length();
        int pos = answ.indexOf(" ");
        answ = answ.substring(0, pos) + " " + len1 + answ.substring(pos);
        File control = new File(answ);
        control1.renameTo(control);
        long len = control.length();
        //EditWord.createDocument();
        //EditWord.addPicture();
        return "redirect:/teacher1/proverka";
        //return "poverka";
    }
    @GetMapping("/viewProverka")
    public String viewProverka( @RequestParam String pathstudent){
        return "viewProverka";
    }
    @GetMapping("/galka/{st}")
    public String galka(HttpSession session,  @PathVariable("st") String st){//Галка установлена
        List<String> galka = (List<String>) session.getAttribute("galka");
        if (!galka.contains(st)) {
            galka.add(st);
        }
        session.setAttribute("galka", galka);
        return "redirect:/teacher1";
    }
}