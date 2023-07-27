package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Dnevnik;
import com.babkin.eljournal.entity.working.FilesForDnevnik;
import com.babkin.eljournal.entity.working.FilesForDnevnikEnum;
import com.babkin.eljournal.repo.FilesForDnevnikRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilesForDnevnikService {
    @Autowired
    private FilesForDnevnikRepos filesForDnevnikRepos;

    public FilesForDnevnik addFilesForDnevnik(String datesdachi, FilesForDnevnikEnum ktocdal, String path,
                                              String status, Dnevnik dnevnik) {
        if (dnevnik == null) {
            return null;
        }
        String ksdal = ktocdal.name();
        FilesForDnevnik filesForDnevnik = filesForDnevnikRepos.findFilesForDnevnikByDnevnik_IdAndKtocdalAndPathstudent(
                dnevnik.getId(), ksdal, path);
        if (filesForDnevnik == null) {
            FilesForDnevnik filesForDnevnikNew = new FilesForDnevnik(datesdachi, ksdal, path,
                    null, null, status, null, dnevnik);
            filesForDnevnik = filesForDnevnikRepos.save(filesForDnevnikNew);
        }
        return filesForDnevnik;
        //return null;
    }

    public FilesForDnevnik findeFilesForDnevnikAndKtocdal(FilesForDnevnikEnum ktocdal, Dnevnik dnevnik){
        if (dnevnik == null){
            return null;
        }
        List<FilesForDnevnik> res = filesForDnevnikRepos.findFilesForDnevnikByDnevnik_IdAndKtocdal(dnevnik.getId(), ktocdal.name());
        if (res.size() == 0){
            return null;
        }
        int max = 0;
        int index = 0;
        for (int i=0; i<res.size(); i++){
            String str = res.get(i).getPathstudent();
            int ind = str.indexOf("~");
            if (ind == -1){
                return null;
            }
            int indprf = str.indexOf(".docx");
            if (indprf == -1)
            {
                indprf = str.indexOf(".pdf");
                if (indprf == -1) {
                    str = str.substring(str.indexOf("~") + 1);
                } else {
                    str = str.substring(str.indexOf("~") + 1, str.indexOf(".pdf"));
                }
            }else {
                str = str.substring(str.indexOf("~") + 1, str.indexOf(".docx"));
            }
            int temp = Integer.parseInt(str);
            if (i==0){
                index = 0;
                max = temp;
            } else {
                if (temp > max){
                    index = i;
                    max = temp;
                }
            }
        }
        return res.get(index);
    }
    public List<FilesForDnevnik> findAllByDnevnik(Dnevnik dnevnik, FilesForDnevnikEnum ktocdal){
        if (dnevnik == null){
            return null;
        }
        return filesForDnevnikRepos.findFilesForDnevnikByDnevnik_IdAndKtocdal(dnevnik.getId(), ktocdal.name());
    }
    public List<FilesForDnevnik> findFilesForDnevnikByKtocdalAndDatesdachiAndDnevnik(String ktosdal, String datasdachi, Dnevnik dnevnik){
        if (dnevnik == null){
            return null;
        }
        return filesForDnevnikRepos.findFilesForDnevnikByKtocdalAndDatesdachiAndDnevnik_Id(
                ktosdal, datasdachi, dnevnik.getId());
    }

    public FilesForDnevnik findById(Long id){
        return filesForDnevnikRepos.findById(id).get();
    }

    public void update(FilesForDnevnik filesForDnevnik){
        FilesForDnevnik fromDB = findById(filesForDnevnik.getId());
        //fromDB.setPathteacher(filesForDnevnik.getPathteacher());
        filesForDnevnikRepos.saveAndFlush(fromDB);
    }
}
