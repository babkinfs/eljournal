package com.babkin.eljournal.service;

import com.babkin.eljournal.controllers.UtilsController;
import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.DnevnikAndFilesForDnevnik;
import com.babkin.eljournal.entity.temporaly.docword.UpdateDocument;
import com.babkin.eljournal.entity.working.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentControllerService {
    @Value( "${upload.path}" )
    private String uploadPath;

    @Autowired
    private CourseService courseService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private RaspisanieService raspisanieService;
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentControllerService studentControllerService;
    @Autowired
    private DnevnikService dnevnikService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private SubgrouppService subgrouppService;
    @Autowired
    private SemestrService semestrService;
    @Autowired
    private FilesForDnevnikService filesForDnevnikService;
    @Autowired
    private TeacherControllerService teacherControllerService;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private UpdateDocument updateDocument;

    //Получить расписание для студентов заданной группы на текущую дату
    public List<Raspisanie> getRaspisaniebYGroupp(String date, Groupp groupp, Teacher teacher) throws ParseException {
        //grouppListAll список подгрупп одной группы
        List<Groupp> grouppListAll = groupService.findGrouppByNameGroupp(groupp.getOnlynamegroupp(), groupp.getSemestr());
        List<Course> courseList = new ArrayList<>();
        if (groupp != null) {
            //List<Groupp> grouppList = new ArrayList<>();
            //for (int i = 0; i < grouppListAll.size(); i++) {
            //    grouppList.addAll(getAllGroupps(grouppListAll.get(i)));
            //}
            //for (Groupp groupp1 : grouppList) {
            for (Groupp groupp1 : grouppListAll) {
                //List<Course> tmpc = courseService.findByGroupp_Id(groupp1);
                courseList.addAll(courseService.findByGroupp(groupp1));
            }
        }
        if (teacher != null){
            courseList.addAll(courseService.findByTeacher(teacher));
        }
        List<Theme> themeList = new ArrayList<>();
        for (Groupp tempGroupp : grouppListAll) {
            for (Course course : courseList) {
                //List<Theme> tmp = themeService.findAllByCourse(course);
                Groupp grouppTemp = course.getGroupp();
                if ((course.getGroupp().getId() == tempGroupp.getId())
                        || (course.getGroupp().getSubgroupp().getNamesubgroupp().equals("0"))) {
                    themeList.addAll(themeService.findAllByCourse(course));
                }
            }
        }
        List<Raspisanie> raspisanieList = new ArrayList<>();
        String timeNow = utilsController.getTimeNow();
        for (Theme theme : themeList) {
            List<Raspisanie> raspisanieListTemp = raspisanieService.findRaspisanieByTheme(theme);
            for (Raspisanie raspisanie : raspisanieListTemp) {
                if ((raspisanie != null) && (raspisanie.getActiondate().equals(date))
                        && (!raspisanieList.contains(raspisanie)) ){
                        //&& (utilsController.comparison(raspisanie.getCall().getName(), timeNow) == 0)) {
                    raspisanieList.add(raspisanie);
                }
            }
        }
        return raspisanieList;
    }

    public Raspisanie getCurrentRaspisanie(List<Raspisanie> raspisanieList, String time, Groupp groupp) {
        Raspisanie currentRaspisanie = null;
        for (Raspisanie raspisanie : raspisanieList) {
            String para = raspisanie.getCall().getName();
            //-1 до пары   0 во время пары   +1 после пары
            int tester = utilsController.comparison(para, time);
            if (tester == 0) {
                if ((raspisanie.getCourse().getGroupp().equals(groupp))
                        || ((raspisanie.getCourse().getGroupp().getSubgroupp().getNamesubgroupp().equals("0")))) {
                    currentRaspisanie = raspisanie;
                }
            }
        }
        return currentRaspisanie;
    }

    public Raspisanie fillStudentHeader(Model model, User user, int click, String number) throws ParseException {
        Student student = studentService.findByUser(user);
        String date = utilsController.getDataNow(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
        model.addAttribute("aDateTime", date);
        String time = utilsController.getTimeNow();
        List<Raspisanie> raspisanieList = null;
        date = utilsController.getDataNow();
        Groupp baseGroupp = student.getGroupp();//группа в которой находится студент
        //Получить расписание для студентов заданной группы на текущую дату
        raspisanieList = getRaspisaniebYGroupp(date, baseGroupp, null);
        Groupp groupp = groupService.findGrouppBySubgroupp_NamesubgrouppAndFacultat_AndSemestr_AndYear_AndNamegroupp("0",
                baseGroupp.getFacultat(), baseGroupp.getSemestr(), baseGroupp.getYear(),
                baseGroupp.getOnlynamegroupp());
        //baseGroupp.getNamegroupp().substring(0, baseGroupp.getNamegroupp().indexOf(".")));
        //List<Raspisanie> raspisanieList1 = getRaspisaniebYGroupp(date, groupp, null);
        //raspisanieList.addAll(raspisanieList1);
        Raspisanie currentRaspisanie = getCurrentRaspisanie(raspisanieList, time, student.getGroupp());
        List<DnevnikAndFilesForDnevnik> dnevnikList = null;
        List<FilesForDnevnik> listOtpravOtv = new ArrayList<>();
        FilesForDnevnik filesForDnevnik = null;
        if (currentRaspisanie != null) {
            if (currentRaspisanie.getTheme().getFileforstudent().length()==0){
                int ooo=0;
            }
            Dnevnik dnevnik = dnevnikService.addDnevnik(true, student, currentRaspisanie);
            model.addAttribute("dnevnik", dnevnik);
            //получить список (старых) расписаний меньше текущего
            List<Raspisanie> smallRaspisanie = raspisanieService.findByCourseAndLessActiondate(
                    currentRaspisanie.getCourse(), currentRaspisanie.getActiondate());
            //из них список дневников
            dnevnikList = new ArrayList<>();
            for (Raspisanie rasp : smallRaspisanie) {
                List<Dnevnik> dnevnikList1 = dnevnikService.findAllByRaspisanie_AndStudent(rasp, student);
                String nameCallFromRasp = rasp.getCall().getName();
//23 08 2023                 if ((dnevnikList1.size() > 0) && (utilsController.comparison(nameCallFromRasp, time) == 0)) {
                if (dnevnikList1.size() > 0){
                    dnevnik = dnevnikList1.get(0);
//                Dnevnik dnevnik = dnevnikService.addDnevnik(false, student, rasp);
                    filesForDnevnik = filesForDnevnikService.findeFilesForDnevnikAndKtocdal(FilesForDnevnikEnum.студент_сдал, dnevnik);
                    //Обнаружил имя последнего файла, который был передан
                    //21.12.2023 if (filesForDnevnik != null && (filesForDnevnik.getOcenka() != null && filesForDnevnik.getOcenka().equals("Проверено, замечаний нет."))) {
                    if (filesForDnevnik != null){
                        //отправляю сообщение что проверил и замечаний нет
                        dnevnikList.add(new DnevnikAndFilesForDnevnik(dnevnik, filesForDnevnik));
                        if (filesForDnevnik.getStatus().equals("отправил студенту")){
                            listOtpravOtv.add(filesForDnevnik);
                        }
                        //int oooo=0;
                    } else {
                        String subGrouppStudent = student.getGroupp().getSubgroupp().getNamesubgroupp();
                        //String subGrouppFilesForDnevnik = filesForDnevnik.getDnevnik().getRaspisanie().getCourse().getGroupp().getSubgroupp().getNamesubgroupp();
                        String subGrouppFilesForDnevnik = dnevnik.getRaspisanie().getCourse().getGroupp().getSubgroupp().getNamesubgroupp();
                        //18 08 23  if ((subGrouppFilesForDnevnik.equals(subGrouppStudent)) || (subGrouppFilesForDnevnik.equals("0"))) {
                        if (dnevnik.isVisiblebuttons() || dnevnik.getRaspisanie().getActiondate().equals(date)) {
                            dnevnikList.add(new DnevnikAndFilesForDnevnik(dnevnik, filesForDnevnik));//null));
                        }
                        //18 08 23  }
                    }
                }
            }
            //по списку дневников получить filesForDnevnik

            //Dnevnik dnevnik = dnevnikService.addDnevnik(true, student, currentRaspisanie);
            //filesForDnevnik = filesForDnevnikService.findeFilesForDnevnik(FilesForDnevnikEnum.студент_сдал, dnevnik);
            raspisanieList = null;
            ////Найти записи из дневника для определенного студента и темы
            //dnevnikList = dnevnikService.findAllByRaspisanie_AndStudent(currentRaspisanie, student);
            if (dnevnikList.size() == 0) {
                dnevnikList = null;
            }
        }
        //model.addAttribute("filesForDnevnik", filesForDnevnik);
        model.addAttribute("raspisanieList", raspisanieList);
        model.addAttribute("dnevnikList", dnevnikList);
        model.addAttribute("student", student);
        if (listOtpravOtv.size() == 0){
            listOtpravOtv = null;
        }
        model.addAttribute("listOtpravOtv", listOtpravOtv);
        if (click == 9999) {
            return currentRaspisanie;
        }
        //int clickint = Integer.parseInt(click);
        int numberint = Integer.parseInt(number);
        List<Raspisanie> list = raspisanieService.findAllByCourse_IdAndCall_IdAndNumber(
                currentRaspisanie.getCourse(), currentRaspisanie.getCall(), numberint);
        return list.get(0);
    }

    public List<Student> getStudents(Groupp groupp) {
        Semestr semestr = semestrService.findByName(utilsController.getTempSemestrName(groupp));
        List<Student> studentList = new ArrayList<>();
//25 02 2023        if (groupp.getSubgroupp().getNamesubgroupp().equals("0")) {
        Subgroupp subgroupp = null;
        Groupp groupp1 = null;
        int countSubgroupp = subgrouppService.getCount();
        //for (int j = 0; j < 10; j++) {
        //    Semestr tempSemestr = semestrService.findByName(Integer.toString(j));
        //    if (tempSemestr != null) {
                for (int i = 1; i < countSubgroupp; i++) {
                    subgroupp = subgrouppService.findSubgrouppByNamesubgroupp(Integer.toString(i));
                    groupp1 = groupService.findGrouppByNamegrouppAndFacultat_AndSemestr_AndYear_AndSubgroupp(
                            groupp.getNamegrouppOunly(), groupp.getFacultat(), semestr, groupp.getYear(), subgroupp);
                    if (groupp1 != null) {
                        studentList.addAll(studentService.findByGrouppId(groupp1));
                    }
                }
        //    }
        //}
        //}
        studentList.addAll(studentService.findByGrouppId(groupp));
        return studentList;
    }

    public List<Groupp> getAllGroupps(Groupp groupp) {
        List<Groupp> grouppList = new ArrayList<>();
        //if (groupp.getSubgroupp().getNamesubgroupp() == 0) {
        Subgroupp subgroupp = null;
        Groupp groupp1 = null;
        for (int i = 0; i < 5; i++) {
            subgroupp = subgrouppService.saveSubgroupp(Integer.toString(i));
            String name = groupp.getOnlynamegroupp();
            groupp1 = groupService.save(
                    name, groupp.getFacultat(), groupp.getSemestr(), groupp.getYear(), subgroupp);
            if (groupp1 != null) {
                grouppList.add(groupp1);
            }
        }
        //}
        return grouppList;
    }
    public String getFullName(Student student, Raspisanie currentRaspisanie,
                              String numberString){
        //Student student =studentService.findByUser(user);
        String dir = uploadPath;
        File uploadDir = new File( dir );
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        dir+="\\" + student.getGroupp().getOnlynamegroupp();//.getNamegroupp();
        uploadDir = new File( dir );
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        dir+="\\" + student.getLastname().getName() + "_" + student.getFirstname().getName().substring(0,1)
                + "_" + student.getSecondname().getName().substring(0,1);
        uploadDir = new File( dir );
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        dir+="\\" + currentRaspisanie.getCourse().getNameCourseShort();
        uploadDir = new File( dir );
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        dir+="\\" + currentRaspisanie.getTheme().getTypezan();
        uploadDir = new File( dir );
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        //String filename = currentRaspisanie.getTheme().getTypezan().substring(1,2) + currentRaspisanie.getTheme().getNumber();// + "_1";
        //Получить из папки по пути: группа, студент, дисциплина, тип занятия список работ
        String filename = currentRaspisanie.getTheme().getTypezan().substring(1,2) + numberString;
        String[] list = uploadDir.list();
        List<String> stringList = new ArrayList<>();
        // Получить список только текущей работы
        for (String st : list){
            if (st.indexOf(filename)>=0){
                stringList.add(st);
            }
        }
        //Получить самый старший номер из полученного списка
        if (stringList.size() == 0){
            filename +=  "~1";
        } else {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (String parm : stringList) {
                String ost = parm.substring(filename.length()+1);
                String nm = ost.substring(0, ost.indexOf("."));
                try{
                    int rs = Integer.parseInt(nm);
                    numbers.add(rs);
                } catch (Exception ex){
                    nm="";
                }
            }
            // в numbers получили список номеров всех файлов для текущей работы
            int count = numbers.stream().max(Integer::compare).get()+1;//count - номер следующего файла
            filename += "~" + count;
            //int ooo=0;
        }
        return dir + "\\" + filename;
    }

    ////Получить инструкцию
    public String getInstruct(Model model, String copied,
                              Student student, Raspisanie currentRaspisanie) throws IOException {
        String fileForStudent = currentRaspisanie.getTheme().getFileforstudent();
        String fullnameCopied = "";
        if (!fileForStudent.equals("")) {
            String fullnameOriginal = teacherControllerService.getPath(fileForStudent);
            fullnameCopied = copied + student.getLastname().getName() + " "
                    + student.getFirstname().getName().substring(0, 1) + ". " + student.getSecondname().getName().substring(0, 1)
                    + ". " + currentRaspisanie.getTheme().getTypezan() + currentRaspisanie.getTheme().getNumber() + " "
                    + currentRaspisanie.getTheme().getFileforstudent();

            model.addAttribute("fullnamecopied", fullnameCopied);
            if (currentRaspisanie.getTheme().getTypezan().equals("лаб")) {
                //String shablon = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileshablon());
                Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
                        student.getFirstname(), student.getSecondname(), student.getLastname());
                String nameCopied = "uploads/tmp.docx";
                if (!currentRaspisanie.getTheme().getFileshablon().equals("")) {
                    String nameshablon = teacherControllerService.getPath(currentRaspisanie.getTheme().getFileshablon());
                    updateDocument.fillFromShablon(fullnameOriginal, nameCopied, nameshablon, startdata, currentRaspisanie.getTheme());
                    FileUtils.copyFile(new File(nameCopied), new File(fullnameCopied));// fullnameCopied == куда
                } else {
                    FileUtils.copyFile(new File(fullnameOriginal), new File(fullnameCopied));
                }
            } else {
                FileUtils.copyFile(new File(fullnameOriginal), new File(fullnameCopied));
            }
        }
        return fullnameCopied;
    }
    ////Отправить файл
    public String getFile(int btn_out, Student student, Raspisanie currentRaspisanie, String nn_po_poryadku,
                          MultipartFile fullnamecop, Dnevnik dnevnik) throws IOException {
        //Проверить нужно ли отправлять файл студента
        boolean miss = true;
        List<Dnevnik> dnevniks = dnevnikService.findAllByRaspisanie_AndStudent(currentRaspisanie, student);
        if (dnevniks.size() > 0) {
            FilesForDnevnik filesForDnevniks = filesForDnevnikService.findeFilesForDnevnikAndKtocdal(
                    FilesForDnevnikEnum.студент_сдал, dnevniks.get(0));
            if (filesForDnevniks != null) {
                if (filesForDnevniks.getOcenka() != null && filesForDnevniks.getOcenka().equals("Проверено, замечаний нет.")) {
                    miss = false;
                }
            }
        }

        String outfile = null;// outfile - полный адрес файла со следующей по порядку работой
        if (miss) {
            String resultFileName = getFullName(student, currentRaspisanie, nn_po_poryadku);
            if (fullnamecop != null && !fullnamecop.getOriginalFilename().isEmpty()) {
                int index = fullnamecop.getOriginalFilename().lastIndexOf(".");
                String ext = fullnamecop.getOriginalFilename().substring(index);
                outfile = resultFileName + ext;
                fullnamecop.transferTo(new File(outfile));
                filesForDnevnikService.addFilesForDnevnik(utilsController.getDataNow(), FilesForDnevnikEnum.студент_сдал,
                        outfile, "создал", dnevnik);
            }
        }
        return outfile;
    }
    ////Получить сообщение
    public String getMessage(String fullnameOriginal, Student student, Raspisanie currentRaspisanie, String copied) throws IOException {
        String fullnameCopied = copied + student.getLastname().getName() + " "
                + student.getFirstname().getName().substring(0, 1) + ". " + student.getSecondname().getName().substring(0, 1)
                + ". " + " Замечания к " + currentRaspisanie.getTheme().getTypezan() + currentRaspisanie.getTheme().getNumber() + " "
                + currentRaspisanie.getTheme().getNameteme() + ".docx";
        FileUtils.copyFile(new File(fullnameOriginal), new File(fullnameCopied));// fullnameCopied == куда
        return fullnameCopied;
    }
}