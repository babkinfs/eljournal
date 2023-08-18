package com.babkin.eljournal.service;

import com.babkin.eljournal.controllers.UtilsController;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MainControllerService {

    @Autowired
    private TeacherControllerService teacherControllerService1;
    @Autowired
    private SubgrouppService subgrouppService;
    @Autowired
    private RaspisanieService raspisanieService;

    //public static final String VESNA = "весна";
    //public static final String OSEN = "осень";

    private YearService yearService;
    private GroupService groupService;
    private LastnameService lastnameService;
    private FirstnameService firstnameService;
    private SecondnameService secondnameService;
    private TeacherService teacherService;
    private StudentService studentService;
    private UserService userService;
    private CourseService courseService;
    private CallService callService;
    private SemestrService semestrService;
    private FacultatService facultatService;
    private StartdataService startdataService;
    private UtilsController utilsController;

    public MainControllerService(YearService yearService,
                                 GroupService groupService,
                                 LastnameService lastnameService,
                                 FirstnameService firstnameService,
                                 SecondnameService secondnameService,
                                 TeacherService teacherService,
                                 StudentService studentService,
                                 UserService userService,
                                 CourseService courseService,
                                 CallService callService,
                                 SemestrService semestrService,
                                 FacultatService facultatService,
                                 StartdataService startdataService
    ) {
        this.yearService = yearService;
        this.groupService = groupService;
        this.lastnameService = lastnameService;
        this.firstnameService = firstnameService;
        this.secondnameService = secondnameService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
        this.callService = callService;
        this.semestrService = semestrService;
        this.facultatService = facultatService;
        this.startdataService = startdataService;
    }

    public String[] fullYearsGroupps(String teacher, String student, String course) throws Exception {

        List<String> yearsDop = new ArrayList<>();
        List<String> groupsDop = new ArrayList<String>();
        List<String> facsDop = new ArrayList<>();
        List<String> frmsDop = new ArrayList<String>();
        List<String> semDop = new ArrayList<String>();

        String[] res = new String[3];
        String nameControl =  "";//UtilsController.getPath() + "\\" + "control.txt";
        //System.out.println("MainControllerService::fullYearsGroupps: Читаем файл:" + nameControl);
        res[0] = nameControl;
        Reader reader0 = new FileReader( nameControl );
        BufferedReader buffReader0 = new BufferedReader( reader0 );
        int index = 0;
        while (buffReader0.ready()) {
            String line = buffReader0.readLine().trim().replaceAll( "\\s{2,}", " " );
            String[] parm = line.split( "~" );
            for (String st : parm) {
                switch (index) {
                    case 0:
                        yearsDop.add( st );
                        break;
                    case 1:
                        groupsDop.add( st );
                        break;
                    case 2:
                        facsDop.add( st );
                        break;
                    case 3:
                        frmsDop.add( st );
                        break;
                    case 4:
                        semDop.add( st );
                        break;
                }
            }
            index++;
        }

        List<String> years = new ArrayList<String>();
        List<String> groups = new ArrayList<String>();
        List<String> facs = new ArrayList<String>();

        //Reader reader2 = new FileReader( UtilsController.getPath() + "\\" + student + ".txt" );
        Reader reader2 = new FileReader( student + ".txt" );
        //System.out.println("MainControllerService::fullYearsGroupps: Читаем файл:" + "FillDB\\" + student + ".txt");
        res[1] = "";//UtilsController.getPath() + "\\" + student + ".txt";
        BufferedReader buffReader2 = new BufferedReader( reader2 );
        while (buffReader2.ready()) {
            String line = buffReader2.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (line.contains( "~" )) {
                if (!line.substring( 0, 2 ).equals( "\uFEFF#" )) {
                    String[] st = line.split( "~" );
                    //String[] test = st[5].split( "-" );
                    if (checkStrings( yearsDop, st[5] )) {
                        if (!years.contains( st[5] )) {
                            years.add( st[5] );
                        }
                    } else {
                        throw new Exception( "Exception: Ошибочный ГОД в файле " + student + ".txt строка: " + line );
                    }
                    if ((checkStrings( groupsDop, st[4] )) && (checkStrings( facsDop, st[6] )) && (checkStrings( frmsDop, st[7] ))) {
//14.08.2021   под вопросом                     if (!groups.contains( st[3] + "~" + st[4] + "~" + st[5] + "~" + st[6] + "~" + st[7] )) {
//14.08.2021                            groups.add( st[3] + "~" + st[4] + "~" + st[5] + "~" + st[6] + "~" + st[7] );
//14.08.2021                        }

                    } else {
                        throw new Exception( "Exception: Ошибочная ГРУППА, ФАКУЛЬТЕТ или ФОРМА ОБУЧЕНИЯ в файле " + student + ".txt строка: " + line );
                    }
                }
            }
        }
        reader2.close();
        buffReader2.close();

        //Reader reader3 = new FileReader( UtilsController.getPath() + "\\" + course + ".txt" );
        Reader reader3 = new FileReader( course + ".txt" );
        //System.out.println("MainControllerService::fullYearsGroupps: Читаем файл:" + UtilsController.getPath() + "\\" + course + ".txt");
        res[2] = "";//UtilsController.getPath() + "\\" + course + ".txt";
        BufferedReader buffReader3 = new BufferedReader( reader3 );
        while (buffReader3.ready()) {
            String line = buffReader3.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (line.contains( "~" )) {
                if (!line.substring( 0, 1 ).equals( "#" )) {
                    String[] st = line.split( "~" );
                    if (checkStrings( yearsDop, st[2] )) {
                        if (!years.contains( st[2] )) {
                            years.add( st[2] );
                        }
                    } else {
                        throw new Exception( "Exception: Ошибочный ГОД  в файле " + course + ".txt строка: " + line );
                    }
                    if ((!checkStrings( groupsDop, st[3] )) || (!checkStrings( facsDop, st[6] )) || (!checkStrings( frmsDop, st[7] ))) {
                        throw new Exception( "Exception: Ошибочная ГРУППА или ФАКУЛЬТЕТ или ФОРМА ОБУЧЕНИЯ в файле " + course + ".txt строка: " + line );
                    } else {
                        if (!groups.contains( st[4] + "~" + st[3] + "~" + st[2] + "~" + st[6] + "~" + st[7] + "~" + st[5] )) {
                            groups.add( st[4] + "~" + st[3] + "~" + st[2] + "~" + st[6] + "~" + st[7] + "~" + st[5] );
                        }
                        if (!facs.contains( st[6] + "~" + st[7] )) {
                            facs.add( st[6] + "~" + st[7] );
                        }
                    }
                }
            }
        }
        reader3.close();
        buffReader3.close();

        for (String fc : facs) {
            String[] parm = fc.split( "~" );
            facultatService.save( parm[0], parm[1] );
        }

        for (String temp : years) {

            String[] prm = temp.split( "-" );
            Year year = yearService.save( prm[0], prm[1] );
//////!!!            if (prm.length > 1) {
//////!!!                Semestr semestr = new Semestr( prm[1], year );
//////!!!                semestrService.save( semestr );
//////!!!            }
            //if (yearService.findByNameyearAndSemestr( prm[0], prm[1] ) == null){
            //    Year year = new Year(prm[0], prm[1]);
            //    yearService.save( year );
            //}
        }
        for (String groupName : groups) {
            String[] names = groupName.split( "~" );
            String[] temp = names[2].split( "-" );
            Year year = yearService.findByFirstnameyearAndSecondnameyear( temp[0], temp[1] );
            Facultat facultat = facultatService.findByNameAndForma( names[3], names[4] );
//16.08.2021            Groupp groupp = new Groupp( names[1], names[0], year, facultat );
//16.08.2021            groupService.save( groupp );
            if (names.length > 5) {
                Semestr semestr = semestrService.save( names[5] );
//28.08.2021                Groupp groupp = new Groupp( names[1], Integer.parseInt( names[0] ), semestr, facultat );

                //Groupp groupp = new Groupp( names[1], facultat, semestr, year, null );
                groupService.save( names[1], facultat, semestr, year, null );
            }
        }
        return res;
    }

    public String prepare(String filename) throws IOException {

        //Reader reader = new FileReader( UtilsController.getPath() + "\\" + filename + ".txt" );
        Reader reader = new FileReader( filename + ".txt" );
        //System.out.println("MainControllerService::prepare: Читаем файл:" + UtilsController.getPath() + "\\" + filename + ".txt");
        BufferedReader buffReader = new BufferedReader( reader );

        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (line.contains( "~" )) {
                if (!line.substring( 0, 2 ).equals( "\uFEFF#" )) {
                    String[] st = line.split( "~" );
                    if ((filename.equals( "Student" )) || (filename.equals( "Teacher" ))) {
                        lastnameService.saveIntoLastname(  st[0] );
                        firstnameService.saveIntoFirstname( st[1] );
                        secondnameService.saveIntoSecondname( st[2] );
                    }
                    Firstname firstname = firstnameService.findFirstnameByName( st[1] );
                    Secondname secondname = secondnameService.findSecondnameByName( st[2] );
                    Lastname lastname = lastnameService.findLastnameByFamily( st[0] );
                    if (filename.equals( "Teacher" )) {
                        Startdata startdata = new Startdata( "Teacher", firstname,
                                secondname, lastname,null, "" );
                        startdataService.save( startdata );
                        //teacherService.saveIntoTeacher( firstname, secondname, lastname, st[3], st[4] );
                    }
                    if (filename.equals( "Student" )) {
                        String[] temp = st[5].split( "-" );
                        Facultat facultat = facultatService.findByNameAndForma( st[6], st[7] );
                        Year year = yearService.findByFirstnameyearAndSecondnameyear( temp[0], temp[1] );
                        String[] semestrs = st[8].split( "-" );
                        if (!semestrs[0].equals( "0" )) {
                            Semestr semestr1 = semestrService.findByName( semestrs[0] );
                            Groupp groupp = null;// groupService.findGrouppByNameGrouppAndPlanSubgrouppAndFacultat_IdAndSemestr_Id(
                            //st[4], st[3], Integer.parseInt( st[3] ), semestr1, facultat );
                            if (groupp != null) {
//2021 09 05                                Startdata startdata = new Startdata( "Student", firstname,
//2021 09 05                                        secondname, lastname, groupp, Long.parseLong( st[3] ), st[9] );
                                Startdata startdata = new Startdata( "Student", firstname,
                                        secondname, lastname, groupp, "" );
                                startdataService.save( startdata );
                            }
                            groupp = null;// groupService.findGrouppByNameGrouppAndPlanSubgrouppAndFacultat_IdAndSemestr_Id(
                            //st[4], st[3], 0, semestr1, facultat );
                            if (groupp != null) {
                                Startdata startdata = new Startdata( "Student", firstname,
                                        secondname, lastname, groupp, "" );
                                startdataService.save( startdata );
                            }
                        }
                        if (!semestrs[1].equals( "0" )) {
                            Semestr semestr2 = semestrService.findByName( semestrs[1] );
                            Groupp groupp = null;// groupService.findGrouppByNameGrouppAndPlanSubgrouppAndFacultat_IdAndSemestr_Id(
                            //st[4], st[3], Integer.parseInt( st[3] ), semestr2, facultat );
                            if (groupp != null) {
                                Startdata startdata = new Startdata( "Student", firstname,
                                        secondname, lastname, groupp,  "" );
                                startdataService.save( startdata );
                            }
                            groupp = null;// groupService.findGrouppByNameGrouppAndPlanSubgrouppAndFacultat_IdAndSemestr_Id(
                            //st[4], st[3], 0, semestr2, facultat );
                            if (groupp != null) {
                                Startdata startdata = new Startdata( "Student", firstname,
                                        secondname, lastname, groupp, "" );
                                startdataService.save( startdata );
                            }
                        }
                        //studentService.saveIntoStudent( firstname, secondname, lastname, st[3], groupp, year );
                    }
                    //if (filename.equals( "Course" )) {
                    //    if (!line.substring( 0, 1 ).equals( "#" )) {
                    //        Long indexOfUser = Long.parseLong( st[2].trim() );
                    //        User user = userService.findUserById( indexOfUser );
                    //        Teacher teacher = null;
                    //        if (user != null) {
                    //            teacher = teacherService.findTeacherByUser( user );
                    //        }
                    //        Year year = yearService.findByNameyear( st[3] );
                    //        Facultat facultat = facultatService.findByNameAndForma( st[6], st[7] );
                    //        Groupp groupp = groupService.findByNameGrouppAndYearAndFacultat( st[4], year, facultat );
                    //        Semestr semestr = semestrService.findByYear_IdAndName( year, st[5] );
                    //        Course course = new Course( st[0], st[1], groupp, teacher, semestr, facultat );
                    //        courseService.save( course );
                    //    }
                    //}
                }
            }
        }
        reader.close();
        buffReader.close();
        //return UtilsController.getPath() + "\\" + filename + ".txt";
        return filename + ".txt";
    }

    public void makeNewModel(Model model, User user, int facultatId, int yearid, int semestrid, int groupid, int couseid,
                             String tdateString, int callid, Long subgroup1, Long subgroup2) {

        Teacher teacher = teacherService.findTeacherByUser( user );
        ////model.addAttribute( "teacher", teacher.getFullFio() );


        ////List<Facultat> facultats = facultatService.findAll();
        ////Facultat facultat = facultats.get( facultatId );
        ////model.addAttribute( "facultats", facultats );
        ////model.addAttribute( "facultatId", facultatId );


        ////List<Year> years = yearService.findAll();
        ////Year year  = years.get( 0 );
        ////model.addAttribute( "years", years );
        ////model.addAttribute( "yearid", yearid );

        ////List<Semestr> semestrs = semestrService.findByYear_Id(year.getId());
        ////Semestr semestr = null;
        ////if (semestrs.size() > 0) {
        ////    semestr = semestrs.get( semestrid );
        ////}
        ////model.addAttribute( "semestrs", semestrs );
        ////model.addAttribute( "semestrid", semestrid );

        ////List<Groupp> groups = groupService.findByYear_IdAndAndFacultat_Id( year.getId(), facultat.getId() );
        ////Groupp groupp = null;
        ////if (groups.size() > 0) {
        ////    groupp = groups.get( groupid );
        ////}
        ////model.addAttribute( "groups", groups );
        ////model.addAttribute( "groupid", groupid );


        ////List<Course> courses = courseService.findByGrouppIdAndTeacher_IdAndFacultat_IdAndSemestrId(
        ////        groupp.getId(), teacher.getId(), facultat.getId(), semestr.getId() );
        ////Course course = null;
        ////if (courses.size() > 0) {
        ////    course = courses.get( couseid );
        ////} else {
        ////    couseid = 0;
        ////}
        ////model.addAttribute( "courses", courses );
        ////model.addAttribute( "couseid", couseid );


        model.addAttribute( "tdate", tdateString );


        ////List<Call> calls = callService.findAll();
        ////model.addAttribute( "calls", calls );
        ////model.addAttribute( "callid", callid );


        List<Student> students1 = null;
        List<Student> students2 = null;
        ////if (groupp != null) {
        ////    students1 = studentService.getStudentsBySubgroups( groupp.getId(), subgroup1 );
        ////    students2 = studentService.getStudentsBySubgroups( groupp.getId(), subgroup2 );
        ////}
        ////model.addAttribute( "students1", students1 );
        ////model.addAttribute( "students2", students2 );

    }

    public void makeModel(Model model, Long facultatId,
                          Long yearid, Long semestrid, String groupIdString, Long subgroup1, Long subgroup2,
                          String couseIdString, String tdateString, String callIdString, User user) {

        //String syd1 = year1.getNameyear();
        //Year year2 = yearService.findByNameyear( "2021" );
        //String sid1 = year2.getNameyear();
////        Long yearid = 0L;
////        for (Year year : years){
////            if (year.getId() == Long.parseLong( yearIdString )){
////                yearid = year.getId();
////            }
////        }

        ////List<Facultat> facultats = facultatService.findAll();
        ////Long facultatIdfirst = facultats.get( 0 ).getId();
        ////model.addAttribute( "facultats", facultats );
        ////model.addAttribute( "facultatId", facultatId );
        ////model.addAttribute( "facultatIdfirst", facultatIdfirst );

        ////List<Year> years = yearService.findAll();// groupService.loadAllNameYear();
        ////Long yearidfirst = years.get( 0 ).getId();
        //////Long yearid = Long.parseLong( yearIdString );
        ////model.addAttribute( "years", years );
        ////model.addAttribute( "yearid", yearid );
        ////model.addAttribute( "yearidfirst", yearidfirst );

        //List<Semestr> semestrs = semestrService.findAll();
        //Long semestrfirst = semestrs.get( 0 ).getId();
        //model.addAttribute( "semestrs", semestrs );
        //model.addAttribute( "semestrid", semestrid );
        //model.addAttribute( "semestrfirst", semestrfirst );


        //Optional<Year> year = yearService.findById( Long.valueOf( yearIdString ));
        //String yearName = years.get( iyI ).getNameyear();
        ////Year year = yearService.findYearById( yearid );// years.get( yearid.intValue() );
        //List<Groupp> groups = groupService.findByYear( year );

        Teacher teacher = teacherService.findTeacherByUser( user );
        ////model.addAttribute( "teacher", teacher.getFullFio() );

        List<Groupp> groups = null;//groupService.findAll( year.getId() );
//        Long groupid = 0L;
//        for( Groupp grp : groups ){
//            if (grp.getId() == Long.parseLong( groupIdString )){
//                groupid = grp.getId();
//            }
//        }

        ////Long groupidfirst = groups.get( 0 ).getGroupp_id();

        //Set<Course> courses1 = groups.get( groupidfirst.intValue() ).getCourses();

        Long groupid = Long.parseLong( groupIdString );
        if (groupid == 0) {
            ////groupid = groupidfirst;
        }
        //int indexgroup = findIndex(groups, groupIdString);
        model.addAttribute( "groups", groups );
        model.addAttribute( "groupid", groupid );
        ////model.addAttribute( "groupidfirst", groupidfirst );

        List<Course> courses=null;// = courseService.findByGrouppIdAndTeacher_IdAndSemestr_Id( groupid, teacher.getId(), 12L );
        Long courseid = utilsController.getFomInit( "groupInit" );
        for (Course crs : courses) {
            if (crs.getId() == Long.parseLong( groupIdString )) {
                courseid = crs.getId();
            }
        }

        //int indexcourse = findIndex(courses, couseId);
        model.addAttribute( "courses", courses );
        model.addAttribute( "couseid", courseid );


        //String tdate = DateTime.now().toString("dd-MM-YY");
        model.addAttribute( "tdate", tdateString );

        ////List<Call> calls = callService.findAll();
        ////Long callid = 0L;
        ////for (Call cal : calls) {
        ////    if (cal.getId() == Long.parseLong( groupIdString )) {
        ////        callid = cal.getId();
        ////    }
        ////}
        //////int indexrasp = findIndex(rasps, raspindex);
        ////model.addAttribute( "calls", calls );
        ////model.addAttribute( "callid", callid );

        //String group = groups.get( indexgroup ).getBody();
        ////List<Student> students1 = studentService.getStudentsBySubgroups( groupid, subgroup1 );
        ////model.addAttribute( "students1", students1 );
        ////List<Student> students2 = studentService.getStudentsBySubgroups( groupid, subgroup2 );
        ////model.addAttribute( "students2", students2 );


    }

    //private static <T> List<T> findIndex (List<? super T> lst, String indexInput){
