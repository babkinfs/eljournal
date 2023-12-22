package com.babkin.eljournal.service;

import com.babkin.eljournal.controllers.UtilsController;
import com.babkin.eljournal.entity.Period;
import com.babkin.eljournal.entity.Role;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.DoubleString;
import com.babkin.eljournal.entity.temporaly.ForTeacherController;
import com.babkin.eljournal.entity.temporaly.ModelForStart;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.entity.temporaly.QuickSort;
import com.babkin.eljournal.repo.StartdataRepos;
import com.babkin.eljournal.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherControllerService {

    @Value("${base.path}")
    private String basepath;
    @Value("${path.antwer}")
    private String pathantwer;

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private YearService yearService;
    @Autowired
    private FacultatService facultatService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private SemestrService semestrService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CallService callService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PlanService planService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StartdataRepos startdataRepos;
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private DnevnikService dnevnikService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private SubgrouppService subgrouppService;
    @Autowired
    private RaspisanieService raspisanieService;
    @Autowired
    private FirstnameService firstnameService;
    @Autowired
    private SecondnameService secondnameService;
    @Autowired
    private LastnameService lastnameService;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private FilesForDnevnikService filesForDnevnikService;

    private int lenDates = 9;
    private String fullFileName;

    public String fillCourse(String[] st) {
        if (st.length > 1) {
            String[] fio = st[9].split(" ");
            Teacher teacher = teacherService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
                    fio[1], fio[2], fio[0]);
            if (teacher == null) {
                return "Отсутствует преподаватель " + st[9];
            }
            String[] temp = st[3].split("-");
            Year year = yearService.findByFirstnameyearAndSecondnameyear(temp[0], temp[1]);
            Semestr semestr = semestrService.save(st[6]);
            Facultat facultat = facultatService.findByNameAndForma(st[7], st[8]);
            String namesubgroupp = st[5];
            Subgroupp subgroupp = subgrouppService.saveSubgroupp(namesubgroupp);
            Groupp groupp = groupService.save(st[4], facultat, semestr, year, subgroupp);
            int kolzan = Integer.parseInt(st[0]);
            Course course = new Course(st[1], st[2], kolzan, groupp, teacher);
            courseService.save(course);
        }
        return null;
    }

    public ForTeacherController makeNewModel(Model model, User user, int number, int facultatId, int yearid, String semestrid, Long groupid,
                                             Long courseid,
                                             String tdateString, int callid, Long subgroup1, Long subgroup2, int period_id, int counter_id,
                                             String facultatInit, String yearInit, String semestrInit, String groupInit, String courseInit,
                                             String callInit, String periodInit, String counterInit, int typezId, int gpgid) throws ParseException {

        model.addAttribute("savevisible", "");


        model.addAttribute("facultatInit", facultatInit);
        model.addAttribute("yearInit", yearInit);
        model.addAttribute("semestrInit", semestrInit);
        model.addAttribute("groupInit", groupInit);
        model.addAttribute("courseInit", courseInit);
        model.addAttribute("callInit", callInit);
        model.addAttribute("periodInit", periodInit);
        model.addAttribute("counterInit", counterInit);

        List<String> periods = Period.getListWeek();
        model.addAttribute("periods", periods);
        model.addAttribute("period_id", period_id);


        model.addAttribute("tdate", tdateString);
        ForTeacherController forTeacherController = calculate(model, user, number, facultatId, yearid, semestrid, groupid,
                courseid, tdateString, callid, period_id, counter_id);
        List<String> dates1 = new ArrayList<>();
        List<String> dates2 = new ArrayList<>();
        List<String> dates3 = new ArrayList<>();
        //if (number > 0) {
        int count = 0;
        for (String date : forTeacherController.getDates()) {
            if (count < 9) {
                dates1.add(date);
            } else if (count < 18) {
                dates2.add(date);
            } else if (count < 27) {
                dates3.add(date);
            }
            count++;
        }
        //}
        model.addAttribute("dates1", dates1);
        model.addAttribute("dates2", dates2);
        model.addAttribute("dates3", dates3);

        model.addAttribute("typezs", UtilsController.getTypeZstrings());
        model.addAttribute("typezId", typezId);
        model.addAttribute("gpgs", UtilsController.getGPGs());
        model.addAttribute("gpgid", gpgid);
        model.addAttribute("getDateFill", utilsController.getDataNow(new SimpleDateFormat("dd MMMM yyyy, EEEE")));

        return forTeacherController;
    }

    public ForTeacherController calculate(Model model, User user, int number, int facultatId, int yearid, String semestrid, Long groupid,
                                          Long couseid,
                                          String tdateString, int callid, int period_id, int counter_id) throws ParseException {

        ForTeacherController forTeacherController = new ForTeacherController();

        forTeacherController.setTeacher(teacherService.findTeacherByUser(user));
        model.addAttribute("teacher", forTeacherController.getTeacher().getFullFio());


        List<Facultat> facultats = facultatService.findAll();
        if (model != null) {
            model.addAttribute("facultats", facultats);
            model.addAttribute("facultatId", facultatId);
        }
        forTeacherController.setFacultat(facultats.get(facultatId));


        List<Year> years = yearService.findAll();
        if (model != null) {
            model.addAttribute("years", years);
            model.addAttribute("yearid", yearid);
        }
        forTeacherController.setYear(years.get(yearid));

        List<Semestr> semestrs = new ArrayList<>(); // semestrService.findByYear_AndFacultat( forTeacherController.getYear(), forTeacherController.getFacultat() );
        Semestr semestr = null;
        int sem_id = 0;
        if ((semestrs.size() > 0) && (semestrid != null)) {
            sem_id = Integer.parseInt(semestrid);
            semestr = semestrs.get(sem_id);
        }
        if (model != null) {
            model.addAttribute("semestrs", semestrs);
            model.addAttribute("semestrid", semestrid);
            model.addAttribute("sem_id", sem_id);
        }
        forTeacherController.setSemestr(semestr);
        List<Groupp> groups = groupService.findAll();//groupService.findAllByPlanSubgrouppNot();
        if (groupid == 0) {
            groupid = groups.get(0).getId();
        }
        Groupp groupp = groupService.findGrouppById(groupid);
        if (model != null) {
            model.addAttribute("groupid", groupid);
        }
        forTeacherController.setGroupp(groupp);

        List<Course> courses2 = new ArrayList<>();
        for (Groupp groupp1 : groups) {
        }
        List<Course> courses = courses2.stream().sorted(Comparator.comparing(Course::getNameCourseFull)).collect(Collectors.toList());
        int int_course_id = Math.toIntExact(couseid);
        couseid = courses.get(int_course_id).getId();
        if (model != null) {
            model.addAttribute("courses", courses);
            model.addAttribute("couseid", couseid);
            model.addAttribute("int_course_id", int_course_id);
        }
        Course course = courseService.findCourseById(couseid);
        forTeacherController.setCourse(course);

        List<Integer> counters = null;//25.12.2022   Period.listCounter();
        if (model != null) {
            model.addAttribute("counters", counters);
            model.addAttribute("counter_id", counter_id);
        }
        int counter = counters.get(counter_id);
        forTeacherController.setCounter(counter);

        List<Call> calls = callService.findAll();
        if (model != null) {
            model.addAttribute("calls", calls);
            model.addAttribute("callid", callid);
        }
        forTeacherController.setCall(calls.get(callid));

        List<String> dates = new ArrayList<>();
        if (number == 0) {
            List<Plan> plans = planService.findPlanByCall_IdAndCourse_Id(forTeacherController.getCall(),
                    forTeacherController.getCourse());
            if (plans != null) {
                for (Plan plan : plans) {
                    dates.add(plan.getAction());
                }
            }
        } else {

            int periodInt = 0;
            switch (period_id) {
                case 0:
                    periodInt = 14;
                    break;
                case 1:
                    periodInt = 7;
                    break;
            }
            String[] dt = tdateString.split("-");
            Integer y = Integer.parseInt(dt[2]);
            int m = Integer.parseInt(dt[1]);
            int d = Integer.parseInt(dt[0]);
            Calendar calendar = new GregorianCalendar(y, m - 1, d, 17, 59, 0);
            for (int i = 0; i < counter; i++) {
                Date dt1 = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String tmpDate = sdf.format(dt1);
                dates.add(tmpDate);
                calendar.add(Calendar.DAY_OF_MONTH, periodInt);
            }
        }

        forTeacherController.setDates(dates);

        return forTeacherController;
    }

    public Facultat getFacultat(Model model, int facultatId) {
        List<Facultat> facultats = facultatService.findAll();
        if (model != null) {
            model.addAttribute("facultats", facultats);
            model.addAttribute("facultatId", facultatId);
        }
        return facultats.get(facultatId);
    }

    public Facultat getFacultat1(Model model, int facultatId) {
        List<Facultat> facultats = facultatService.findAll();
        List<String> facultatsStrings = new ArrayList<>();
        for (Facultat facultat : facultats) {
            facultatsStrings.add(facultat.getName() + ", " + facultat.getForma());
        }
        if (model != null) {
            model.addAttribute("facultats", facultatsStrings);
            model.addAttribute("facultatId", facultatId);
        }
        return facultats.get(facultatId);
    }
    public Facultat getFacultat2(Model model, int facultatId, Semestr semestr) {
        List<Facultat> facultats = facultatService.findAll();
        List<String> facultatsStrings = new ArrayList<>();
        for (Facultat facultat : facultats) {
            facultatsStrings.add(facultat.getName() + ", " + facultat.getForma());
        }
        if (model != null) {
            model.addAttribute("facultats", facultatsStrings);
            model.addAttribute("facultatId", facultatId);
        }
        return facultats.get(facultatId);
    }


    public Year getYear(Model model, int yearId) {
        List<Year> years = yearService.findAll();
        if (model != null) {
            model.addAttribute("years", years);
            model.addAttribute("yearid", yearId);
        }
        return years.get(yearId);
    }

    public List<Year> getYear1(Model model, int yearId) {
        List<Year> years = yearService.findAll();
        List<String> yearsStrings = new ArrayList<>();
        for (Year year : years) {
            yearsStrings.add(year.getFirstnameyear() + "-" + year.getSecondnameyear());
        }
        if (model != null) {
            model.addAttribute("years", yearsStrings);
            model.addAttribute("yearId", yearId);
        }
        return years;
    }
    public Year getYear2(Model model, int yearId) {
        List<Year> years = yearService.findAll();
        List<String> yearsStrings = new ArrayList<>();
        for (Year year : years) {
            yearsStrings.add(year.getFirstnameyear() + "-" + year.getSecondnameyear());
        }
        if (model != null) {
            model.addAttribute("years", yearsStrings);
            model.addAttribute("yearId", yearId);
        }
        return years.get(yearId);
    }

    public Semestr getSemestr(Model model, int semestrId) {
        List<Semestr> semestrs = semestrService.findAll();
        if (model != null) {
            model.addAttribute("semestrs", semestrs);
            model.addAttribute("semestrId", semestrId);
        }
        return semestrs.get(semestrId);
    }

    public Semestr getSemestr1(Model model, int semestrId) {
        List<Semestr> semestrs = semestrService.findAll();
        List<String> semestrsStrings = new ArrayList<>();
        for (Semestr semestr : semestrs) {
            semestrsStrings.add(semestr.getName());
        }
        if (model != null) {
            model.addAttribute("semestrs", semestrsStrings);
            model.addAttribute("semestrId", semestrId);
        }
        //System.out.println("getSemestr1 semestrId=" + semestrId);
        return semestrs.get(semestrId);
    }

