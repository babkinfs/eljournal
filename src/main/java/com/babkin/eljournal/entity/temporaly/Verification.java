package com.babkin.eljournal.entity.temporaly;

import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

@Service
public class Verification {
    @Value("${base.path}")
    private String basePath;

    @Autowired
    private TeacherControllerService teacherControllerService;

    @Autowired
    private FirstnameService firstnameService;
    @Autowired
    private SecondnameService secondnameService;
    @Autowired
    private LastnameService lastnameService;
    @Autowired
    private FacultatService facultatService;
    @Autowired
    private YearService yearService;
    @Autowired
    private SemestrService semestrService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private SubgrouppService subgrouppService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ShabloncourseService shablonCourseService;
    @Autowired
    private StandartService standartService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private RaspisanieService raspisanieService;
    @Autowired
    private CallService callService;
    //public Verification(StandartService standartService){
    //    this.standartService = standartService;
    //}
//    @Autowired
//    private CourseService courseService;
//    @Autowired
//    private UtilsController utilsController;

    private List<String> yearsDop = new ArrayList<>();
    private List<String> groupsDop = new ArrayList<String>();
    private List<String> facsDop = new ArrayList<>();
    private List<String> frmsDop = new ArrayList<String>();
    private List<String> semDop = new ArrayList<String>();
    private List<String> emailDop = new ArrayList<String>();

    private List<String> yearsReal = new ArrayList<String>();
    private List<String> groupsReal = new ArrayList<String>();
    private List<String> facsReal = new ArrayList<String>();
    private List<String> frmsReal = new ArrayList<String>();
    private List<String> semReal = new ArrayList<String>();

    public void clearMailDop(){
        emailDop.clear();
    }

    private List<String> lexemaDop = new ArrayList<>();