//    private int findIndex(List<T> lst, String indexInput){
//        int i = 0;
//        int indexOut = 0;
////        Long igL = Long.valueOf( indexInput );
//        for ( gr:lst){
////            if (gr.getId().longValue() == igL.longValue()) {
////            String tmp = String.valueOf(gr.getId());
////            if (tmp.equals( indexInput)) {
////                indexOut = i;
////            }
//            i++;
//        }
//        return indexOut;
//    }
    public Timestamp convertStringToTimestamp(String strDate) {
        try {
            DateFormat formatter = new SimpleDateFormat( "dd-MM-yyyy" );
            // you can change format of date
            Date date = formatter.parse( strDate );
            Timestamp timeStampDate = new Timestamp( date.getTime() );

            return timeStampDate;
        } catch (ParseException e) {
            System.out.println( "Exception :" + e );
            return null;
        }
    }

    private boolean checkStrings(List<String> arr, String parm) {
        boolean res = false;
        for (String tmp : arr) {
            if (tmp.equals( parm )) {
                res = true;
            }
        }
        return res;
    }

    public void makeRaspisanie(User user, Model model, String actDate){
        List<Course> courses = new ArrayList<>();

        model.addAttribute("tdate", actDate);
        //DateTime.now().toString().split("T");
        List<Raspisanie> raspisanie = new ArrayList<>();
        if (user != null) {
            Teacher teacher = teacherService.findTeacherByUser(user);
            if (teacher != null){
                courses = courseService.findByTeacher(teacher);
                for (Course course : courses) {
                    //raspisanie.addAll(teacherControllerService1.findRaspisanie( course, actDate, null));
                    raspisanie.addAll(raspisanieService.findAllByActiondateAndCourse(
                            actDate, course));
                }
            }
            Student student = studentService.findByUser(user);
            if (student != null) {
                courses = courseService.findByGroupp(student.getGroupp());
                Groupp grouppLek = groupService.findGrouppByNamegrouppAndFacultat_AndSemestr_AndYear_AndSubgroupp(
                        student.getGroupp().getOnlynamegroupp(), student.getGroupp().getFacultat(),
                        student.getGroupp().getSemestr(), student.getGroupp().getYear(),
                        subgrouppService.findSubgrouppByNamesubgroupp("0"));
                courses.addAll(courseService.findByGroupp(grouppLek));
                for (Course course : courses) {
                    raspisanie.addAll(raspisanieService.findAllByActiondateAndCourse(
                            actDate, course));
                }
            }
        }
        model.addAttribute("raspisanieList", raspisanie);
        //model.addAttribute("actDate", actDate );
    }
}