//    public Groupp getGroupp(Model model, int grouppId, String typezId, Semestr semestr, Year year, Facultat facultat) {
//         Subgroupp subgroupp1 = subgrouppService.saveSubgroupp(typezId);
//        Groupp groupps = groupService.findGrouppBy_NamesubgrouppAndSemestrAndYearAndFacultat(semestr, year, facultat, subgroupp1);
//        if (model != null) {
//            model.addAttribute("groups", groupps);
//            model.addAttribute("groupid", grouppId);
//        }
//        return groupps;//.get( grouppId );
//    }

    public Groupp getGroupp1(Model model, int grouppId, String typez,
                             Semestr semestr, Year year, Facultat facultat) {

        int subgrouppFind = 0;
        if (!typez.equals("лекция")) {
            subgrouppFind = 1;
        }
        List<Groupp> groupps = null;
        if ((facultat == null) && (semestr == null) && (year == null)) {
            groupps = groupService.findAll();
        } else if ((facultat == null) && (semestr == null)) {
            groupps = groupService.findGrouppsByYear(year);
        } else if ((facultat == null) && (year == null)) {
            groupps = groupService.findGrouppsBySemestr(semestr);
        } else {
            groupps = groupService.findGrouppsByFacultat_AndSemestr_AndYear(facultat, semestr, year);
        }
        List<Groupp> grouppsOk = new ArrayList<>();
        List<String> grouppsStrings = new ArrayList<>();
        int index = 0;
        int grouppIdNew = 0;
        for (Groupp groupp : groupps) {
            String grouppName = groupp.getNamegroupp();
            if (((grouppName.indexOf(".") < 0) && (subgrouppFind ==0))
            || ((grouppName.indexOf(".") >- 0) && (subgrouppFind !=0))){
            //if (groupp.getSubgroupp().getNamesubgroupp() == subgrouppFind) {
                grouppsStrings.add(grouppName);// + "." + groupp.getSubgroupp().getNamesubgroupp());
                grouppsOk.add(groupp);
                //grouppIdNew = index;
            }
            index++;
        }
        if (grouppId >= grouppsOk.size()){
            grouppId = grouppsOk.size()-1;
        }
        if (model != null) {
            model.addAttribute("groups", grouppsStrings);
            model.addAttribute("grouppId", grouppId);
            model.addAttribute("grouppIdNew", grouppIdNew);
        }
        return grouppsOk.get(grouppId);
    }

    public Subgroupp getSubgroupp1(Model model, int subgrouppId, Groupp groupp) {

        List<Groupp> groupps = groupService.getDistinctGrouppByNamegroupp(groupp.getNamegroupp());

        List<Subgroupp> subgroupps = new ArrayList<>();
        List<String> subgrouppsStrings = new ArrayList<>();
        for (Groupp grouppFor : groupps) {
            //Subgroupp subgroupp = subgrouppService.findSubgrouppsById( grouppFor.getSubgroupp(). )
            if (!subgroupps.contains(grouppFor.getSubgroupp())) {
                subgroupps.add(grouppFor.getSubgroupp());
                String namesubgroupp = String.valueOf(grouppFor.getSubgroupp().getNamesubgroupp());
                subgrouppsStrings.add(namesubgroupp);
            }
        }
        if (model != null) {
            model.addAttribute("subgroupps", subgrouppsStrings);
            model.addAttribute("subgrouppId", subgrouppId);
        }
        return subgroupps.get(subgrouppId);
    }

    public Course getCourse(Model model, int courseId, Groupp groupp, Teacher teacher) {
        List<Course> courses = courseService.findByGrouppAndTeacher(groupp, teacher);//
        if (model != null) {
            model.addAttribute("courses", courses);
            model.addAttribute("couseid", courseId);
        }
        if (courses.size() == 0) {
            return null;
        }
        return courses.get(courseId);
    }

    public Course getCourse1(Model model, int courseId, Groupp groupp, Teacher teacher) {
        List<Course> courses = courseService.findByGrouppAndTeacher(groupp, teacher);//
        List<String> coursesStrings = new ArrayList<>();
        for (Course course : courses) {
            coursesStrings.add(course.getNameCourseFull());
        }
        if (courseId>=courses.size()){
            courseId=courses.size()-1;
        }
        if (model != null) {
            model.addAttribute("courses", coursesStrings);
            model.addAttribute("courseId", courseId);
        }
        if (courses.size() > 0) {
            return courses.get(courseId);
        }
        return null;
    }

    public Call getCall(Model model, int callId) {
        List<Call> calls = callService.findAll();
        if (model != null) {
            model.addAttribute("calls", calls);
            model.addAttribute("callid", callId);
        }
        return calls.get(callId);
    }

    public Call findByTime(String time) {
        List<Call> calls = callService.findAll();
        for (Call call : calls) {
            if (utilsController.comparison(call.getName(), time) == 0) {//0 во время пары
                return call;
            }
        }
        return calls.get(0);
    }

    public Call getCall1(Model model, int callId) {
        List<Call> calls = callService.findAll();
        calls.remove(9);
        List<String> callsStrings = new ArrayList<>();
        for (Call call : calls) {
            callsStrings.add(call.getName());
        }
        if (model != null) {
            model.addAttribute("calls", callsStrings);
            model.addAttribute("callId", callId);
        }
        return calls.get(callId);
    }

    public String getTypeZ1(Model model, int typezId) {
        String[] typeZstrings = UtilsController.getTypeZstrings();
        if (model != null) {
            model.addAttribute("typezs", typeZstrings);
            model.addAttribute("typezId", typezId);
        }
        return typeZstrings[typezId];

    }

    private Groupp findGroupp(Model model, int yearId, int semestrId, int facultatId,
                              int typezId, int grouppId, int courseId, Teacher teacher) {
        List<Year> years = getYear1(model, yearId);
        Year year = years.get(yearId);
        Semestr semestr = getSemestr1(model, semestrId);
        //Semestr semestr = semestrs.get( semestrId );
        Facultat facultat = getFacultat1(model, facultatId);
        //Facultat facultat = facultats.get( facultatId );
        String typeZstring = getTypeZ1(model, typezId);
        return getGroupp1(model, grouppId, typeZstring, semestr, year, facultat);
    }

    public void fillDataFromLekLab(String fileName, ModelForStart modelForStart,
                                   Model model) throws IOException {

        List<Raspisanie> list = null;// raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() );

        Reader reader = new FileReader(fileName);
        BufferedReader buffReader = new BufferedReader(reader);
        String startPath = buffReader.readLine();
        String[] template = buffReader.readLine().split(" ");
        int count = 0;
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
            if (!line.equals("")) {
                String[] arr = line.split("~");
                String[] themeZadan = arr[3].split("@");
                String zadanie = "";
                if (themeZadan.length > 1) {
                    zadanie = themeZadan[1];
                }
            }
        }
        model.addAttribute("tdatas", list);
    }
    public ModelForStart prepareStart(
            String name,
            int number,
            int periodId,
            int yearId,
            int semestrId,
            int facultatId,
            int typezId,
            int grouppId,
            int courseId,
            int callId,
            String calc_fysgc,
            boolean blockdate,
            Teacher teacher,
            Model model
    ) {
        String[] names = new String[]{"periodId", "yearId", "semestrId", "facultatId", "typezId", "grouppId", "courseId", "callId"};
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) {
                switch (i) {
                    case 0:
                        periodId = number;
                        break;
                    case 1:
                        yearId = number;
                        break;
                    case 2:
                        semestrId = number;
                        break;
                    case 3:
                        facultatId = number;
                        break;
                    case 4:
                        typezId = number;
                        break;
                    case 5:
                        grouppId = number;
                        break;
                    case 6:
                        courseId = number;
                        break;
                    case 7:
                        callId = number;
                        break;
                }
            }
        }
        Year year = getYear2(model, yearId);
        Semestr semestr = getSemestr1(model, semestrId);
        Facultat facultat = getFacultat2(model, facultatId, semestr);
        String typeZ = getTypeZ1(model, typezId);
        Groupp groupp = getGroupp1(model, grouppId, typeZ, semestr, year, facultat);
        Course course = getCourse1(model, grouppId, groupp, teacher);
        Call call = getCall1(model, callId);
        List<String> periodList = getPeroid(model, periodId);
        int kolzan = 0;
        if (course!= null){
            kolzan = course.getKolzan();
        }
        model.addAttribute("kolzan", kolzan);
        model.addAttribute("tdate", utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")));
        return new  ModelForStart(periodId, year, semestr, facultat, groupp, course, call, typeZ);
        //return null;
}
    public ModelForStart prepareStart1(
            String name,
            int number,
            int periodId,
            int yearId,
            int semestrId,
            int facultatId,
            int typezId,
            int grouppId,
            int courseId,
            int callId,
            String calc_fysgc,
            boolean blockdate,
            Teacher teacher,
            Model model
    ) {
        if (name.equals("typezId")) {
            typezId = number;
        }
        model.addAttribute("tdate", calc_fysgc);// utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ) );
        model.addAttribute("getDateFill", utilsController.getDataNow(new SimpleDateFormat("dd MMMM yyyy, EEEE")));


        List<Year> years = getYear1(model, yearId);
        Year year = years.get(yearId);
        List<Groupp> grouppList = groupService.findAll();
        Groupp groupp1 = grouppList.get(grouppId);
        Semestr semestr = semestrService.findByName(utilsController.getTempSemestrName(groupp1));
        Facultat facultat = getFacultat1(model, facultatId);

        String typeZstring = getTypeZ1(model, typezId);

        String subgrouppFind = String.valueOf(typezId);//"0";
        List<Groupp> groupps = null;
        if ((facultat == null) && (semestr == null) && (year == null)) {
            groupps = groupService.findAll();
        } else if ((facultat == null) && (semestr == null)) {
            groupps = groupService.findGrouppsByYear(year);
        } else if ((facultat == null) && (year == null)) {
            groupps = groupService.findGrouppsBySemestr(semestr);
        } else {
            groupps = groupService.findGrouppsByFacultat_AndSemestr_AndYear(facultat, semestr, year);
        }
        subgrouppFind = "0";//
        if (typeZstring.equals("лекция")) {
//2023 07 29            Groupp forFind = groupps.get(grouppId);//получили группу
//2023 07 29            String grouppName = groupps.get(0).getNamegroupp();//получили группу и подгруппу
//2023 07 29            if (grouppName.indexOf(".")>=0){
//2023 07 29                grouppName = grouppName.substring(0, grouppName.indexOf("."));//удалили подгруппу
//2023 07 29            }
//2023 07 29            for (int ind = 0; ind < groupps.size(); ind++){
//2023 07 29                if (groupps.get(ind).getNamegroupp().equals(grouppName)){
//2023 07 29                    grouppId = ind;
//2023 07 29                }
//2023 07 29            }
        } else {
            subgrouppFind = "1";
            if (name.equals("grouppId")) {
                subgrouppFind = groupps.get(number).getSubgroupp().getNamesubgroupp();// 24 02 2023 "1";
            } else if (!groupps.get(grouppId).getSubgroupp().getNamesubgroupp().equals("0")) {
                subgrouppFind = groupps.get(grouppId).getSubgroupp().getNamesubgroupp();
            }
        }

        boolean grouppClick = false;
        String[] names = new String[]{"periodId", "yearId", "semestrId", "facultatId", "typezId", "grouppId", "courseId", "callId"};
        for (
                int i = 0;
                i < names.length; i++) {
            if (name.equals(names[i])) {
                //System.out.println("i=" + i + " names[i]=" + names[i] + " number=" + number);
                switch (i) {
                    case 0:
                        periodId = number;
                        break;
                    case 1:
                        yearId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 2:
                        semestrId = number;
                        semestr = getSemestr1(model, semestrId);
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 3:
                        facultatId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 4:
                        typezId = number;
                        Groupp tempGroupp = groupps.get(grouppId);
                        for (int k = 0; k < groupps.size(); k++) {
                            if (groupps.get(k).getNamegroupp().equals(tempGroupp.getNamegroupp())
                                    && groupps.get(k).getSubgroupp().getNamesubgroupp().equals("1")) {
                                grouppId = k;
                                break;
                            }
                        }
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 5:
                        grouppId = number;
                        grouppClick = true;
                        break;
                    case 6:
                        courseId = number;
                        break;
                    case 7:
                        callId = number;
                        break;
                }
            }
        }

        Call tempCall = findByTime(utilsController.getTimeNow());
        Raspisanie raspisanie = null;
        List<Raspisanie> raspisanies = raspisanieService.findRaspisaniesByActiondateAndCall(calc_fysgc, tempCall);
        if (raspisanies.size()>0){
            raspisanie = raspisanies.get( 0 );
        }
        if ((raspisanie != null) && (name.equals("typezId"))) {
            callId = Long.valueOf(raspisanie.getCall().getId()).intValue();
            if (raspisanie.getTheme().getTypezan().equals("лаб")) {
                typezId = 1;
            }
        }


        List<String> grouppsStrings = new ArrayList<>();
        int index = 0;
        String baseGroupp = groupps.get(grouppId).getNamegroupp();
        int grouppIdNew = 0;
        for (Groupp groupp : groupps) {
            grouppsStrings.add(groupp.getNamegroupp());
            String tempSubgrouppFind = groupp.getSubgroupp().getNamesubgroupp();
            String nameGroupp = groupp.getNamegroupp();
            String nameSubgroupp = groupp.getSubgroupp().getNamesubgroupp();
            String nameSubgrouppRasp = "0";
            if (raspisanie != null) {
                nameSubgrouppRasp = raspisanie.getCourse().getGroupp().getSubgroupp().getNamesubgroupp();
            }

            if (tempSubgrouppFind.equals(subgrouppFind)
                    && nameGroupp.equals(baseGroupp)) {
                grouppIdNew = index;
            }
            index++;
        }
        if (model != null) {
            model.addAttribute("groups", grouppsStrings);
            //model.addAttribute( "grouppId", grouppId );
            model.addAttribute("grouppId", grouppIdNew);
            model.addAttribute("grouppIdNew", grouppIdNew);
        }

        Groupp groupp = null;
        if (groupps.size() > 0) {
            groupp = groupps.get(grouppIdNew);
        }


        List<Course> courses = courseService.findByGrouppAndTeacher(groupp, teacher);//
        List<String> coursesStrings = new ArrayList<>();
        for (
                Course course : courses) {
            coursesStrings.add(course.getNameCourseFull());
        }

        Course course = null;
        int kolzan = 0;
        if (courses.size() > 0) {
            course = courses.get(courseId);
            kolzan = course.getKolzan();
        } else {
            course = null;
        }
        if (model != null) {
            model.addAttribute("courses", coursesStrings);
            model.addAttribute("courseId", courseId);
            model.addAttribute("kolzan", kolzan);
        }

        List<String> periods = Period.getListWeek();
        model.addAttribute("periodId", periodId);
        model.addAttribute("periods", periods);
        model.addAttribute("courseId", courseId);
        model.addAttribute("blockdate", blockdate);
        Call call = getCall1(model, callId);
        return new  ModelForStart(periodId, year, semestr, facultat, groupp, course, call, typeZstring);
    }


    public String prepareForView(
            Model model,
            String name,
            int number,
            int periodId,
            int yearId,
            int semestrId,
            int facultatId,
            int typezId,
            int grouppId,
            int courseId,
            int callId,
            String calc_fysgc,
            String btn_out,
            Teacher teacher) throws ParseException {
        //1 нажал кнопку "Планы": name: "", number: 0, btn_out: ""
        //2 щелчок по "Время начала": name: "callId", number: 2, btn_out: ""
        //3 щелчок по "Дата начала" : name: "21-05-2022", number: 999, btn_out: ""
        //4 нажал на кнопку "Сохранить": name: "", number: 0, btn_out: "save"
        //5 щелчок на выбранном поле даты и времени: name: "dates", number: 0, btn_out: ""
        //6 нажал на кнопку "Удалить": name: "", number: 0, btn_out: "delete"
        //7 щелчок по "Семестр" : name: "semestrId", number: 0, btn_out: ""


        model.addAttribute( "getDateFill", utilsController.getDataNow( new SimpleDateFormat( "dd MMMM yyyy, EEEE" ) ) );

        List<Call> calls = callService.findAll();
        Call call = calls.get( callId );
        List<Raspisanie> raspisanies = null;
        boolean grouppClick = false;
        String[] names = new String[]{"periodId", "yearId", "semestrId", "facultatId", "typezId", "grouppId", "courseId", "callId"};
        for (int i = 0; i < names.length; i++) {
            if (name.equals( names[i] )) {
                //System.out.println( "i=" + i + " names[i]=" + names[i] + " number=" + number );
                switch (i) {
                    case 0:
                        periodId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 1:
                        yearId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 2:
                        semestrId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 3:
                        facultatId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 4:
                        typezId = number;
                        grouppId = UtilsController.groupIdInit;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 5:
                        grouppId = number;
                        grouppClick = true;
                        break;
                    case 6:
                        courseId = number;
                        courseId = UtilsController.courseIdInit;
                        break;
                    case 7:
                        callId = number;
                        break;
                }
            }
        }
        Groupp groupp = findGroupp(model, yearId, semestrId, facultatId, typezId, grouppId, courseId, teacher);
        Course course = getCourse1( model, courseId, groupp, teacher );

        int grouppIdNew = (int) model.getAttribute( "grouppIdNew" );
        if (!grouppClick) {
            model.addAttribute( "grouppId", grouppIdNew );
        }

        if (number == 999){
            model.addAttribute( "tdate", name);
        } else {
            model.addAttribute( "tdate", utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ) );
        }

        call = getCall1( model, callId );
        if (course == null) {
            model.addAttribute( "dates1", new ArrayList<DoubleString>() );
            model.addAttribute( "dates2", new ArrayList<DoubleString>() );
            model.addAttribute( "dates3", new ArrayList<DoubleString>() );
            model.addAttribute( "dates4", new ArrayList<DoubleString>() );

            return "Такого круса нет";
        }
        boolean addnewdates = false;
        if (raspisanies.size() == 0) {
            addnewdates = true;
        }
        List<DoubleString> dates = new ArrayList<DoubleString>();
        dates = raspisanies.stream().map(rasp ->new DoubleString( rasp.getActiondate(), rasp.getCall().getName() ))
                .collect(Collectors.toList());
        int periodInt = Period.getLength( periodId );
        if (btn_out.equals( "delete" )) {
            String zvonok = call.getName();
            DoubleString yakor = new DoubleString( calc_fysgc, call.getName() );
            dates = sortedDates(dates);
            List<DoubleString> datesStart = dates.stream().filter( dt -> dt.compareTo( yakor ) < 0 ).collect( Collectors.toList());

            int j = 0;
            for (int i = datesStart.size(); i < dates.size(); i++) {
                DoubleString fordelete = dates.get( i );
                if (fordelete.getSecond().equals( yakor.getSecond() ) && j < periodInt) {
                    j++;
                    Call call1 = callService.findByName( fordelete.getSecond() );
                    if (call1 != null) {
                        Raspisanie raspisanie = raspisanieService.findByActiondateAndCall( fordelete.getFirst(), call1 );

                        Long id = raspisanie.getId();
                        raspisanie.setCall( null );
                        raspisanie = raspisanieService.update( raspisanie );
                        raspisanieService.delete( raspisanie );
                    }
                }
            }
            dates = raspisanies.stream().map(rasp ->new DoubleString( rasp.getActiondate(), rasp.getCall().getName() ))
                    .collect(Collectors.toList());
            model.addAttribute( "disabled",true );//кнопка пассивна
        } else {
            if ((addnewdates) || (name.equals("callId"))) {// || (name.equals("dates"))) {
                calculate_raspisanie( periodId, dates, call.getName(), calc_fysgc, periodInt );
                model.addAttribute( "disabled",false );//кнопка активна
            } else {
                model.addAttribute( "disabled",true );//кнопка пассивна
            }
            if (btn_out.equals( "save" )) {
                for (DoubleString item : dates){
                    raspisanieService.save(  item.getFirst(), 0, call, null, null );
                }
                dates = raspisanies.stream().map(rasp ->new DoubleString( rasp.getActiondate(), rasp.getCall().getName() ))
                        .collect(Collectors.toList());
            }
        }
        model.addAttribute( "periodId", periodId );
        model.addAttribute( "periods", Period.getListWeek() );
        fillDates(dates, model);
        return null;
    }

    public List<DoubleString> calculate_raspisanie(int periodId, List<DoubleString> dates, String callName,
                                                   String calc_fysgc, int periodInt) {
        // Вычисление дат в количестве из Period по индексу periodId с даты начала calc_fysgc со сдвигом в periodInt
        List<DoubleString> tempdates = new ArrayList<DoubleString>();
        String[] dt = null;
        dt = calc_fysgc.split( "-" );
        Integer y = Integer.parseInt( dt[2] );
        int m = Integer.parseInt( dt[1] );
        int d = Integer.parseInt( dt[0] );
        Calendar calendar = new GregorianCalendar( y, m - 1, d, 17, 59, 0 );
        int counter = Period.getSemestr( periodId );
        boolean found = false;
        for (int i = 0; i < counter; i++) {
            Date dt1 = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
            String tmpDate = sdf.format( dt1 );
            DoubleString dstr = new DoubleString( tmpDate, callName );
            for (int ind=0; ind<dates.size(); ind++) {
                DoubleString ds = dates.get(ind);
                if ( dstr.getFirst().equals(ds.getFirst()) && dstr.getSecond().equals(ds.getSecond())){

                    found = true;
                }
            }
            tempdates.add(dstr);
            calendar.add( Calendar.DAY_OF_MONTH, periodInt );
        }
        if (found){
            tempdates.clear();
        }
        return tempdates;
    }

    public String prepareForView1(
            Model model,
            String name,
            int number,
            int periodId,
            int yearId,
            int semestrId,
            int facultatId,
            int typezId,
            int grouppId,
            int courseId,
            int callId,
            String calc_fysgc,
            String btn_out,
            Teacher teacher) throws ParseException {

        boolean grouppClick = false;
        List<Call> calls = callService.findAll();
        Call call = calls.get( callId );
        Groupp groupp = findGroupp(model, yearId, semestrId, facultatId, typezId, grouppId, courseId, teacher);
        Course course = getCourse1( model, courseId, groupp, teacher );;
        List<Raspisanie> raspisanies = null;

        model.addAttribute( "getDateFill", utilsController.getDataNow( new SimpleDateFormat( "dd MMMM yyyy, EEEE" ) ) );
        if (((!name.isEmpty()) && (number != 999)) || (btn_out.isEmpty())) {
            String[] names = new String[]{"periodId", "yearId", "semestrId", "facultatId", "typezId", "grouppId", "courseId", "callId"};
            for (int i = 0; i < names.length; i++) {
                if (name.equals( names[i] )) {
                    //System.out.println( "i=" + i + " names[i]=" + names[i] + " number=" + number );
                    switch (i) {
                        case 0:
                            periodId = number;
                            grouppId = UtilsController.groupIdInit;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 1:
                            yearId = number;
                            grouppId = UtilsController.groupIdInit;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 2:
                            semestrId = number;
                            grouppId = UtilsController.groupIdInit;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 3:
                            facultatId = number;
                            grouppId = UtilsController.groupIdInit;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 4:
                            typezId = number;
                            grouppId = UtilsController.groupIdInit;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 5:
                            grouppId = number;
                            grouppClick = true;
                            break;
                        case 6:
                            courseId = number;
                            courseId = UtilsController.courseIdInit;
                            break;
                        case 7:
                            callId = number;
                            break;
                    }
                }
            }
        }

        List<String> periods = getPeroid( model, periodId );
        int grouppIdNew = (int) model.getAttribute( "grouppIdNew" );
        if (!grouppClick) {
            model.addAttribute( "grouppId", grouppIdNew );
        }

        model.addAttribute( "tdate", utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ) );

        call = getCall1( model, callId );
        if (course == null) {
            model.addAttribute( "dates1", new ArrayList<DoubleString>() );
            model.addAttribute( "dates2", new ArrayList<DoubleString>() );
            model.addAttribute( "dates3", new ArrayList<DoubleString>() );
            model.addAttribute( "dates4", new ArrayList<DoubleString>() );

            return "Такого круса нет";
        }
        //попали в эту точку при первом входе при нажатии кнопки "Планы"
        if (btn_out.isEmpty() && number == 0){
            List<DoubleString> dates = raspisanies.stream().map(rassp ->new DoubleString( rassp.getActiondate(), rassp.getCall().getName() ))
                    .collect(Collectors.toList());
            fillDates(dates, model);
            return "";
        }

        List<DoubleString> dates = new ArrayList<DoubleString>();
        if (btn_out.equals( "delete" )){
            String tekDate = utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) );
            int ind = 0;
            for (Raspisanie raspisanie : raspisanies){
                //System.out.println(ind + " raspisanie = " + raspisanie.getActiondate() + "  " + raspisanie.getCall().getName());
                ind++;
                if ((utilsController.compareDate( raspisanie.getActiondate(), calc_fysgc) >= 0)
                        && (utilsController.compareDate( raspisanie.getActiondate(), tekDate) >= 0)){
                    //System.out.println("save");
                    DoubleString item = new DoubleString( raspisanie.getActiondate(), raspisanie.getCall().getName() );
                    dates.add( item );
                }
            }
        } else {
            dates = raspisanies.stream().map( rassp -> new DoubleString( rassp.getActiondate(), rassp.getCall().getName() ) )
                    .collect( Collectors.toList() );
        }

        if ((number == 999) || (name.equals( "callId" ) || ((name.isEmpty()) && (number > 0)))
                || (btn_out.equals( "save" )) || (btn_out.equals( "delete" ))) {
            int periodInt = Period.getSemestr( periodId );
            if (btn_out.equals( "delete" )) {
                for (int i = 0; i < periodInt; i++){
                    if (i < dates.size()) {
                        DoubleString fordelete = dates.get( i );
                        Call call1 = callService.findByName( fordelete.getSecond() );
                        Raspisanie raspisanie = raspisanieService.findByActiondateAndCall( fordelete.getFirst(), call1 );
                        raspisanieService.delete( raspisanie );
                    }
                }
            } else {
                String[] dt = null;
                if (name.equals( "" )) {
                    name = utilsController.getDataNow();
                }
                //if (number == 999) {
                dt = calc_fysgc.split( "-" );
                //}
                Integer y = Integer.parseInt( dt[2] );
                int m = Integer.parseInt( dt[1] );
                int d = Integer.parseInt( dt[0] );
                Calendar calendar = new GregorianCalendar( y, m - 1, d, 17, 59, 0 );
                int counter = Period.getLength( periodId );
                for (int i = 0; i < counter; i++) {
                    Date dt1 = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
                    String tmpDate = sdf.format( dt1 );
                    dates.add( new DoubleString( tmpDate, call.getName() ) );
                    calendar.add( Calendar.DAY_OF_MONTH, periodInt );
                    if (!name.isEmpty() && (btn_out.equals( "save" ))) {
                        Raspisanie raspisanie = raspisanieService.save( tmpDate, i + 1, call, null, null );
                    }
                }
            }
            fillDates(dates, model);
        } else {
            getFromDB(model, call, course);
        }
        return null;
    }
    public List<DoubleString> sortedDates(List<DoubleString> dates){
        return dates.stream().sorted((o1, o2) -> {
            if (o1.getFirst().equals( o2.getFirst() )){
                return o1.getSecond().compareTo( o2.getSecond() );
            }
            String[] parm1 = o1.getFirst().split( "-" );
            String[] parm2 = o2.getFirst().split( "-" );
            if (parm1[2].equals( parm2[2] )){
                if (parm1[1].equals( parm2[1] )){
                    return parm1[0].compareTo( parm2[0] );
                }
                return parm1[1].compareTo( parm2[1] );
            }
            return parm1[2].compareTo( parm2[2] );
        }).collect(Collectors.toList());
    }
    public void fillDates(List<DoubleString> dates, Model model){
        dates = sortedDates(dates);
        List<DoubleString> dates1 = new ArrayList<DoubleString>();
        List<DoubleString> dates2 = new ArrayList<DoubleString>();
        List<DoubleString> dates3 = new ArrayList<DoubleString>();
        List<DoubleString> dates4 = new ArrayList<DoubleString>();
        for (int i = 0; i < dates.size(); i++) {
            if (i < 9) {
                dates1.add( dates.get( i ) );
            } else if (i < 18) {
                dates2.add( dates.get( i ) );
            } else if (i < 27) {
                dates3.add( dates.get( i ) );
            } else if (i < 36) {
                dates4.add( dates.get( i ) );
            }
        }

        model.addAttribute( "dates1", dates1 );
        model.addAttribute( "dates2", dates2 );
        model.addAttribute( "dates3", dates3 );
        model.addAttribute( "dates4", dates4 );

    }
    private void getFromDB(Model model, Call call, Course course){
        List<Raspisanie> raspisanies = null;// raspisanieService.findRaspisaniesByCall_AndCourse( call, course );
        if (raspisanies.size() > 0){
            List<DoubleString> dates1 = new ArrayList<DoubleString>();
            List<DoubleString> dates2 = new ArrayList<DoubleString>();
            List<DoubleString> dates3 = new ArrayList<DoubleString>();
            List<DoubleString> dates4 = new ArrayList<DoubleString>();
            for (int i = 0; i < raspisanies.size(); i++) {
                if (i < 9) {
                    dates1.add( new DoubleString( raspisanies.get( i ).getActiondate(), raspisanies.get( i ).getCall().getName() ) );
                } else if (i < 18) {
                    dates2.add( new DoubleString( raspisanies.get( i ).getActiondate(), raspisanies.get( i ).getCall().getName() ) );
                } else if (i < 27) {
                    dates3.add( new DoubleString( raspisanies.get( i ).getActiondate(), raspisanies.get( i ).getCall().getName() ) );
                } else if (i < 36) {
                    dates4.add( new DoubleString( raspisanies.get( i ).getActiondate(), raspisanies.get( i ).getCall().getName() ) );
                }
            }
            model.addAttribute( "dates1", dates1 );
            model.addAttribute( "dates2", dates2 );
            model.addAttribute( "dates3", dates3 );
            model.addAttribute( "dates4", dates4 );
        } else {
            model.addAttribute( "dates1", new ArrayList<DoubleString>() );
            model.addAttribute( "dates2", new ArrayList<DoubleString>() );
            model.addAttribute( "dates3", new ArrayList<DoubleString>() );
            model.addAttribute( "dates4", new ArrayList<DoubleString>() );
        }
    }

    public boolean ViewsFacYearSemGroupCourseFacDatePeriodTypezChastBtn(Model model, int facultatid, int yearId,
                                                                        int semestrId, int grouppId, int courseId,
                                                                        int callId, int periodid, int typezId, int gpgid,
                                                                        boolean dtStartDtEndVisible, Teacher teacher){
        Facultat facultat = getFacultat( model, facultatid );
        Year year = getYear( model, yearId );
        Semestr semestr = getSemestr( model, semestrId );//, year, facultat );
        if (semestr == null){
            model.addAttribute( "message", " Введите список предметов!" );
            return false;
        }
        Groupp groupp = null;// getGroupp( model, grouppId, typezId, semestr, year, facultat );
        Course course = getCourse( model, courseId, groupp, teacher );
        if (course == null){
            model.addAttribute( "message", " Введите список предметов!" );
            return false;
        }
        //String period = getPeroid( model, periodid );
        model.addAttribute( "typezs", UtilsController.getTypeZstrings() );
        model.addAttribute( "typezId",  typezId);
        model.addAttribute( "gpgs", UtilsController.getGPGs() );
        model.addAttribute( "gpgid",  gpgid);
        String dateFromPlan = utilsController.getDataNow();
        String timeFromPlan = utilsController.getTimeNow();
//24 07 2023        ForTeacherControllerOnestudent forTeacherControllerOnestudent =  null;//22 08 2022 utilsController.getListOfStudents( course, typezId, gpgid );
//24 07 2023        if (forTeacherControllerOnestudent != null) {
//24 07 2023            //18 08 2022  dateFromPlan = forTeacherControllerOnestudent.getDnevnikFullList().get( 0 ).getDnevnik().getPlan().getAction();
//24 07 2023        }
//24 07 2023        if (forTeacherControllerOnestudent != null) {
//24 07 2023            //18 08 2022  timeFromPlan = forTeacherControllerOnestudent.getDnevnikFullList().get( 0 ).getDnevnik().getPlan().getCall().getName();
//24 07 2023        }
        //model.addAttribute( "tdate", utilsController.getDataNow() );
        model.addAttribute( "tdate", dateFromPlan );
        getCall( model, callId );
        int tmFromPlan = callId;
//24 07 2023        if (forTeacherControllerOnestudent != null) {
//24 07 2023            //18 08 2022  tmFromPlan = Math.toIntExact( forTeacherControllerOnestudent.getDnevnikFullList().get( 0 ).getDnevnik().getPlan().getCall().getId() - 1 );
//24 07 2023        }
        //Call call  = forTeacherControllerOnestudent.getDnevnikFullList().get( 0 ).getDnevnik().getPlan().getCall();
        model.addAttribute( "callid", tmFromPlan );

        List<DoubleString>[] res = new ArrayList[4];
        for (int i =0; i < 4; i++){
            res[i] = new ArrayList<>();
        }
        int index = 0;
//24 07 2023        if (forTeacherControllerOnestudent != null) {
//24 07 2023            for (DnevnikFull dnevnikFull : forTeacherControllerOnestudent.getDnevnikFullList()) {
//24 07 2023                index++;
//24 07 2023                //18 08 2022  Plan plan = dnevnikFull.getDnevnik().getPlan();
//24 07 2023                //18 08 2022  if (index <= lenDates) {
//24 07 2023                //18 08 2022      res[0].add( new DoubleString( plan.getAction(), plan.getCall().getName() ) );
//24 07 2023                //18 08 2022  } else if (index <= lenDates * 2) {
//24 07 2023                //18 08 2022      res[1].add( new DoubleString( plan.getAction(), plan.getCall().getName() ) );
//24 07 2023                //18 08 2022  } else if (index <= lenDates * 3) {
//24 07 2023                //18 08 2022      res[2].add( new DoubleString( plan.getAction(), plan.getCall().getName() ) );
//24 07 2023                //18 08 2022  } else if (index <= lenDates * 3) {
//24 07 2023                //18 08 2022      res[3].add( new DoubleString( plan.getAction(), plan.getCall().getName() ) );
//24 07 2023                //18 08 2022  }
//24 07 2023            }
//24 07 2023        }
        model.addAttribute( "dates1", res[0] );
        model.addAttribute( "dates2", res[1] );
        model.addAttribute( "dates3", res[2] );
        model.addAttribute( "dates4", res[3] );
        return true;
    }
    public List<String> getPeroid(Model model, int periodId){
        List<String> periods = Period.getListWeek();
        if (model != null) {
            model.addAttribute( "periods", periods );
            model.addAttribute( "periodId", periodId );
        }
        return periods;
    }
    public List<DoubleString>[] calcDates(String calc_fysgc, int search_call_fysgc, int search_period_fysgc){
        List<DoubleString>[] res = new ArrayList[3];
        List<Call> calls = callService.findAll();
        List<DoubleString> dates = new ArrayList<>();
        int counter = 0;
        int periodInt = 0;
        switch (search_period_fysgc) {
            case 0:
                periodInt = 14;
                counter = 8;
                break;
            case 1:
                periodInt = 14;
                counter = 9;
                break;
            case 2:
                periodInt = 7;
                counter = 16;
                break;
            case 3:
                periodInt = 7;
                counter = 18;
                break;
        }
        //dates.add( tdateString );
        String[] dt = calc_fysgc.split( "-" );
        //String yy = dt[2];
        Integer y = Integer.parseInt( dt[2] );
        int m = Integer.parseInt( dt[1] );
        int d = Integer.parseInt( dt[0] );
        Calendar calendar = new GregorianCalendar( y, m - 1, d, 17, 59, 0 );
        for (int i = 0; i < counter; i++) {
            Date dt1 = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
            String tmpDate = sdf.format( dt1 );
            //int j = i+1;
            //dates.add( tmpDate + " " + calls.get( search_call_fysgc ).getName());
            dates.add( new DoubleString( tmpDate, calls.get( search_call_fysgc ).getName() ));
            calendar.add( Calendar.DAY_OF_MONTH, periodInt );
        }
        for (int i = 0; i < 3; i++){
            res[i] = new ArrayList<>();
        }
        if (dates.size() <= lenDates){
            res[0] = dates;
        } else {
            int startDates = 0;
            int endDates = lenDates;
            for (int i = 0; i < 3; i++) {
                if (startDates < dates.size()) {
                    res[i].addAll( dates.subList( startDates, endDates ) );
                    startDates += lenDates;
                    endDates += lenDates;
                }
            }
        }
        return res;
    }

    public void saveStudents() {
        List<Startdata> startdatas = startdataRepos.findStartdataByRole( "Student" );
        for (Startdata startdata : startdatas) {
            if (startdata.getRole().equals( "Student" )) {
                User user = new User();
                user.setUsername( translate(startdata.getLastname().getName())
                        + translate(startdata.getFirstname().getName().substring( 0, 1 ))
                        + translate(startdata.getSecondname().getName().substring( 0, 1 ))+"@ya.ru" );
                user.setRoles( Collections.singleton( Role.STUDENT ) );
                user.setActive( true );//??
                user.setPassword( passwordEncoder.encode( "Q!123456q" ) );
                User user2 = userService.save( user );

                Student student = new Student( startdata.getFirstname(), startdata.getSecondname(), startdata.getLastname(),
                        user2, startdata.getGroupp() );
            }
        }
    }
    private String translate(String parm){
        String rus = "1234567890АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЫЭЮЯабвгдежзийклмнопрстуфхцчшщыэюяьъ";
        String lat = "1234567890ABVGDEGZIKLMNOPRSTUFHCCSSYEUYabvgdegziiklmnoprstufhccssyeuy";
        String res = "";
        for (int i = 0; i < parm.length(); i++){
            char ch = parm.charAt( i );
            int j = rus.indexOf( ch );
            if ((j < lat.length()) && (ch != ' ')) {
                res += lat.charAt( j );
            }
        }
        return res;
    }

    public String savePlans(Teacher teacher) throws IOException {
        Reader reader = new FileReader( "Course.txt" );
        BufferedReader buffReader = new BufferedReader( reader );
        String lineStart = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        if (!lineStart.equals( "#Полное наименование~Краткое наим.r~год~группа~семестр~факультет~форма обуч.~ФИО" )) {
            return "settings";
        }
        int index = 0;
        int indCall= 0;
        while (buffReader.ready()) {
            if (indCall > 1){
                indCall = 0;
            }
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            String[] str = line.split( "~" );
            if (str[7].equals( "Бабкин Геннадий Викторович" )){
                Year year = null;//////!!! yearService.findByNameyear( str[2] );
                Facultat facultat = facultatService.findByNameAndForma( str[5], str[6] );
                Semestr semestr = semestrService.findByName( str[4] );
                Call call = null;
                switch (indCall) {
                    case 0:
                        call = callService.findByName( "22:29-23:59" );
                        break;
                    case 1:
                        call = callService.findByName( "20:55-22:25" );
                        break;
                }

                int counter = 18;
                List<String> dates = new ArrayList<>();
                int periodInt = 7;
                String tdateString = "08-02-2021";
                String[] dt = tdateString.split( "-" );
                //String yy = dt[2];
                Integer y = Integer.parseInt( dt[2] );
                int m = Integer.parseInt( dt[1] );
                int d = Integer.parseInt( dt[0] ) + index;
                Calendar calendar = new GregorianCalendar( y, m - 1, d );
                for (int i = 0; i < counter; i++) {
                    Date dt1 = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-YYYY" );
                    dates.add( sdf.format( dt1 ) );
                    calendar.add( Calendar.DAY_OF_MONTH, periodInt );
//!!!!!!                    Plan plan = new Plan(sdf.format( dt1 ), course, call, teacher, groupp, "лабораторная");
//!!!!!!                    planService.addPlan( plan );
                }


            }
            index++;
            indCall++;
        }
        return "";
    }

    public void prepareListStudents(User user, Model model) throws ParseException {
//24 07 2023        Plan plan = utilsController.getPlanTemp( user );
//24 07 2023        model.addAttribute( "plan", plan );
//24 07 2023        prepareOne( model, plan, 1L );
//24 07 2023        prepareOne( model, plan, 2L );
    }
    public void prepareOne(Model model, Plan plan, Long subgroup){
        List<Student> students = null;
        if (plan != null){
            Theme theme = plan.getTheme();//themeService.findByPlan( plan );
            List<Dnevnik> dnevniks = dnevnikService.findDnevniksByDateteacherAndPlan( null, plan );
            students = studentService.findByGrouppIdAndSubGroupp( plan.getGroupp(), subgroup );
            for (Student student : students) {
                //Long rs = 0L;
                //List<Dnevnik> dnevnikList = dnevnikService.findDnevniksByPlanCourse_IdAndStudent_Id( plan.getCourse(), student );
                List<Dnevnik> dnevnikList = dnevnikService.findByStudent_IdAndPlan_Id( student, plan );
                //24 07 2023  if ((dnevnikList.size() > 0) && (dnevnikList.get( 0 )).isIspresent()){
                //24 07 2023      //student.setSelect( 1 );
                //24 07 2023  }else {
                //24 07 2023      //student.setSelect( 2 );
                //24 07 2023  }
                //listStudents.add( new ForView( rs, student.getFN() ) );
            }
            model.addAttribute( "students"+subgroup.toString(), students );
            model.addAttribute( "theme", theme );
        } else {
            model.addAttribute( "students"+subgroup.toString(), null );
            model.addAttribute( "theme", null );
        }
    }

