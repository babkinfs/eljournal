package com.babkin.eljournal.entity.temporaly.docword;

//Java – Чтение и запись Microsoft Word с помощью POI Apache
// https://javascopes.com/java-read-and-write-microsoft-word-with-apache-poi-47320aea/?ysclid=l77ahxkhdj406898925

import com.babkin.eljournal.entity.temporaly.DoubleString;
import com.babkin.eljournal.entity.working.Exercise;
import com.babkin.eljournal.entity.working.Startdata;
import com.babkin.eljournal.entity.working.Theme;
import com.babkin.eljournal.service.ExerciseService;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateDocument {
    @Autowired
    private ExerciseService exerciseService;
    public void fillFromShablon(String input, String output, String shablon, Startdata startdata, Theme theme) throws IOException {
        List<String> textShablon = new ArrayList<>();
        Reader reader = new FileReader( shablon );
        BufferedReader buffReader = new BufferedReader( reader );
        while (buffReader.ready()) {
            String line = buffReader.readLine().trim().replaceAll("\\s{2,}", " ");
            if ((!line.equals("")) && (!line.substring(0, 1).equals("#"))) {
                String res = "";
                while (line.length()>0){
                    int point = 0;
                    int startIndex = line.indexOf("{");
                    int endIndex = line.indexOf("}");
                    if (startIndex==-1 && endIndex==-1){
                        res += line;
                        textShablon.add(res);
                        break;
                    }
                    res += line.substring(point, startIndex-1);
                    String key = line.substring(startIndex+1, endIndex);
                    Exercise rs = exerciseService.findExerciseByKeyAndStartdata_AndTheme_(key, startdata, theme);
                    res += rs.getBody();
                    line = line.substring(endIndex+1);
                }
            }
        }
        int ind = 0;
        String template = "14,BOTH";//Размер шрифта, выравнивание (BOTH, START, CENTER)
        for (String text : textShablon){
            //paragraphOneRunThree.setText(st);
            //addNewParagr(input, output, text);
            if (ind == 0){
                addMyParagr(input, output, new DoubleString(text, template), ind);
            } else {
                addMyParagr(output, output, new DoubleString(text, template), ind);
            }
            ind++;
        }

    }
    public void addMyParagr(String input, String output, DoubleString text, int index) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(input)))) {
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            XWPFParagraph xwpfParagraph = xwpfParagraphList.get(index);
            XmlCursor cursor = xwpfParagraph.getCTP().newCursor();
            XWPFParagraph new_par = doc.insertNewParagraph(cursor);
            XWPFRun r1 = new_par.createRun();


            //new_par.getFormat().setFirstLineIndent(25f);//.setIndentationFirstLine(20);//.setFirstLineIndent(20);

            //new_par.setIndentationFirstLine(25);
            new_par.setFirstLineIndent(720);//Отступ в абзаце
            //new_par.setIndentationHanging(720);

            //String template = "16,BOTH";//Размер шрифта, выравнивание (BOTH, START, CENTER)
            String[] template = text.getSecond().split(",");
            ParagraphAlignment pa = ParagraphAlignment.BOTH;//.DISTRIBUTE;//.START;
            switch (template[1]){
                case "START": pa = ParagraphAlignment.START;
                case "CENTER": pa = ParagraphAlignment.CENTER;
            }
            new_par.setAlignment(pa);
            r1.setBold(false);
            r1.setItalic(false);
            int size = Integer.parseInt(template[0]);
            r1.setFontSize(size);
            r1.setText(text.getFirst());//+ (char) 13 + (char) 10);
            r1.setFontFamily("Times New Roman");
            try (FileOutputStream out = new FileOutputStream(output)) {
                doc.write(out);
            }
        }
    }
    public int getparagraphs(String input) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(input)))) {
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            return xwpfParagraphList.size();
        }
    }
    public void addNewParagr(String input, String output, String text) throws IOException {

        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(input)))) {
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();

            XWPFParagraph xwpfParagraph = xwpfParagraphList.get(0);
            XmlCursor cursor = xwpfParagraph.getCTP().newCursor();
            XWPFParagraph new_par = doc.insertNewParagraph(cursor);
            XWPFRun paragraphOneRunThree = new_par.createRun();
            paragraphOneRunThree.setFontSize(14);

            //for (String st : textShablon){
            //    paragraphOneRunThree.setText(st);
            //}
            paragraphOneRunThree.setText(text);

////            new_par.createRun().setText("Stupid text");
            //for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
            //    for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
//
            //    }
            //}
            // save the docs
            try (FileOutputStream out = new FileOutputStream(output)) {
                doc.write(out);
            }
        }
    }


    public void updateDocument(String input, String output, String name) throws IOException {

        /*try (InputStream is = getFileFromResource(input);
             XWPFDocument doc = new XWPFDocument(is)) {*/

        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(input)))
        ) {

            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            //Iterate over paragraph list and check for the replaceable text in each paragraph
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    String docText = xwpfRun.getText(0);
                    //replacement and setting position
                    docText = docText.replace("${name}", name);
                    xwpfRun.setText(docText, 0);
                }
            }

            // save the docs
            try (FileOutputStream out = new FileOutputStream(output)) {
                doc.write(out);
            }

        }

    }

    // get file from the resource folder.
    private InputStream getFileFromResource(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
