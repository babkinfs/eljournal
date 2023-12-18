package com.babkin.eljournal.controllers;

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.service.*;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UtilsController {

    public static int facultatIdInit = 0;
    public static int yearIdInit = 0;
    public static int semestrIdInit = 1;
    public static int groupIdInit = 0;
    public static int courseIdInit = 0;
    public static int callIdInit = 7;
    public static int typezIdInit = 1;
    public static int gpgidInit = 0;
    public static int periodidInit = 8;
    public static int subgrouppidInit = 0;
    public static boolean dtStartDtEndVisibleInit = true;

//    public static String getPath(){
//        return "FillDB 2021 2022";
//    }
    @Value("${base.path}")
    private String basepath;

    @Autowired
    private CallService callService;
    public String getNameOfFile(int index){
        String[] filenames = new String[]{
                "Standart",
                "control",
                "Lexema",
                "Teacher",
                "Student",
                "Course",
        };
        String[] parm = basepath.split("-");
        String res = parm[0].substring(parm[0].length()-2, parm[0].length())+parm[1].substring(parm[1].length()-2, parm[1].length());
        return filenames[index] + res + ".txt";
    }

    Map<String, Long> mapInit = new HashMap<String, Long>();

    public void addInit(String name, Long body){
        mapInit.put(name, body);
    }

    public Long getFomInit(String parm){
        if (mapInit.containsKey(parm)) {
            return mapInit.get( parm );
        } else {
            return 0L;
        }
    }

    private static String[] TypeZstrings = new String[]{"лекция", "лабораторная"};

    public static String[] getTypeZstrings() {
        return TypeZstrings;
    }

    public static String getTypeZbyIndex(int index, int numberoflab) {
        if (numberoflab == 0){
            return TypeZstrings[index].substring( 0, 3 );
        } else {
            return TypeZstrings[index].substring(0, 3) + " №" + numberoflab;
        }
    }
    public static String getTypeZbyIndex(int index) {
        return TypeZstrings[index].substring( 0, 3 );
    }

    public static String[] GPG = new String[]{"полная группа", "первая подгруппа", "вторая подгруппа", "третья подгруппа"};

    public static String[] getGPGs() {
        return GPG;
    }

    public static String getGrpbyIndex(int index) {
        return GPG[index];
    }

    @Autowired
    private TeacherControllerService teacherControllerService;
    @Autowired
    private TeacherService teacherService;
//    @Autowired
//    private StudentControllerService studentControllerService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PlanService planService;
    @Autowired
    private DnevnikService dnevnikService;

    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect( collector );
    }

    public String getTempSemestrName(Groupp groupp){
        String[] mesats = getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) ).split("-");
        int semestrInYear = 1;
        if (mesats[1].equals("02") || mesats.equals("03") || mesats.equals("04") || mesats.equals("05") || mesats.equals("06")){
            semestrInYear = 2;
        }
        switch (groupp.getNamegroupp().substring(0,1)){
            case "1" : break;
            case "2" : semestrInYear += 2; break;
            case "3" : semestrInYear += 4; break;
            case "4" : semestrInYear += 6; break;
            case "5" : semestrInYear += 8; break;
        }
        return Integer.toString(semestrInYear);

    }
    public DateTime getDateFill() {
        return DateTime.now();
    }

    public  String getDataNow(){
        return getDataNow(new SimpleDateFormat("dd-MM-yyyy") );
    }
    private Calendar calendar;
    public  String getDataNow(SimpleDateFormat dateFormat){
        calendar = new GregorianCalendar(2023, Calendar.SEPTEMBER, 18 );//4 //11 //18
        calendar.set(Calendar.HOUR, 8);//8 9 11 13
        calendar.set(Calendar.MINUTE, 42);
        calendar.set(Calendar.SECOND, 12);
        return dateFormat.format(calendar.getTime());
    }
    public  String getTimeNow(){
        String rs = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        //.getTime().toString().split(" ")[3];
        return rs;// "11:25";//"13:55";//"09:46";//DateTime.now().toString("HH:mm");//"16:55";//"12:25";//
    }//

    public int subDates(String first, String last) throws ParseException {
        Date firstDate = parseDate( first, "dd-MM-yyyy");
        Date lastDate = parseDate( last, "dd-MM-yyyy");
        return Days.daysBetween(new DateTime(lastDate), new DateTime(firstDate)).getDays();
    }




    public Plan getPlanTemp(User user) throws ParseException {
        String date = getDataNow();
        String time = getTimeNow();
        Teacher teacher = teacherService.findTeacherByUser( user );
        List<Plan> plans = null;
        if (teacher != null) {
            plans = planService.findByActionAndTeacher_Id( date, teacher );
        }
        Student student = studentService.findByUser( user );
        if (student != null){
            //Получить расписание для студентов заданной группы на текущую дату
            //List<Raspisanie> raspisanieList = studentControllerService.getRaspisaniebYGroupp(date, student.getGroupp(), null);
            plans = null;// planService.findByActionAndGroupp( date, student.getGroupp() );
        }
        Plan pok = null;
        Plan ptmp = null;
        boolean ok = false;
        if (plans != null) {
            for (Plan plan : plans) {
                //-1 до пары   0 во время пары   +1 после пары
                int tester = comparison( plan.getCall().getName(), time );
                if (tester == 0) {
                    pok = plan;
                }
                if (tester == -1) {
                    if (ptmp == null) {
                        ptmp = plan;
                    } else {
                        time = ptmp.getCall().getName().split( "-" )[0];
                        tester = comparison( plan.getCall().getName(), time );
                        if (tester == 1) {
                            ptmp = plan;
                        }
                    }
                    ok = true;
                }
            }
        }
        if (pok == null){
            return ptmp;
        }
        return pok;
    }


    public int compareDate(String first, String last) throws ParseException {//res = 0 if == ; >0 if first>last
        Date firstDate = parseDate( first, "dd-MM-yyyy");
        Date lastDate = parseDate( last, "dd-MM-yyyy");
        if (first.equals( last)){
            return 0;
        }
        if (firstDate.getTime() > lastDate.getTime()){
            return 1;
        } else {
            return -1;
        }
    }
    private Date parseDate(String date, String format) throws ParseException  {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

    public int compareCall(Call firstCall, Call secondCall){
        String[] firstArr = firstCall.getName().split("-");
        String[] secondArr = secondCall.getName().split("-");
        int first = Integer.parseInt(firstArr[0]);
        int second = Integer.parseInt(secondArr[0]);
        return first - second;
    }

    String findeCurrentCall(String timeNow){
        for (Long i=1L; i<9; i++){
            Call call = callService.findCallById(i);
            if (comparison(call.getName(), timeNow) == 0){//Найдено
                return call.getName();
            }
        }
        return "";
    }
    public int comparison(String call, String time){
        //-1 до пары
        //0 во время пары
        //+1 после пары
        String[] arr = call.split( "-" );
        int first = timeToInt( arr[0] );
        int last = timeToInt( arr[1] );
        int temp = timeToInt( time );
        if (temp < first){
            return -1;
        }
        if (temp > last){
            return 1;
        }
        return 0;
    }
    public String addDays(String date, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //Date oldDate = sdf.parse(date);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);
        String output = sdf.format(c.getTime());
        return output;
    }
    private int timeToInt(String parm){
        String[] arr = parm.split( ":" );
        int res = Integer.parseInt( arr[0] )*60;
        res += Integer.parseInt( arr[1] );
        return res;
    }

    public void studentSelected(Plan plan, Model model, Long subgroup) {
        if (plan != null) {
            List<Student> students1 = studentService.findByGrouppIdAndSubGroupp( plan.getGroupp(), subgroup );
            for (Student studenttmp : students1){
                //List<Dnevnik> dnevnik = dnevnikService.findDnevniksByPlanCourse_IdAndStudent_Id( plan.getCourse(), studenttmp );
                List<Dnevnik> dnevnik = dnevnikService.findByStudent_IdAndPlan_Id( studenttmp, plan );
                if ((dnevnik.size() > 0) && (dnevnik.get( 0 )).isIspresent()){
//                    studenttmp.setSelect( 1 );
                } else {
//                    studenttmp.setSelect( 2 );
                }
            }
            model.addAttribute( "students" + subgroup.toString(), students1 );
        } else {
            model.addAttribute( "students" + subgroup.toString(), null );
        }
    }
    public static String prepareStr(String parm){
        String[] rs = parm.split( " " );
        String[] rs1 = rs[0].split( "-" );
        if (rs.length == 1){
            return rs1[2] + rs1[1] + rs1[0];
        }
        return rs1[2] + rs1[1] + rs1[0] + rs[1] + rs[2];
    }
    public List<Plan> planSorted(List<Plan> plans){
        String action = prepareStr(plans.get( 0 ).getAction());// + " " + plans.get( 0 ).getCall().getName());
        List<Plan> res = new ArrayList<>();
        return res;
    }
//24 07 2023    @Nullable
//24 07 2023    public ForTeacherControllerOnestudent getListOfStudents(Course course, String typez, int gpg) {
//24 07 2023        List<Plan> planList = null;// 22 08 2022 planService.findPlanByTypezAndCourse_IdAndGpg( course, typez, gpg );
//24 07 2023        List<DnevnikFull> dnevnikFullList = new ArrayList<>();
//24 07 2023        ForTeacherControllerOnestudent forTeacherControllerOnestudent = null;
//24 07 2023        return forTeacherControllerOnestudent;
//24 07 2023
//24 07 2023    }
}