//24 07 2023    public List<DnevnikFull> prepareOneStudent(Model model, Plan plan, Student student) {
//24 07 2023        //Teacher teacher = teacherService.findTeacherByUser( user );
//24 07 2023        //String action = utilsController.getDataNow();
//24 07 2023        //List<Plan> plan = planService.findByActionAndTeacher_Id( action, teacher );
//24 07 2023        List<Plan> plans = planService.findByCourse_Id( plan.getCourse() );
//24 07 2023        List<DnevnikFull> dnevnikList = new ArrayList<>();
//24 07 2023        for (Plan plan1 : plans){
//24 07 2023            List<Dnevnik> dnevniks = dnevnikService.findByStudent_IdAndPlan_Id( student, plan1 );
//24 07 2023            if (dnevniks.size() > 0) {
//24 07 2023                for (Dnevnik dnevnik : dnevniks) {
//24 07 2023                    //18 08 2022  Theme theme = dnevnik.getPlan().getTheme();//themeService.findByPlan( dnevnik.getPlan() );
//24 07 2023                    //18 08 2022  if ((theme != null) &&(!StringUtils.isEmpty( theme.getZadanie() ))) {
//24 07 2023                    //18 08 2022      //dnevnikList.add( new DnevnikFull( dnevnik, theme.getNameteme(), theme.getZadanie() ) );
//24 07 2023                    //18 08 2022      dnevnikList.add( new DnevnikFull( dnevnik, "", theme.getNameteme(), theme.getZadanie() ) );
//24 07 2023                    //18 08 2022  }
//24 07 2023                }
//24 07 2023            }
//24 07 2023        }
//24 07 2023        //model.addAttribute( "student", student );
//24 07 2023        //model.addAttribute( "predmet", plan.getCourse().getNameCourseFull() );
//24 07 2023        //model.addAttribute( "dnevnikList", dnevnikList );
//24 07 2023        return  dnevnikList;
//24 07 2023    }
    public int findeMax(List<Plan> planList){
        int result = 0;
        for (Plan plan : planList){
            if (plan.getNumberoflab() > result){
                result = plan.getNumberoflab();
            }
        }
        return result;
    }
    public String getPath(String filename){
        // Корневая директория
        String rootDir = basepath ;
        List<Path> listPath = new ArrayList<Path>();
        try {
            // используя метод `Files.walk()`
            Files.walk( Paths.get( rootDir ) )
                    .filter( Files::isRegularFile )
                    .filter( it -> it.endsWith( filename ) )
                    .forEach( p -> listPath.add( p ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listPath.size() == 0){
            return "Файл " + filename + " не найден!";
        }
        String sss = listPath.get( 0 ).toString();//.replace( filename, "" );
        return sss;
    }
    //вызывается при входе преподавателя во время пары. Должен просмотреть всех студентов
    //текущей группы и если у них отсутствует дневниковая запись по одному из прошедших
    //занятий данного предмета создать ее
    public void fillDnevnik(User user, Model model, Raspisanie currentRaspisanie, List<Student> studentList) throws ParseException {
        //List<Theme> themeList = themeService.findThemesByCourse_IdAndTypezan(currentRaspisanie.getCourse(),
        //        currentRaspisanie.getTheme().getTypezan());
        List<Raspisanie> raspisanieList = raspisanieService.findByCourseAndLessActiondate(currentRaspisanie.getCourse(), currentRaspisanie.getActiondate());
        for (Student student : studentList){
            for (Raspisanie raspisanie : raspisanieList) {
                if (!raspisanie.getActiondate().equals(currentRaspisanie.getActiondate())) {
                    List<Dnevnik> dnevnikList = dnevnikService.findAllByRaspisanie_AndStudent(raspisanie, student);
                    if (dnevnikList.size() == 0){
                        Dnevnik dnevnik = dnevnikService.addDnevnik(false, student, raspisanie);
                        int kkk = 0;
                    }
                }
            }
        }
        int o = 0;
    }
    public String calcRasp(HttpSession session,
                           Model model,
                           String name,
                           int number,
                           Course course,
                           Call call,
                           int periodId,
                           String typez,
                           String btn_out,
                           String calc_fysgc,
                           boolean blockdate) throws IOException {
        //1. name: nplanes, number: 0, btn_out: "" - нажал кнопку "Планы"
        //2. name: новая дата, number: 999, btn_out: "", blockdate: true - выбрал новую дату при включенном флажке блокировки
        //3. name: новая дата, number: 999, btn_out: "", blockdate: false - выбрал новую дату при выключенном флажке блокировки
        //4. name: nplanedoubl, number: 0, btn_out: "save", blockdate: false - нажал кнопку "Сохранить"
        //5. name: callId, number: 7, btn_out: "", blockdate: false - выбрал "Время начала"
        //6. name: "", number: 0, btn_out: "delete", blockdate: false - выбрал "Удалить"
        //7. name: "periodId", number: 1, btn_out: "", blockdate: false - изменил период на "Раз в две недели (8)"
        //8. name: "courseId", number: 1, btn_out: "", blockdate: false - изменил "Дисциплину"
        //9. name: "typeZId", number: 1, btn_out: "", blockdate: false - изменил "Тип занятия"

        //List<String> blocking = new ArrayList<>();
        //blocking.add( "block" );
        //blocking.add( "not block" );
        //String blocking = "block";
        //        model.addAttribute( "blocking", blocking );
        List<Raspisanie> raspisanies = null;
        if (number == 999) {
            model.addAttribute("tdate", name);
        } else {
            if (!blockdate) {
                model.addAttribute("tdate", utilsController.getDataNow(new SimpleDateFormat("dd-MM-YYYY")));
            }
        }

        //Call call = teacherControllerService.getCall1( model, callId );
        if (course == null) {
            model.addAttribute("dates1", new ArrayList<DoubleString>());
            model.addAttribute("dates2", new ArrayList<DoubleString>());
            model.addAttribute("dates3", new ArrayList<DoubleString>());
            model.addAttribute("dates4", new ArrayList<DoubleString>());

            return "Такого круса нет";
        }

        boolean addnewdates = false;
        Call value = (Call) session.getAttribute("oldCall");
        //получим какое расписание на данный момент есть в базе
        raspisanies =  raspisanieService.findAllByCourse(course);
        if (raspisanies.size()==0) {
            if (value != null) {
                if ((!name.equals("callId")) && (!name.equals("nplanes"))) {
                    call = value;
                }
                raspisanies = raspisanieService.findAllByCallAndCourseEmpty(call)
                        .stream().filter(rasp -> rasp.getCourse() == null).map(rasp -> new Raspisanie(rasp.getId(), rasp.getActiondate(), rasp.getNumber(), "",
                                rasp.getCall(), rasp.getTheme(), rasp.getCourse())).collect(Collectors.toList());
            } else {
                //List<Raspisanie> raspisanies222 =raspisanieService.findAll();
                raspisanies = raspisanieService.findAll()
                        .stream().filter(rasp -> rasp.getCourse() == null).map(rasp -> new Raspisanie(rasp.getId(), rasp.getActiondate(), rasp.getNumber(), "",
                                rasp.getCall(), rasp.getTheme(), rasp.getCourse())).collect(Collectors.toList());
                if (raspisanies.size()>0) {
                    call = raspisanies.get(0).getCall();
                }
            }
            //.findAllRaspisaniesByActiondateAndCall(calc_fysgc, call);
            //.stream().map(rasp -> if (rasp.getCourse() == null) new Raspisanie(rasp. .getActiondate, number, "", call, theme, course));//.findAllRaspisaniesByActiondateAndCall(calc_fysgc, call);
        }
        QuickSort.quickSort(raspisanies, 0, raspisanies.size()-1);
        //teacherControllerService1.findRaspisanie(course, null, null);//raspisanieService.findRaspisaniesByCourse( course );
        //raspisanies = new ArrayList<>();
        if ((raspisanies.size() == 0 && !name.equals("nplanes") && !name.equals("typezId") && !name.equals("semestrId")
                && !name.equals("grouppId") && !name.equals("periodId") && !name.equals("courseId") && !blockdate)
                || (name.equals("callId")) || (btn_out.equals("save"))) {
            addnewdates = true;
        }
        List<DoubleString> dates = new ArrayList<DoubleString>();
        dates = raspisanies.stream().map(rasp -> new DoubleString(rasp.getActiondate(), rasp.getCall().getName()))
                .sorted((o1, o2)->o1.compareTo(o2)).collect(Collectors.toList());
        //dates = raspisanies.stream().map(rasp ->new DoubleString( rasp.getActiondate(), "rasp.getCall().getName()" ))
        //        .collect( Collectors.toList());

        int periodInt = Period.getLength(periodId);
        if (btn_out.equals("delete")) {
            String zvonok = call.getName();
            DoubleString yakor = new DoubleString(calc_fysgc, call.getName());
            dates = sortedDates(dates);
            List<DoubleString> datesStart = dates.stream().filter(dt -> dt.compareTo(yakor) < 0).collect(Collectors.toList());
            int j = 0;
            for (int i = datesStart.size(); i < dates.size(); i++) {
                DoubleString fordelete = dates.get(i);
                if (fordelete.getSecond().equals(yakor.getSecond()) && j < periodInt) {
                    j++;
                    Call call1 = callService.findByName(fordelete.getSecond());
                    if (call1 != null) {
                        Raspisanie raspisanie = raspisanieService.findByActiondateAndCall(fordelete.getFirst(), call1);

                        Long id = raspisanie.getId();
                        //raspisanie.setCourse( null );
                        raspisanie.setCall(null);
                        raspisanie.setTheme(null);
                        raspisanie = raspisanieService.update(raspisanie);
                        raspisanieService.delete(raspisanie);
                    }
                }
            }
            //raspisanies = raspisanieService.findRaspisaniesByCourse( course );
            dates = raspisanies.stream().map(rasp -> new DoubleString(rasp.getActiondate(), rasp.getCall().getName()))
                    .collect(Collectors.toList());
            //dates = raspisanies.stream().map(rasp ->new DoubleString( rasp.getActiondate(), "rasp.getCall().getName()" ))
            //        .collect(Collectors.toList());
            model.addAttribute("disabled", true);//кнопка пассивна
        } else {
            if ((addnewdates) || ((number == 999) && (!blockdate))) {// || (name.equals("dates"))) {
                if (value != null) {
                    raspisanies = raspisanieService.findAllByCallAndCourseEmpty(value)
                            .stream().filter(rasp -> rasp.getCourse() == null).map(rasp -> new Raspisanie(rasp.getId(), rasp.getActiondate(), rasp.getNumber(), "",
                                    rasp.getCall(), rasp.getTheme(), rasp.getCourse())).collect(Collectors.toList());
                    dates = raspisanies.stream().map(rasp -> new DoubleString(rasp.getActiondate(), rasp.getCall().getName()))
                            .sorted((o1, o2)->o1.compareTo(o2)).collect(Collectors.toList());
                    session.setAttribute("oldCall", value);
                } else {
                    session.setAttribute("oldCall", call);
                }
                dates.addAll(calculate_raspisanie(periodId, dates, call.getName(), calc_fysgc, periodInt));
                dates = dates.stream().sorted((o1, o2)->o1.compareTo(o2)).collect(Collectors.toList());
                model.addAttribute("disabled", false);//кнопка активна
            } else {
                model.addAttribute("disabled", true);//кнопка пассивна
                //Call call1 = call;
                //session.setAttribute("oldCall", /call1);
            }
            if (btn_out.equals("save")) {
                int index = 0;
                String nameGroupp = course.getGroupp().getNamegroupp();
                int indexSub = nameGroupp.indexOf(".");
                if (indexSub >= 0) {
                    nameGroupp = nameGroupp.substring(0, indexSub);
                }
                String grouppString = " ИВТ";
                if (nameGroupp.substring(1, 2).equals("9")) {
                    grouppString = " ИБ";
                }
                String namefile = nameGroupp + grouppString + " " + typez + " " + course.getNameCourseFull() + ".txt";
                String nmfile = getPath(namefile);
                Reader reader = new FileReader(nmfile);
                BufferedReader buffReader = new BufferedReader(reader);
                String[] startKolNumb = buffReader.readLine().trim().split(" ");

                String startKolNmbSt = startKolNumb[0].replace((char) 65279, ' ').trim();
                int startIndex = Integer.parseInt(startKolNmbSt);//Кол-во повторений в первой итерации главного цикла
                int firstCount = Integer.parseInt(startKolNumb[1]);// Кол-во итераций главного цикла
                int secondCount = 0;
                if (startKolNumb.length > 2) {// Если существует два цикла
                    secondCount = Integer.parseInt(startKolNumb[2]);
                }
                int firstIndex = 0;//Индекс главного цикла
                int secondIndex = firstCount;// Индекс второстепенного цикла
                boolean firstSecond = true;
                if (course.getKolzan() != dates.size()) {
                    for (DoubleString item : dates){
                        Call call1 = callService.findByName(item.getSecond());
                        Raspisanie raspisanieSave = raspisanieService.save(item.getFirst(), 0, call1, null, null);
                        //if (!(raspisanies.contains(raspisanieSave))) {
                        //    raspisanies.add(raspisanieSave);
                        //}
                        boolean found = false;
                        for (Raspisanie temp: raspisanies) {
                            if (temp.getId() == raspisanieSave.getId()){
                                found = true;
                            }
                        }
                        if (!found){
                            raspisanies.add(raspisanieSave);
                        }
                    }
                }else {
                    //while (firstIndex < firstCount) {
                    while (firstIndex <= firstCount + secondCount) {
                        if (startIndex > 0) {
                            if (firstIndex < dates.size()) {
                                //int indexFromDates = secondIndex;
                                //if (firstSecond){
                                //    indexFromDates = firstIndex;
                                //}
                                DoubleString item = dates.get(firstIndex);//Дата и время в расписании
                                index++;
                                Call call1 = null; //callService.findByName(item.getSecond());//Объект Call, содержащий дату и время в расписании
                                Theme theme = null;//themeService.findThemesByCourse_AndNumberAnd_Typezan(course, index, typez);
                                if (firstSecond) {
                                    theme = themeService.findThemesByCourse_AndNumberAnd_Typezan(course, firstIndex + 1, typez);
                                    call1 = callService.findByName(dates.get(firstIndex).getSecond());
                                    //System.out.println(dates.get(firstIndex).getFirst() + "  " + dates.get(firstIndex).getSecond()
                                    //        + " dates.size()=" + dates.size() + " firstIndex=" + firstIndex + " index=" + index + "theme=" + theme.getNameteme());
                                    Raspisanie raspisanieSave = raspisanieService.save(dates.get(firstIndex).getFirst(), index, call1, theme, course);
                                    raspisanies.add(raspisanieSave);
                                    theme.setZadanie(Integer.toString(index));
                                    themeService.update(theme);
                                    firstIndex++;
                                } else {
                                    if (dates.size() > secondIndex) {
                                        theme = themeService.findThemesByCourse_AndNumberAnd_Typezan(course, index, typez);
                                        call1 = callService.findByName(dates.get(firstIndex).getSecond());
                                        //System.out.println(dates.get(secondIndex).getFirst() + "  " + dates.get(secondIndex).getSecond()
                                        //+ " dates.size()=" + dates.size() + " firstIndex=" + firstIndex + " index=" + index + "theme=" + theme.getNameteme());
                                        Raspisanie raspisanieSave = raspisanieService.save(dates.get(firstIndex).getFirst(), index, call1, theme, course);
                                        raspisanies.add(raspisanieSave);
                                        theme.setZadanie(Integer.toString(index));
                                        themeService.update(theme);
                                        firstIndex++;
                                        int oo = 0;
                                    } else {
                                        //System.out.println(" " + " index=" + index);
                                    }
                                    secondIndex++;
                                }
                                if (startIndex < 3) {
                                    startIndex--;
                                }
                            } else {
                                firstIndex++;
                                int oooo = 0;
                            }
                        } else {//Переход к следующей итерации
                            firstSecond = !firstSecond;
                            if (firstSecond) {//firstCount > secondCount
                                startIndex = 2;
                            } else {
                                startIndex = 1;
                            }
                        }
                    }
                    raspisanies = raspisanieService.findAllByCourse(course);
                }
                //List<Theme> themeList = themeService.findThemesByCourse_IdAndTypezan(course, typez);
                //if (dates.size() == themeList.size()) {
                //for (DoubleString item : dates) {
                //    Call call1 = callService.findByName(item.getSecond());
                //    Theme theme = themeService.findThemesByCourse_AndNumberAnd_Typezan(course, index, typez);
                //    //raspisanieService.save(item.getFirst(), index, call1, theme, course);
                //    index++;
                //}
                //}
                //raspisanies = raspisanieService.findAllByCourse(course);
                //raspisanies = findRaspisanie(course, null, null);
                dates = raspisanies.stream().filter(rasp -> rasp.getCourse()!=null)
                        .map(rasp -> new DoubleString(rasp.getActiondate(), rasp.getCall().getName()))
                        .collect(Collectors.toList());
            }
        }
        model.addAttribute("periodId", periodId);
        model.addAttribute("periods", Period.getListWeek());
        //model.addAttribute( "disabled", true );
        fillDates(dates, model);
        model.addAttribute("tdatas", raspisanies);
        return "";
    }
    public String fillZadaniya(List<Theme> themeList) throws IOException {
        for (Theme theme : themeList){
            String pathShablon =  getPath(theme.getFileshablon());
            int iii = pathShablon.indexOf("не найден!");
            if (pathShablon.indexOf("не найден!") < 0) {
                List<String> keys = new ArrayList<>();
                Reader reader1 = new FileReader(pathShablon);
                BufferedReader buffReader1 = new BufferedReader(reader1);
                while (buffReader1.ready()) {
                    String line1 = buffReader1.readLine().trim().replaceAll("\\s{2,}", " ");
                    if ((!line1.equals("")) && (!line1.substring(0, 1).equals("#"))) {
                        while (line1.length() > 0) {
                            int indexStart = line1.indexOf("{") + 1;
                            int indexEnd = line1.indexOf("}");
                            if (indexStart == 0) {
                                break;
                            }
                            keys.add(line1.substring(indexStart, indexEnd));
                            line1 = line1.substring(indexEnd + 1);
                        }
                    }
                }
                String pathLab = getPath(
                        theme.getCourse().getGroupp().getNamegroupp()
                                + theme.getFileshablon().replace("exercise_", " "));
                if (pathLab.indexOf("не найден!") > 0) {
                    return pathLab;
                }
                //if (pathLab.equals("2022-2023\\Osen\\3 к ИВТ\\1 sem lab Sety\\310 seti_lab8.txt")){
                //    int asdfg=0;
                //}
                Reader reader = new FileReader(pathLab);
                BufferedReader buffReader = new BufferedReader(reader);
                while (buffReader.ready()) {
                    String line = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
                    if ((!line.equals("")) && (!line.substring(0, 1).equals("#"))) {
                        String[] lineArr = line.split("~");
                        String[] fio = lineArr[0].split(" ");
                        if (fio.length < 3) {
                            return "Фамилия Имя Отчетство должны быть представлены полностью в файле: " + pathLab;
                        }
                        Firstname firstname = firstnameService.findFirstnameByName(fio[1]);
                        Secondname secondname = secondnameService.findSecondnameByName(fio[2]);
                        Lastname lastname = lastnameService.findLastnameByFamily(fio[0]);
                        Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
                                firstname, secondname, lastname);
                        //27 11 2022   int i = 1;
                        for (String parm : keys) {
                            //Exercise exercise = new Exercise(parm, lineArr[i], theme, startdata);
                            //27 11 2022   exerciseService.save(parm, lineArr[i], theme, startdata);
                            exerciseService.save(parm, lineArr[1], theme, startdata);
                            //27 11 2022   i++;
                        }
                    }
                }
                Writer writer = new FileWriter(pathantwer + "logForLek.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                writer.append(pathLab + "\n");
                writer.close();
            }
        }
        return null;
    }
    public List<Raspisanie> findRaspisanie(Course course, String actionDate, String call){
        List<Theme> themeList = themeService.findAllByCourse(course);
        List<Raspisanie> raspisanieList = new ArrayList<>();
        for (Theme theme : themeList) {
            List<Raspisanie> raspisanieListTemp = raspisanieService.findRaspisanieByTheme(theme);
            for (Raspisanie raspisanie : raspisanieListTemp) {
                if ((raspisanie != null) && (!raspisanieList.contains(raspisanie))) {
                    if (actionDate == null) {
                        raspisanieList.add(raspisanie);
                        //raspisanieList.sort(Comparator<Raspisanie> raspisanie)//
                    } else {
                        if (raspisanie.getActiondate().equals(actionDate)
                                && (call == null) || (raspisanie.getCall().getName().equals(call))) {
                            raspisanieList.add(raspisanie);
                        }
                    }
                }
            }
        }
        //17 08 23 Comparator<Raspisanie> comp = (a, b) -> {
        //17 08 23     try {
        //17 08 23         return Raspisanie.compare(a,b);
        //17 08 23     } catch (ParseException e) {
        //17 08 23         throw new RuntimeException(e);
        //17 08 23     }
        //17 08 23 };
        //17 08 23 raspisanieList.sort(comp);
        //QuickSort.quickSort(raspisanieList, 0, raspisanieList.size()-1);

        return raspisanieList;
    }
    public void proverka(Model model, @AuthenticationPrincipal User user, String dttm, String forProverka) throws ParseException {
        String dt = dttm.split(" ")[0];
        String[] dtArr = dt.split("-");
        String[] yst = basepath.split("\\\\");
        String[] yearsString = yst[yst.length-1].split("-");
        Year year = yearService.findByFirstnameyearAndSecondnameyear(yearsString[0], yearsString[1]);
        String[] strArr = null;
        List<Semestr> semArr = new ArrayList<>();
        int month = Integer.parseInt(dtArr[1]);
        if (month >8){//Вычисляем осенний или весенний семестр
            strArr = new String[]{"1", "3", "5", "7"};
        } else {
            strArr = new String[]{"2", "4", "6", "8"};
        }
        Facultat facultat = facultatService.findByNameAndForma("ФМИ", "ОФО");
        List<Groupp> groupps = new ArrayList<>();
        for (String sm : strArr){
            Semestr ssm = semestrService.findByName(sm);
            if (ssm != null) {
                List<Groupp> grouppList = groupService.findGrouppsByFacultat_AndSemestr_AndYear(facultat, ssm, year);
                groupps.addAll(grouppList);
            }
        }
        List<Course> courseList = new ArrayList<>();
        Teacher teacher = teacherService.findTeacherByUser(user);
        for (Groupp groupp : groupps) {
            courseList.addAll(courseService.findByGrouppAndTeacher(groupp, teacher));
        }
        List<Theme> themeList = new ArrayList<>();
        for (Course course : courseList){
            List<Theme> lst = themeService.findAllByCourse(course);
            themeList.addAll(themeService.findAllByCourse(course));
        }
        List<Raspisanie> raspisanieList = new ArrayList<>();
        for (Theme theme : themeList){
            List<Raspisanie> raspisanieListTemp = raspisanieService.findRaspisanieByTheme(theme);
            for (Raspisanie raspisanie : raspisanieListTemp) {
                if (utilsController.compareDate(raspisanie.getActiondate(), dt) <= 0) {
                    raspisanieList.add(raspisanie);
                }
            }
        }
        List<Dnevnik> dnevnikList = new ArrayList<>();
        for (Raspisanie raspisanie : raspisanieList){
            dnevnikList.addAll(dnevnikService.findAllByRaspisanie(raspisanie));
        }
        List<FilesForDnevnik> filesForDnevniks = new ArrayList<>();
        for (Dnevnik dnevnik : dnevnikList){
            List<FilesForDnevnik> filesForDnevniks1 =
                    filesForDnevnikService.findAllByDnevnik(dnevnik, FilesForDnevnikEnum.студент_сдал);
            if (filesForDnevniks1 != null && filesForDnevniks1.size()>0){
                //filesForDnevniks.addAll(filesForDnevnik);
                for (FilesForDnevnik filesForDnevnik1 : filesForDnevniks1){
                    if (filesForDnevnik1.getOcenka() != null) {
                        if (!filesForDnevnik1.getOcenka().equals("Проверено, замечаний нет.")
                                && !filesForDnevnik1.getOcenka().equals("Не отвечал")) {
                            //filesForDnevniks.add(filesForDnevnik1);
                        }
                    } else {
                        if (forProverka.equals("Ответ преподавателя готов к отправке")) {
                            filesForDnevniks.add(filesForDnevnik1);
                        }
                    }
                }
            }
        }
        int o = 0;
        model.addAttribute("filesForDnevniks", filesForDnevniks);
    }
    public List<Raspisanie> findByActiondateCallTeacher(String actionDate, Call callNow, Teacher teacher){
        List<Raspisanie> raspisanieList = new ArrayList<>();
        List<Course> courseList = courseService.findByTeacher(teacher);
        for (Course course : courseList) {
            List<Raspisanie> res = findRaspisanie(course, actionDate, callNow.getName());
            if (res.size()>0) {
                raspisanieList.addAll(res);
            }
        }
        QuickSort.quickSort(raspisanieList, 0, raspisanieList.size()-1);
        return raspisanieList;
    }
}
