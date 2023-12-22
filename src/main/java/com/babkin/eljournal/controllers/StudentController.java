package com.babkin.eljournal.controllers;
//https://translated.turbopages.org/proxy_u/en-ru.ru.764f1d76-6308461f-bdcb7aa1-74722d776562/https/stackoverflow.com/questions/7810746/java-web-service-to-transfer-file-to-local-system
//https://translated.turbopages.org/proxy_u/en-ru.ru.764f1d76-6308461f-bdcb7aa1-74722d776562/https/stackoverflow.com/questions/921262/how-can-i-download-and-save-a-file-from-the-internet-using-java
//https://tproger.ru/translations/copy-files-in-java/?ysclid=l7a07wpouz948492664
//Доступ к localhost из любого места
//https://coderlessons.com/articles/mobilnaia-razrabotka-articles/dostup-k-localhost-iz-liubogo-mesta?ysclid=l7a45xh4y518004862
//Java реализует функцию загрузки и скачивания файлов через SSH
//https://russianblogs.com/article/46151151565/
//Передача файлов с использованием SFTP на Java (JSch)
//https://javascopes.com/file-transfer-using-sftp-in-java-jsch-a098de0e/?ysclid=l7a5cihjsj380653747

import com.babkin.eljournal.entity.User;
import com.babkin.eljournal.entity.temporaly.DnevnikFull;
import com.babkin.eljournal.entity.temporaly.docword.UpdateDocument;
import com.babkin.eljournal.entity.working.*;
import com.babkin.eljournal.service.*;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/student")
@PreAuthorize( "hasAuthority('STUDENT')" )
public class StudentController {


    private static final String dbgist = "debug";

    @Autowired
    private RaspisanieService raspisanieService;
    @Autowired
    private UtilsController utilsController;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DnevnikService dnevnikService;
    @Autowired
    private CallService callService;
    @Autowired
    private ThemeService themeService;
    //@Autowired
    //private SdachaService sdachaService;
    @Autowired
    private StudentControllerService studentControllerService;
    @Autowired
    private UpdateDocument updateDocument;
    @Autowired
    private TeacherControllerService teacherControllerService1;
    @Autowired
    private StartdataService startdataService;
    @Autowired
    private FilesForDnevnikService filesForDnevnikService;

    @Value( "${upload.path}" )
    private String uploadPath;

    @GetMapping
    public String startStudent(Model model, @AuthenticationPrincipal User user) throws Exception {

        Raspisanie currentRaspisanie = studentControllerService.fillStudentHeader( model, user, 9999, "9999");
        //Создать список ответов преподавателя
        //Записать в filesfordnevnik записть если отсутствует задание
        return "studentstart";
    }

    //Object ooo = model.getAttribute("dnevnikList");
    //List<DnevnikAndFilesForDnevnik> list = (List<DnevnikAndFilesForDnevnik>)model.getAttribute("dnevnikList");
    //for (DnevnikAndFilesForDnevnik dnevnikAndFilesForDnevnik: list){
    //    String str = dnevnikAndFilesForDnevnik.getFilesForDnevnik().getPathstudent();
    //}
    //Student student = studentService.findByUser(user);
    //Student student = studentService.findByUser(user);
    //Groupp groupp = student.getGroupp();
    //String date = utilsController.getDataNow();
    //List<Raspisanie> raspisanieList = studentControllerService.getRaspisaniebYGroupp(date, groupp);
    //String time = utilsController.getTimeNow();
    //Raspisanie current = studentControllerService.getCurrentRaspisanie(raspisanieList, time);

    //CreateDocumentSimple.start();
    //updateDocument.updateDocument("c:\\test\\template.docx",
    //        "c:\\test\\output.docx", "c:\\test\\");

    //String path = StudentController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    //Java11HttpClientExample obj = new Java11HttpClientExample();

    //System.out.println("Testing 1 - Send Http GET request");
    //java11HttpClientExample.sendGet();
//
    //System.out.println("Testing 2 - Send Http POST request");
    //java11HttpClientExample.sendPost();