    public String makeTeacher(String fileName, Model model, String retName) throws IOException {
        String fullFileName = fileName;
        Reader reader = new FileReader( fullFileName );
        BufferedReader buffReader = new BufferedReader( reader );
        String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if ((!line.equals( "" )) && (!line.substring( 0, 1 ).equals( "#" ))) {
                String[] lineArr = line.split( "~" );
                Firstname firstname = firstnameService.saveIntoFirstname( lineArr[1] );
                Secondname secondname = secondnameService.findSecondnameByName( lineArr[2] );
                Lastname lastname = lastnameService.findLastnameByFamily( lineArr[0] );
                Teacher teacher = new Teacher(firstname, secondname, lastname, null);
                teacherService.save( teacher );
            }
        }
        return null;
    }

    public String testStudent(String fileName, Model model, String retName) throws IOException {
        String fullFileName = fileName;
        Reader reader = new FileReader( fullFileName );
        BufferedReader buffReader = new BufferedReader( reader );
        String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if ((!line.equals( "" )) && (!line.substring( 0, 1 ).equals( "#" ))) {
                String[] lineArr = line.split( "~" );
                Firstname firstname = firstnameService.saveIntoFirstname( lineArr[1] );
                Secondname secondname = secondnameService.findSecondnameByName( lineArr[2] );
                Lastname lastname = lastnameService.findLastnameByFamily( lineArr[0] );
                Teacher teacher = new Teacher(firstname, secondname, lastname, null);
                teacherService.save( teacher );
            }
        }
        return null;
    }


    public void makeVarLab(String filename, ModelForStart modelForStart) throws IOException {
        //Запись сгенерированных вариантов заданий по лаб в базу данных
        List<String> listShablon = new ArrayList<>();

        Reader reader = new FileReader(filename);
        BufferedReader buffReader = new BufferedReader(reader);
        String startKolNumb = buffReader.readLine();
        String path = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
        String allShablon = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
        Reader reader2 = new FileReader(basePath + "\\" + allShablon);
        BufferedReader buffReader2 = new BufferedReader(reader2);
        //Работа с шаблоном общим для курса
        while (buffReader2.ready()) {
            String ln = buffReader2.readLine();
            String line2 = ln.trim().replaceAll("\\s{2,}", " ");
            Shabloncourse shablonCourse = shablonCourseService.save(line2, modelForStart.getTypeZ(), modelForStart.getCourse());
        }
        buffReader2.close();
        while (buffReader.ready()) {
            String line0 = buffReader.readLine().trim();
            if (!line0.equals("")) {
                listShablon.clear();
                String[] lineArr = line0.replaceAll("\\s{2,}", " ").split("~");
                if (!lineArr[3].equals("@")) {
                    String[] body = lineArr[3].split("@");
                    String nameTheme = body[0];
                    Theme theme = null;
                    int number = Integer.parseInt(lineArr[1]);
                    if (modelForStart.getTypeZ().equals("лекция")) {
                        theme = themeService.save(nameTheme, "", number, modelForStart.getTypeZ().substring(0, 3),
                                "", lineArr[2], modelForStart.getCourse());
                    } else {
                        String parm = "";
                        if (body.length>1){
                            parm =  body[1];
                        }
                        theme =  themeService.save(nameTheme, "", number, modelForStart.getTypeZ().substring(0, 3),
                                parm, lineArr[2], modelForStart.getCourse());
                    }
                    if (lineArr.length > 1) {
                        if (body.length > 1) {
                            String fullName = basePath + "\\" + path + "\\" + body[1];
                            //Работа с шаблонами тем
                            Reader reader1 = new FileReader(fullName);
                            BufferedReader buffReader1 = new BufferedReader(reader1);
                            while (buffReader1.ready()) {
                                String line = buffReader1.readLine().trim().replaceAll("\\s{2,}", " ");
                                String[] parms = line.split("}");
                                //Получение списка шаблонов
                                for (String tmp : parms) {
                                    int start = tmp.indexOf("${");
                                    if (start >= 0) {
                                        String data = tmp.substring(start + 2);
                                        //if (listShablon.contains(data)) {
                                        //    listShablon.remove(0);
                                        //    listShablon.add(0, "В файле-шаблоне \"" + fileNameStudents + "\" повторяется параметр \"" + data + "\"");
                                        //    String err = listShablon.get(0);
                                        //    return listShablon;
                                        //}
                                        listShablon.add(data);
                                    }
                                }
                            }
                            buffReader1.close();
                            //Ищем файл со сгенерированными заданиями
                            String namefilezadan = basePath + "\\" + path + "\\"
                                    //+ modelForStart.getGroupp().getNamegroupp()
                                    + body[1].replace("exercise_", " ").replaceAll("  ", " ");
                            Reader reader3 = new FileReader(namefilezadan);
                            BufferedReader buffReader3 = new BufferedReader(reader3);
                            //Работа с шаблоном общим для курса
                            while (buffReader3.ready()) {
                                String line3 = buffReader3.readLine().trim().replaceAll("\\s{2,}", " ");
                                String[] fromShablon = line3.split("~");
                                String[] fio = fromShablon[0].split(" ");
                                Firstname firstname = firstnameService.findFirstnameByName(fio[1]);
                                Secondname secondname = secondnameService.findSecondnameByName(fio[2]);
                                Lastname lastname = lastnameService.findLastnameByFamily(fio[0]);
                                Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(firstname, secondname, lastname);
                                int index = 1;
                                for (String st : listShablon) {
                                    Exercise exercise = exerciseService.save(st, fromShablon[index], theme, startdata);
                                    index++;
                                }
                                int p = 0;
                            }
                            buffReader3.close();
                        }
                    }
                }
            }
        }
        buffReader.close();

    }

    private List<ForLekLab> forLekLab;
    //private List<String> firstArr;
    //private List<String> secondArr;
    private String fullFileName;
    //private String[] startKolNumb;


    //Проверка файла на корректность
    public String readDataFromLekLab(String fileName) throws IOException {//}, ModelForStart modelForStart) throws IOException {
        if (fileName.equals( "" )){
            return "Такого файла нет!";
        }
        forLekLab = new ArrayList<ForLekLab>();
        // UtilsController.getPath() + "\\" + fileName;//"Student.txt";
        fullFileName = "";
        Reader reader = new FileReader( fileName );
        BufferedReader buffReader = new BufferedReader( reader );
        String startKolNumb = buffReader.readLine();
        Call call = callService.update(10L, startKolNumb);
        String startPath = buffReader.readLine();
        String templateAddress = buffReader.readLine();
        if (!templateAddress.toLowerCase( Locale.ROOT ).contains( "шаблон" )){
            return "Отстутствует строка шаблона";
        }
        //firstArr = new ArrayList<>();
        //secondArr = new ArrayList<>();
        //int firstArrLen = Integer.parseInt(startKolNumb[1]);
        int count = 0;
        //String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        //String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            //if ((startKolNumb.length>2) && firstArrLen > 0){
            //    firstArr.add(line);
            //    firstArrLen--;
            //} else {
            //    secondArr.add(line);
            //}
            String[] arr = line.split( "~" );
            if (!line.equals( "" )) {
                int number = 0;
                try {
                    number = Integer.parseInt( arr[1].trim() );
                    count++;
                    if (count != number) {
                        return "Нарушена последовательность работ в строке " + line;
                    }
                } catch (Exception ex) {
                    return "Ошибка номера в строке " + line;
                }

                if (!arr[0].equals( "лк" ) && !arr[0].equals( "лб" )) {
                    return "Тип занятия должен быть \"лк\" или \"лб\" в строке " + line;
                }
                fullFileName = (basePath + "\\" + startPath + "\\" + arr[2]).replaceAll("  ", " ");// + ".docx";
                if ((fileName.contains("лаб")) && (arr[2].length() > 0)) {
                    File file = new File( fullFileName );
                    if (!file.exists()) {
                        return "Отсутствует файл \"" + fullFileName + "\" в строке \"" + line + "\"";
                    }
                }
                String[] zadan = arr[3].split("@");
                if (zadan.length==2) {
                    fullFileName = basePath + "\\" + startPath + "\\" + zadan[1];
                    File file1 = new File(fullFileName);
                    if (!file1.exists()) {
                        return "Отсутствует файл \"" + fullFileName + "\" в строке \"" + line + "\"";
                    }
                    Map<Integer, String> listShablon = new HashMap<>();
                    Reader reader1 = new FileReader(fullFileName);
                    BufferedReader buffReader1 = new BufferedReader(reader1);
                    while (buffReader1.ready()) {
                        String line1 = buffReader1.readLine().trim().replaceAll("\\s{2,}", " ");
                        String[] parms = line1.split("}");
                        int linenumber = 0;
                        for (String tmp : parms) {
                            int start = tmp.indexOf("${");
                            if (start >= 0) {
                                String data = tmp.substring(start + 2);
                                int index = Integer.parseInt(data.substring(0, data.indexOf("_")));
                                if (linenumber != 0){
                                    if (!listShablon.containsKey(index-1) || (listShablon.containsKey(index))) {
                                        return "Нарушена последовательность индексов в шаблоне \"" + fullFileName + "\" в строке \"" + line + "\"";
                                    }
                                }
                                linenumber++;
                                listShablon.put(index, data);
                            }
                        }
                    }
                    int p = 0;
                }
                String[] thZ = arr[3].split( "@" );
                if (thZ.length == 1) {
                    forLekLab.add( new ForLekLab( arr[0], number, arr[2], thZ[0], "", templateAddress ) );
                } else if (thZ.length != 0) {
                    forLekLab.add( new ForLekLab( arr[0], number, arr[2], thZ[0], thZ[1], templateAddress ) );
                }
            }
        }

        //List<Raspisanie> list = null;// raspisanieService.findRaspisaniesByCourse( modelForStart.getCourse() );
        //if (list.size() != count){
        //    return "Количество заданий в файле " + fileName + " не соответствует количеству дней в расписании";
        //}
        return null;
    }

    public List<Exercise> showVarLab(ModelForStart modelForStart){
        Course course = modelForStart.getCourse();
        List<Theme> themeList = themeService.findAllByCourse(course);
        if (themeList.size()==0){
            return new ArrayList<>();
        }
        Groupp groupp = modelForStart.getGroupp();
        List<Startdata> startdataList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Subgroupp subgroupp = subgrouppService.findSubgrouppByNamesubgroupp(Integer.toString(i));
            String nameGroupp = groupp.getNamegroupp();
            //Groupp groupp1 = groupService.findGrouppByNamegrouppAndSubgroupp(nameGroupp, subgroupp);
            Groupp groupp1 = groupService.findGrouppByNamegrouppAndFacultat_AndSemestr_AndYear_AndSubgroupp(
                    groupp.getNamegroupp(), groupp.getFacultat(), groupp.getSemestr(), groupp.getYear(), subgroupp);
            if (groupp1 != null) {
                List<Startdata> startdataList1 = startdataService.findStartdataByGroupp(groupp1);
                if (startdataList1.size() > 0) {
                    startdataList.addAll(startdataList1);
                }
            }
        }

        if (startdataList.size()==0){
            return new ArrayList<>();
        }
        List<Exercise> exerciseList = exerciseService.findAllByTheme_AndStartdata(themeList.get(0), startdataList.get(0));
        for (int i=0; i<themeList.size(); i++){
            for (int j=1; j<startdataList.size(); j++){
                exerciseList.addAll(exerciseService.findAllByTheme_AndStartdata(themeList.get(i), startdataList.get(j)));
            }
        }
        //exerciseList.addAll(exerciseService.findAllByTheme_AndStartdata(themeList.get(0), startdataList.get(1)));
        //exerciseList.addAll(exerciseService.findAllByTheme_AndStartdata(themeList.get(1), startdataList.get(0)));
        //exerciseList.addAll(exerciseService.findAllByTheme_AndStartdata(themeList.get(1), startdataList.get(1)));

        return exerciseList;
    }
    public String saveStudentsTeashers(String fileName, Model model, String retName) throws IOException {
        List<String> testGroupp = new ArrayList<>();
        String role = "TEACHER";
        int indexLineArr = 3;
        if (fileName.contains( "Student" )){
            role = "STUDENT";
            indexLineArr = 4;
        }
        String fullFileName = fileName;//UtilsController.getPath() + "\\" + fileName;//"Student.txt";
        Reader reader = new FileReader( fullFileName );
        BufferedReader buffReader = new BufferedReader( new FileReader( new File(fullFileName).getAbsoluteFile()) );
        String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        String format2 = "";
        if (fileName.contains( "Student" )) {
            format2 = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            format2 = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        }
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if ((!line.equals( "" )) && (!line.substring( 0, 1 ).equals( "#" ))) {
                String[] lineArr = line.split( "~" );

                if (emailDop.contains( lineArr[indexLineArr] )) {
                    model.addAttribute( "messageType", "danger" );
                    model.addAttribute( "message", "MainController:" + retName +
                            " : Дублируется адрес почты \"" + lineArr[indexLineArr] + " " + lineArr[0] + " " + lineArr[1]
                            + " " + lineArr[2] + "\" в файле \"" + fullFileName + "\"" );
                    return retName;
                }
                emailDop.add( lineArr[indexLineArr] );

                Lastname lastname = lastnameService.saveIntoLastname( lineArr[0] );
                Firstname firstname = firstnameService.saveIntoFirstname( lineArr[1] );
                Secondname secondname = secondnameService.saveIntoSecondname( lineArr[2] );

                //Firstname firstname = firstnameService.findFirstnameByName( lineArr[1] );
                //Secondname secondname = secondnameService.findSecondnameByName( lineArr[2] );
                //Lastname lastname = lastnameService.findLastnameByFamily( lineArr[0] );
                if (lineArr.length > 4) {
//26 07 2023                    Year year = null;
//26 07 2023                    if (yearsReal.contains(lineArr[5])) {
//26 07 2023                        String[] yearArr = lineArr[5].split("-");
//26 07 2023                        year = yearService.save(yearArr[0], yearArr[1]);//.findByFirstnameyearAndSecondnameyear( yearArr[0], yearArr[1] );
//26 07 2023                    }
//26 07 2023                    Facultat facultat = null;
//26 07 2023                    if ((facsReal.contains(lineArr[6])) && (frmsReal.contains(lineArr[7]))) {
//26 07 2023                        facultat = facultatService.save(lineArr[6], lineArr[7]);//.findByNameAndForma( lineArr[6], lineArr[7] );
//26 07 2023                    }
//26 07 2023                    if ((year != null) && (facultat != null)) {
//26 07 2023                        //int subgroupp = Integer.parseInt(lineArr[3]);
//26 07 2023                        String subgroupp = lineArr[3];
//26 07 2023                        String[] semestrsArr = lineArr[8].split("-");
//26 07 2023
//26 07 2023                        String grp = lineArr[4] + "_" + lineArr[3];
//26 07 2023                        if (!testGroupp.contains(grp)) {
//26 07 2023                            testGroupp.add(grp);
//26 07 2023                        }
//26 07 2023
//26 07 2023                        if ((!semestrsArr[0].equals("0")) && (semReal.contains(semestrsArr[0]))) {
//26 07 2023                            saveGroupp(semestrsArr[0], lineArr[4], subgroupp, year, facultat,
//26 07 2023                                    firstname, secondname, lastname, role, lineArr[9]);
//26 07 2023                        }
//26 07 2023                        if ((!semestrsArr[1].equals("0")) && (semReal.contains(semestrsArr[1]))) {
//26 07 2023                            saveGroupp(semestrsArr[1], lineArr[4], subgroupp, year, facultat,
//26 07 2023                                    firstname, secondname, lastname, role, lineArr[9]);
//26 07 2023                        }
//26 07 2023                    }
//26 07 2023                    //saveSemestrs( semestrsArr[0], year, firstname, secondname, lastname, facultat, lineArr[3], lineArr[4] );
//26 07 2023                    //saveSemestrs( semestrsArr[1], year, firstname, secondname, lastname, facultat, lineArr[3], lineArr[4] );
                } else {
                    Startdata startdata = new Startdata( role, firstname, secondname, lastname, null, "" );
                    startdataService.save( startdata );
                }
            }
        }
        int max = 0;
        for (String tg : testGroupp){
            String[] parm = tg.split( "_" );
            int zn = Integer.parseInt( parm[1] );
            if (max < zn){
                max = zn;
            }
        }
        ArrayList<String>[] asm = new ArrayList[max];
        for (int i = 0; i < asm.length; i++){
            asm[i] = new ArrayList<>();
        }
        for (String tg : testGroupp) {
            String[] parm = tg.split( "_" );
            int zn = Integer.parseInt( parm[1] );
            if (zn>0) {
                asm[zn - 1].add(parm[0]);
            }
        }
        if (asm.length > 0) {
            boolean udalenie = false;
            while (asm[0].size() > 0) {
                udalenie = false;
                String parm = asm[0].get( 0 );
                for (int i = 1; i < asm.length; i++) {
                    if (asm[i].contains( parm )) {
                        asm[i].remove( parm );
                        asm[0].remove( parm );
                        udalenie = true;
                    }
                }
                if (!udalenie){
                    asm[0].remove( parm );
                }
            }
        }
        for (int i = 1; i < asm.length; i++){
            if (asm[i].size() > 0){
                model.addAttribute( "messageType", "danger" );
                model.addAttribute( "message", "MainController:" + retName +
                        " : Отсутствует подгруппа с меньшим номером в группе " + asm[i].get( 0 ) + " в файле \"" + fullFileName + "\"" );
                return retName;

            }
        }
        return null;
    }

    private void saveGroupp(String semestrName, String grouppName, String subGroupp, Year year, Facultat facultat,
                            Firstname firstname, Secondname secondname, Lastname lastname, String role, String mail){
        //Semestr semestr = new Semestr( semestrName );
        Semestr semestr = semestrService.save( semestrName );
        Startdata startdata = null;
        Subgroupp subgroupp = subgrouppService.saveSubgroupp( subGroupp );
        //Groupp groupp = new Groupp( grouppName, facultat, semestr, year, subgroupp );
        Groupp groupp = groupService.save( grouppName, facultat, semestr, year, subgroupp );
        startdata = new Startdata( role, firstname, secondname, lastname, groupp, mail );
        startdata = startdataService.save( startdata );
    }

    public String makeCourse(String nameCourse) throws IOException {
        // = teacherControllerService1.getPath("Course2122.txt"  );//UtilsController.getPath() + "\\" + "Course.txt";
        Reader reader = new FileReader( nameCourse );
        BufferedReader buffReader = new BufferedReader( reader );
        String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if ((!line.equals( "" )) && (!line.substring( 0, 1 ).equals( "#" ))) {
                String[] lineArr = line.split( "~" );
                if ((yearsDop.contains( lineArr[2] )) && (!yearsReal.contains( lineArr[2] ))){
                    yearsReal.add( lineArr[2] );
                }
                if ((groupsDop.contains( lineArr[3] )) && (!groupsReal.contains( lineArr[3] ))){
                    groupsReal.add( lineArr[3] );
                }
                if ((semDop.contains( lineArr[5] )) && (!semReal.contains( lineArr[5] ))){
                    semReal.add( lineArr[5] );
                }
                if ((facsDop.contains( lineArr[6] )) && (!facsReal.contains( lineArr[6] ))){
                    facsReal.add( lineArr[6] );
                }
                if ((frmsDop.contains( lineArr[7] )) && (!frmsReal.contains( lineArr[7] ))){
                    frmsReal.add( lineArr[7] );
                }
            }
        }
        return nameCourse;
    }


    public String makeStudent(String nameStudent) throws IOException {
        // =  teacherControllerService1.getPath("Student2122.txt"  );//UtilsController.getPath() + "\\" + "Student.txt";
        Reader reader = new FileReader( nameStudent );
        BufferedReader buffReader = new BufferedReader( reader );
        String format = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        String[] formatArr = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " ).replace( "#", "" ).split( "~" );
        buffReader.readLine();
        buffReader.readLine();
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            //if ((!line.equals( "" )) && (!line.substring( 0, 1 ).equals( "#" ))) {
            if (line.substring( 0, 1 ).equals( "#" )) {
                String[] lineArr = line.split( "#" );
                String[] groupp = lineArr[1].split("~");
                if ((groupsDop.contains( groupp[0] )) && (!groupsReal.contains( groupp[0] ))){
                    groupsReal.add( groupp[0] );
                }
                if ((yearsDop.contains( lineArr[2] )) && (!yearsReal.contains( lineArr[2] ))){
                    yearsReal.add( lineArr[2] );
                }
                if ((facsDop.contains( lineArr[3] )) && (!facsReal.contains( lineArr[3] ))){
                    facsReal.add( lineArr[3] );
                }
                if ((frmsDop.contains( lineArr[4] )) && (!frmsReal.contains( lineArr[4] ))){
                    frmsReal.add( lineArr[4] );
                }
                String[] sem = lineArr[5].split( "-" );
                if ((semDop.contains( sem[0] )) && (!semReal.contains( sem[0] ))){
                    semReal.add( sem[0] );
                }
                if ((semDop.contains( sem[1] )) && (!semReal.contains( sem[1] ))){
                    semReal.add( sem[1] );
                }
            }
        }
        return nameStudent;
    }

    //Сохранение содержимого файла control в памати yearsDop, groupsDop, facsDop, frmsDop, semDop
    public String makeControl(String nameControl) throws IOException {
        // =  teacherControllerService1.getPath("control2122.txt"  );//UtilsController.getPath() + "\\" + "control.txt";
        Reader reader = new FileReader( nameControl );
        BufferedReader buffReader = new BufferedReader( reader );
        int index = 0;
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            String[] parm = line.split( "~" );
            for (String st : parm) {
                switch (index) {
                    case 0:
                        if (!yearsDop.contains( st )) {
                            yearsDop.add( st );
                        }
                        break;
                    case 1:
                        if (!groupsDop.contains( st )) {
                            groupsDop.add( st );
                        }
                        break;
                    case 2:
                        if (!facsDop.contains( st )) {
                            facsDop.add( st );
                        }
                        break;
                    case 3:
                        if (!frmsDop.contains( st )) {
                            frmsDop.add( st );
                        }
                        break;
                    case 4:
                        if (!semDop.contains( st )) {
                            semDop.add( st );
                        }
                        break;
                }
            }
            index++;
        }
        return nameControl;
    }

    //Сохранение содержимого файла Standart в БД
    public String makeStandart(String nameStandart) throws IOException {
        // =  teacherControllerService1.getPath("Standart2122.txt"  );//UtilsController.getPath() + "\\" + "Standart.txt";
        Reader reader = new FileReader( nameStandart );
        BufferedReader buffReader = new BufferedReader( reader );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (!line.equals( "" )) {
                //Standart standart = new Standart(line);
                Standart standart = standartService.save( line );
                //String[] lineArr = line.split( "~" );

            }
        }
        return nameStandart;
    }

    //Сохранение содержимого файла control в памати lexemaDop
    public String makeLexema(String nameStandart) throws IOException {
        // =  teacherControllerService1.getPath("Lexema2122.txt"  );//UtilsController.getPath() + "\\" + "Lexema.txt";
        Reader reader = new FileReader( nameStandart );
        BufferedReader buffReader = new BufferedReader( reader );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (!line.equals( "" )) {
                lexemaDop.add( line );
                //Standart standart = new Standart(line);
                //Standart standart = standartService.save( line );
                //String[] lineArr = line.split( "~" );

            }
        }
        return nameStandart;
    }

    public String verificateFile(String name, Model model, String retName) throws IOException {
        Reader reader = new FileReader( name );
        int startIndex = 3;
        int endIndex = 4;
        if (name.contains( "Teacher" )){
            endIndex = 3;
        }
        if (name.contains( "Course" )){
            startIndex = 3;
            endIndex = 8;
        }
        //#группа #год#факультет#форма обучения#семестр
        String grouppFromFile = "";
        String yearFromFile = "";
        String facultatFromFile = "";
        String frmObuchFromFile = "";
        String semestrFromFile = "";
        BufferedReader buffReader = new BufferedReader( reader );
        String format1 = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
        format1 = makeFormat(buffReader.readLine(), model, retName, name, "1");
        String format2 = "";
        if (name.contains( "Student" )) {
            format2 = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
            format2 = makeFormat(buffReader.readLine(), model, retName, name, "2");
        }
                //makeFormat(String input, Model model, String retName, String name){
        //if (!format1.substring( 0, 1 ).equals( "#" )) {
        //    model.addAttribute( "messageType", "danger" );
        //    model.addAttribute( "message", "MainController:" + retName +
        //            " : Отсутствует шаблон в файле \"" + name + "\"" );
        //    return retName;
        //}
        String[] formatArr1 = format1.replace( "#", "" ).split( "~" );
        if (name.contains( "Student" )) {
            String[] formatArr2 = format2.replace("#", "").split("~");
        }
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll( "\\s{2,}", " " );
            if (!line.isEmpty()) {
                if (line.substring(0, 1).equals("#")) {
                    String[] arrLine = line.substring(1).split("#");
                    grouppFromFile = arrLine[0].split(" ")[0];
                    yearFromFile = arrLine[1].replace("#", "");
                    facultatFromFile = arrLine[2];
                    frmObuchFromFile = arrLine[3];
                    semestrFromFile = arrLine[4];
                    int ooo = 0;
                } else if (!line.substring(0, 1).equals("#")) {
                    String[] lineArr = line.split("~");
                    if (name.contains("Student")) {
                        if ((StringUtils.isNumeric(lineArr[0]))
                                || (StringUtils.isNumeric(lineArr[1])) || (StringUtils.isNumeric(lineArr[2]))) {
                            model.addAttribute("messageType", "danger");
                            model.addAttribute("message", "Verification|verificateFile : LexemaDop " + retName +
                                    " :  Отсутствует имя или фамилия или отчество в строке \"" + line + "\" в файле \"" + name + "\"");
                            return retName;
                        }
                        char symbol = grouppFromFile.charAt(0);//Получить первую цифру группы
                        boolean ok = false;
                        switch (symbol) {
                            case '1':
                                ok = comparison(semestrFromFile, "1-2-0");
                                break;
                            case '2':
                                ok = comparison(semestrFromFile, "3-4-0");
                                break;
                            case '3':
                                ok = comparison(semestrFromFile, "5-6-0");
                                break;
                            case '4':
                                ok = comparison(semestrFromFile, "7-8-0");
                                break;
                        }
                        if (!ok) {
                            model.addAttribute("messageType", "danger");
                            model.addAttribute("message", "Verification|verificateFile : LexemaDop " + retName +
                                    " :  Не согласованы номер группы и номера семестров в строке \"" + line + "\" в файле \"" + name + "\"");
                            return retName;
                        }
                    }
                    for (int i = startIndex; i < endIndex; i++) {
                        if (!lexemaDop.contains(lineArr[i])) {
                            model.addAttribute("messageType", "danger");
                            model.addAttribute("message", "Verification|verificateFile : LexemaDop " + retName +
                                    " :  отсутствует \"" + lineArr[i] + "\" в строке \"" + line + "\" в файле \"" + name + "\"");
                            return retName;
                        }
                    }
                    if (formatArr1.length != lineArr.length) {
                        model.addAttribute("messageType", "danger");
                        model.addAttribute("message", "MainController:" + retName +
                                " :  Не верен формат строки \"" + line + "\" в файле \"" + name + "\"");
                        return retName;
                    }
                    for (int i = 0; i < formatArr1.length; i++) {
                        try {
                            switch (formatArr1[i]) {
                                case "string":
                                    break;
                                case "int":
                                    Integer.parseInt(lineArr[i]);
                                    break;
                                case "int-int":
                                    break;
                            }
                        } catch (Exception ex) {
                            return "Ошибка в " + (i + 1) + " слове в строке \"" + line + "\" в файле \"" + name;
                        }
                    }
                }
            }
        }
        return null;
    }
    private boolean comparison(String parm1, String parm2){
        String[] semesters = parm1.split( "-" );
        String[] obraz = parm2.split( "-" );
        boolean[] ok = new boolean[semesters.length];
        for (boolean temp : ok) {
            temp = false;
        }
        int i = 0;
        for (String first : semesters){
            for (String second : obraz){
                if (first.equals( second ) && !ok[i]){
                    ok[i] = true;
                }
            }
            i++;
        }
        int count = 0;
        for (boolean temp : ok){
            if (temp){
                count++;
            }
        }
        return (count == semesters.length);
    }
    private String makeFormat(String input, Model model, String retName, String name, String nmb){
        String format = input.trim().replaceAll( "\\s{2,}", " " );
        if (!format.substring( 0, 1 ).equals( "#" )) {
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "MainController:" + retName +
                    " : Отсутствует шаблон" + nmb + " в файле \"" + name + "\"" );
            return null;
        }
        return format;
    }
}