    //String fullnameOriginal = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileforstudent());
    //String fullnameCopied = "uploads\\" +student.getLastname().getName() + " "
    //        + student.getFirstname().getName().substring(0, 1) + ". " + student.getSecondname().getName().substring(0, 1)
    //        + ". " + currentRaspisanie.getTheme().getTypezan() + currentRaspisanie.getTheme().getNumber() + " "
    //        + currentRaspisanie.getTheme().getNameteme() + ".docx";
//
    //model.addAttribute("fullnamecopied", fullnameCopied);
    //if (currentRaspisanie.getTheme().getTypezan().equals("лаб")) {
    //    String shablon = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileshablon());
    //    Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
    //            student.getFirstname(), student.getSecondname(), student.getLastname());
    //    String nameCopied = "uploads/tmp.docx";
    //    String nameshablon = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileshablon());
    //    updateDocument.addNewParagr(fullnameOriginal, nameCopied, nameshablon, startdata, currentRaspisanie.getTheme());
    //    FileUtils.copyFile( new File(nameCopied),  new File( fullnameCopied));
    //} else {
    //    FileUtils.copyFile( new File(fullnameOriginal),  new File( fullnameCopied));
    //}
    //return "studentstart";
    //}

    @PostMapping
    public String startStudent(Model model, @AuthenticationPrincipal User user,
                               @RequestParam String fromteacher,
                               @RequestParam int btn_out,//индекс по порядку среди списка представленных пользователю строк
                               @RequestParam String btn_prep,//1 - первая "Получить" 2 - "Отправить" 3 - вторая "Получить"
                               @RequestParam String copied,
                               @RequestParam String prepAddr,
                               @RequestParam String[] numberString,
                               @RequestParam("fullnamecop" )MultipartFile fullnamecop
    ) throws IOException, ParseException {
        //String fromteacher = "";
        if (btn_prep.equals("3")){
            prepAddr = fromteacher;
        }
        prepAddr = prepAddr.replace(",", "");
        Student student = studentService.findByUser(user);
        Raspisanie currentRaspisanie = null;
        //if (btn_out.equals("download")) {
        if (btn_prep.equals("1")) {
            currentRaspisanie = studentControllerService.fillStudentHeader(model, user, 9999, "9999");
        } else {
            //int btn_out_int = Integer.parseInt(btn_out);
            currentRaspisanie = studentControllerService.fillStudentHeader(model, user, btn_out, numberString[btn_out]);
            String nameSendFile = fullnamecop.getOriginalFilename();//.getName();
            //if ((nameSendFile.indexOf(".pdf")<0) || (nameSendFile.indexOf(".docx")<0)) {
            if ((nameSendFile.indexOf(".pdf")<0) && (!btn_prep.equals("3"))) {
                model.addAttribute("message", "Файл не отправлен, т.к.он не в формате pdf!");
                model.addAttribute("messageType", "danger");
                return "studentstart";
            }
        }
        if ((fromteacher.equals("")) && (copied.equals("")) && (btn_prep.equals("1"))) {// btn_out.equals("download")){
            model.addAttribute("message", "Укажите путь сохранения для файла инструкции!");
            model.addAttribute("messageType", "danger");
            return "studentstart";
        }
        if (fromteacher.equals("") && copied.equals("") && btn_prep.equals("3")) {// btn_out.equals("download")){
            model.addAttribute("message", "Укажите путь получения для файла инструкции!");
            model.addAttribute("messageType", "danger");
            return "studentstart";
        }
        if (fromteacher.equals("") && (copied.equals(""))) {// btn_out.equals("download")){
            model.addAttribute("message", "Укажите путь сохранения для файла ответа от преподавателя!");
            model.addAttribute("messageType", "danger");
            return "studentstart";
        }
        List<Raspisanie> smallRaspisanie = raspisanieService.findByCourseAndLessActiondate(
                currentRaspisanie.getCourse(), currentRaspisanie.getActiondate());

        List<Dnevnik> dnevnikList = dnevnikService.findAllBy_Less_Raspisanie_AndStudent(
                smallRaspisanie, student);
        //int indNumb = Integer.parseInt(btn_out);
        //String nn_po_poryadku = numberString[indNumb];
        String nn_po_poryadku = numberString[btn_out];
        String outfile = "";
        //String[] copiedList = copied.split(",");
        model.addAttribute("messageType", "success");
        if (fromteacher.equals("") || (fromteacher.equals("@"))) {
            switch (btn_prep) {
                case "1": //Получить инструкцию
                    if (copied.equals("")){
                        model.addAttribute("message", "Не указан путь сохранения файла!");
                        model.addAttribute("messageType", "danger");
                        return "studentstart";
                    }
                    outfile = studentControllerService.getInstruct(model, copied, student, smallRaspisanie.get(btn_out));
                    break;
                case "2": //Отправить файл
                    int numberFromForm = Integer.parseInt(numberString[btn_out]);
                    Dnevnik tempDnevnik = null;
                    for (Dnevnik dnevnik : dnevnikList){
                        if (dnevnik.getRaspisanie().getNumber() == numberFromForm){
                            tempDnevnik = dnevnik;
                            break;
                        }
                    }
                    outfile = studentControllerService.getFile(btn_out, student, smallRaspisanie.get(btn_out),
                            nn_po_poryadku, fullnamecop, tempDnevnik);
                    break;
                case "3": //Получить сообщение
                    outfile = studentControllerService.getMessage(prepAddr, student, smallRaspisanie.get(btn_out), copied);
                    break;
            }
            if (outfile != null && outfile.equals("")) {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "На сервере отсутствует запрошенный Вами файл!");
            } else {
                if (!dbgist.equals("debug")) {
                    outfile = "";
                } else {
                    outfile = "\"" + outfile + "\"";
                }
                if (outfile.equals("\"null\"")) {
                    model.addAttribute("message", "Дождитесь ответа преподавателя.");
                } else {
                    model.addAttribute("message", "Файл " + outfile + " получен!");
                }
            }
        } else {
            List<FilesForDnevnik> listOtpravOtv = (List<FilesForDnevnik>)model.getAttribute("listOtpravOtv");
            FilesForDnevnik tempfilesForDnevnik = listOtpravOtv.get(btn_out);
            tempfilesForDnevnik.setStatus("студент получил");
            filesForDnevnikService.update(tempfilesForDnevnik);
            outfile = fromteacher + "\\" + prepAddr.substring(prepAddr.indexOf(" "), prepAddr.length());
            FileUtils.copyFile(new File(prepAddr), new File(outfile));
            model.addAttribute("message", "Файл " + outfile + " получен!");
        }
        return "studentstart";
    }
    public String startStudent1111(Model model, @AuthenticationPrincipal User user,
                                   @RequestParam int btn_out,//индекс по порядку среди списка представленных пользователю строк
                                   @RequestParam String btn_prep,
                                   @RequestParam String copied,
                                   @RequestParam String[] numberString,
                                   @RequestParam("fullnamecop" )MultipartFile fullnamecop
    ) throws IOException, ParseException {

        //String number = "0";
        //String fullnamecop = "";
        //@RequestParam String fullnamecop) throws IOException {
        //if (fullnamecop.getOriginalFilename().equals("")){fullnamecop = {StandardMultipartHttpServletRequest$StandardMultipartFile@12650}
        //    model.addAttribute( "messageType", "danger" );
        //    model.addAttribute( "message", "Файл получен!" );
        //}
        String outfile = "";
        Student student = studentService.findByUser(user);
        Raspisanie currentRaspisanie = null;
        //04 12 22  if (btn_out.equals("download")) {
        //04 12 22      currentRaspisanie = studentControllerService.fillStudentHeader(model, user, "9999", "9999");
        //04 12 22  } else {
        //04 12 22      int btn_out_int = Integer.parseInt(btn_out);
        //04 12 22      currentRaspisanie = studentControllerService.fillStudentHeader(model, user, btn_out, numberString[btn_out_int]);
        //04 12 22  }
        //04 12 22  if (copied.equals("") && btn_out.equals("download")){
        //04 12 22      model.addAttribute("message", "Укажите путь сохранения для файла!");
        //04 12 22      model.addAttribute( "messageType", "danger" );
        //04 12 22      return "studentstart";
        //04 12 22  }
        // String original = "D:\\\\Working\\\\2022\\\\ftp server.docx";
        //Upload.downloadFile("localhost:8080", original);
        //btn_out = download - Получить
        //получить список (старых) расписаний меньше текущего
        List<Raspisanie> smallRaspisanie = raspisanieService.findByCourseAndLessActiondate(
                currentRaspisanie.getCourse(), currentRaspisanie.getActiondate());

        List<Dnevnik> dnevnikList = dnevnikService.findAllBy_Less_Raspisanie_AndStudent(
                smallRaspisanie, student);
        // currentRaspisanie, student);
        if (true){// (btn_out.equals("download")) {
            String[] listCopied = copied.split(",");
            int indCopied = -1;
            for (String temp : listCopied) {
                indCopied++;
                if (temp.length() > 0) {
                    break;
                }
            }
            currentRaspisanie = smallRaspisanie.get(indCopied);
            String fullnameOriginal = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileforstudent());
            String fullnameCopied = listCopied[indCopied] + student.getLastname().getName() + " "
                    + student.getFirstname().getName().substring(0, 1) + ". " + student.getSecondname().getName().substring(0, 1)
                    + ". " + currentRaspisanie.getTheme().getTypezan() + currentRaspisanie.getTheme().getNumber() + " "
                    + currentRaspisanie.getTheme().getNameteme() + ".docx";

            model.addAttribute("fullnamecopied", fullnameCopied);
            if (currentRaspisanie.getTheme().getTypezan().equals("лаб")) {
                String shablon = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileshablon());
                Startdata startdata = startdataService.findByFirstname_NameAndSecondname_NameAndLastname_Family(
                        student.getFirstname(), student.getSecondname(), student.getLastname());
                String nameCopied = "uploads/tmp.docx";
                if (!currentRaspisanie.getTheme().getFileshablon().equals("")) {
                    String nameshablon = teacherControllerService1.getPath(currentRaspisanie.getTheme().getFileshablon());
                    updateDocument.fillFromShablon(fullnameOriginal, nameCopied, nameshablon, startdata, currentRaspisanie.getTheme());
                    FileUtils.copyFile(new File(nameCopied), new File(fullnameCopied));// fullnameCopied == куда
                } else {
                    FileUtils.copyFile(new File(fullnameOriginal), new File(fullnameCopied));
                }
            } else {
                FileUtils.copyFile(new File(fullnameOriginal), new File(fullnameCopied));
            }
            outfile = fullnameCopied;


            //File fileoriginal = new File(fullnamecop.getOriginalFilename());
            //String pathCopied = copied;// + fullnamecop.getOriginalFilename().substring(8);
            //File filecopied = new File(pathCopied);//"src/test/Teacher.docx");
            //  //FileUtils.copyFile(original, copied, StandardCopyOption.COPY_ATTRIBUTES);
            //FileUtils.copyFile(fileoriginal, filecopied);
            filesForDnevnikService.addFilesForDnevnik(utilsController.getDataNow(), FilesForDnevnikEnum.студент_скачал,
                    fullnameCopied, "1", dnevnikList.get(indCopied));
        } else {
            //if (btn_out.equals("save")) {//Сохранить
            //int indNumb = Integer.parseInt(btn_out);
            String resultFileName = studentControllerService.getFullName(student, currentRaspisanie, numberString[btn_out]);
            if (fullnamecop != null && !fullnamecop.getOriginalFilename().isEmpty()) {
                //    File uploadDir = new File( uploadPath );
                //    if (!uploadDir.exists()){
                //        uploadDir.mkdir();
                //    }
                //    String uuidFile = UUID.randomUUID().toString();
                //    resultFileName = uuidFile + "." + fullnamecop.getOriginalFilename();
                //    fullnamecop.transferTo( new File( uploadDir + "\\" + resultFileName ) );
                //}
                int index = fullnamecop.getOriginalFilename().lastIndexOf(".");
                String ext = fullnamecop.getOriginalFilename().substring(index);
                outfile = resultFileName + ext;
                fullnamecop.transferTo(new File(outfile));
                //int indexFomdnevnikList = Integer.parseInt(btn_out);
                filesForDnevnikService.addFilesForDnevnik(utilsController.getDataNow(), FilesForDnevnikEnum.студент_сдал,
                        outfile, "2", dnevnikList.get(btn_out));
            }
        }
        //currentRaspisanie = studentControllerService.fillStudentHeader( model, user );
        model.addAttribute( "messageType", "success" );
        if (outfile.equals("")){
            model.addAttribute("message", "Не указан файл для отправки!");
        } else {
            model.addAttribute("message", "Файл \"" + outfile + "\" получен!");
        }
        return "studentstart";
    }

    private void prepareForStudent(Model model, User user) throws ParseException {
        Plan plan = utilsController.getPlanTemp( user );
        model.addAttribute( "plan", plan );
        Student student = studentService.findByUser( user );
        model.addAttribute( "student", student );

        String timeNow = utilsController.getTimeNow();// DateTime.now().toString("HH:mm");//"21:24";//
        //List<Dnevnik> dnevniks = dnevnikService.findByStudent_IdAndPlan_Id( student, plan );
        List<Dnevnik> dnevniks = null;
        if (plan != null) {
            dnevniks = dnevnikService.findDnevniksByPlan_ActionAndPlan_CourseAndStudent_Id(
                    plan.getAction(), plan.getCourse(), student );
            if (dnevniks.size() == 1){
                Dnevnik dnevnik = dnevniks.get( 0 );
                //dnevnik.setDatestudentsdal( utilsController.getDataNow() );//??????22 08 2022
                //dnevnikService.update( dnevnik );
            }
        }
        Long callid = 0L;//plan.getCall().getId()-1; str66
        int result = 0;
        if (plan != null) {
            result = utilsController.comparison( plan.getCall().getName(), timeNow );//-1 до пары 0 во время пары
        }
        if (plan != null) {
            if (result == 0) {
                if (dnevniks.size() == 0) {
                    //18 08 2022  Dnevnik dnevnik = new Dnevnik( student, plan, true, null, null );
                    //18 08 2022  dnevnikService.save( dnevnik );
                    //dnevniks = dnevnikService.findByStudent_IdAndPlan_Id( student, plan );
                }
            } else {
                callid = plan.getCall().getId();
            }
        }
        List<DnevnikFull> fromDevnik = new ArrayList<>();

        List<String> lines = new ArrayList<String>();
        Collections.sort(lines, Collator.getInstance());
        if ((plan != null) && (result != -1)) {//если план существует и во время пары
            //if ((plan != null) && (utilsController.comparison( plan.getCall().getName(), timeNow ) != -1)) {
            dnevniks = dnevnikService.findDnevniksByPlanCourse_IdAndStudent_Id( plan.getCourse(), student );
            for (Dnevnik dnevnik : dnevniks) {
                //18 08 2022  Theme theme = dnevnik.getPlan().getTheme();//themeService.findByPlan(  );
                //18 08 2022  if ((theme != null) && (!StringUtils.isEmpty(theme.getZadanie()))) {
                //18 08 2022      //fromDevnik.add( new DoubleString( dnevnik.getPlan().getAction()
                //18 08 2022      //        + " " + dnevnik.getPlan().getCall().getName() + " " + dnevnik.getPropuski(),
                //18 08 2022      //        theme.getZadanie() ) );
                //18 08 2022      //найти индекс по котораму вставлять очередной элемент
                //18 08 2022      int ind = 0;
                //18 08 2022      String themeForFromDevnik = dnevnik.getPlan().getAction()
                //18 08 2022              + " " + dnevnik.getPlan().getCall().getName()
                //18 08 2022              + " " + UtilsController.getTypeZbyIndex(dnevnik.getPlan().getTypez(), dnevnik.getPlan().getNumberoflab())
                //18 08 2022              + " " + dnevnik.getPropuski();
                //18 08 2022      boolean found = false;
                //18 08 2022      while (!found){
                //18 08 2022          if (ind < fromDevnik.size()){
                //18 08 2022              DnevnikFull temp = fromDevnik.get( ind );
                //18 08 2022              int sravn = temp.getTheme().compareTo( themeForFromDevnik );
                //18 08 2022              if  (UtilsController.prepareStr(temp.getTheme()).compareTo( UtilsController.prepareStr(themeForFromDevnik) ) > 0){
                //18 08 2022                  found = true;
                //18 08 2022              }
                //18 08 2022          } else {
                //18 08 2022              found = true;
                //18 08 2022          }
                //18 08 2022          ind++;
                //18 08 2022      }
                //18 08 2022      ind--;
                //18 08 2022      fromDevnik.add(ind, new DnevnikFull( dnevnik, theme.getNameteme(), themeForFromDevnik, theme.getZadanie() ) );
                //18 08 2022      //DnevnikFull dnevnikFull = fromDevnik.get( 0 );
                //18 08 2022      //Dnevnik dnevnik1 = dnevnikFull.getDnevnik();
                //18 08 2022      //int oo =0;
                //18 08 2022  }
            }
            callid = plan.getCall().getId()-1;
        }
        Theme theme = null;
        if (plan != null){
            theme = plan.getTheme();//themeService.findByPlan( plan );
        }
        model.addAttribute( "theme", theme );//не возвращать nameTheme
        model.addAttribute( "fromDevnik", fromDevnik );
        model.addAttribute( "aDateTime", utilsController.getDataNow() );
        List<Call> calls = callService.findAll();
        model.addAttribute( "calls", calls );
        model.addAttribute( "callid", callid );
        model.addAttribute( "timeview", true );
    }
//    private String prepareStr(String parm){
//        String[] rs = parm.split( " " );
//        String[] rs1 = rs[0].split( "-" );
//        return rs1[2] + rs1[1] + rs1[0] + rs[1] + rs[2];
//    }

    @RequestMapping("/sendfile/{index}")
    public String sendFile(@AuthenticationPrincipal User user, @PathVariable(name="index") int index, Model model) throws ParseException {
        prepareForStudent( model, user );
        model.addAttribute( "index", index-1 );
        return "sendfile";
    }
    @PostMapping("/sendfile/sendfl")
    public String sendFile(@AuthenticationPrincipal User user, @RequestParam(name="index") int index,
                           @RequestParam("file") MultipartFile file, Model model) throws IOException, ParseException {
        String originalFilename = file.getOriginalFilename();
        prepareForStudent( model, user );
        //model.addAttribute( "index", index );
        //String datasdachi = DateTime.now().toString("dd-MM-YYY");
        String datasdachi = utilsController.getDataNow( new SimpleDateFormat( "dd-MM-YYYY" ) );
        //int index = ((int)model.getAttribute( "index" ));
        ArrayList<DnevnikFull> dnevnikFulls = ((ArrayList<DnevnikFull>) model.getAttribute( "fromDevnik" ));
        DnevnikFull dnevnikFull = dnevnikFulls.get( index );
        if (originalFilename.equals( "" )){
            model.addAttribute( "messageType", "danger" );
            model.addAttribute( "message", "Не был выбран файл." );
            return "studentstart";
        }
        Student student = (Student)model.getAttribute( "student" );
        Plan plan = (Plan)model.getAttribute( "plan" );
        //String filename = DateTime.now().toString("YYYMMdd HHmm") + " " + plan.getAction() + " " + originalFilename;
        String filename = utilsController.getDataNow( new SimpleDateFormat( "YYYMMdd HHmm" ) ) + " " + plan.getAction() + " " + originalFilename;
        String[] year = plan.getAction().split( "-" );
        //String pathYear = year[2];
        String[] pathStud = new String [5];
        pathStud[0] = year[2];
        pathStud[1] = "";//plan.getCourse().getSemestr().getName();
        pathStud[2] = student.getGroupp().getNamegroupp();
        pathStud[3] = student.getLastname().getName() + "_" + student.getFirstname().getName().substring( 0, 1 )
                + student.getSecondname().getName().substring( 0, 1 );
        pathStud[4] = plan.getCourse().getNameCourseShort();


        //String studPath = "Year" + year[2] + "\\Sem" + plan.getCourse().getSemestr().getName() ;//+ "\\g" + student.getGroupp().getNameGroupp();// + "\\" + student.getLastname().getName() + "_"
        //        //+ student.getFirstname().getName().substring( 0, 1 ) + student.getSecondname().getName().substring( 0, 1 ) + "\\"
        //        //+ plan.getCourse().getNameCourseShort() + "\\";
        String fullname = uploadPath;
        for (int i = 0; i < pathStud.length; i++) {
            fullname += "\\" + pathStud[i];
            File uploadDir = new File( fullname );//uploadPath+ "\\serf\\" );// + studPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
        }
        //String uuidFile = UUID.randomUUID().toString();
        //String resultFilename = uuidFile + "." + originalFilename;
        file.transferTo( new File( fullname + "\\" + filename ) );
//17 08 2023        Sdacha sdacha = new Sdacha(datasdachi, filename, ((Student)model.getAttribute( "student" )),
//17 08 2023                dnevnikFull.getDnevnik());
//17 08 2023        sdachaService.addSdacha( sdacha );
        model.addAttribute( "messageType", "success" );
        model.addAttribute( "message", "Данные \"" + fullname + "\\" + filename + "\" отправлены." );

        //return "redirect:/student";
        return "studentstart";
    }
}